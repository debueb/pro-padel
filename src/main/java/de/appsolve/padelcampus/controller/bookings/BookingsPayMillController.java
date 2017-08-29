package de.appsolve.padelcampus.controller.bookings;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.paymill.context.PaymillContext;
import com.paymill.models.Transaction;
import com.paymill.services.TransactionService;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.PayMillConfigDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.PayMillConfig;
import de.appsolve.padelcampus.utils.SessionUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/bookings")
public class BookingsPayMillController extends BookingsPaymentController {

    private static final Logger LOG = Logger.getLogger(BookingsPayMillController.class);

    @Autowired
    SessionUtil sessionUtil;

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    PayMillConfigDAOI payMillConfigDAO;


    public ModelAndView redirectToDirectDebit(Booking booking) throws Exception {
        return new ModelAndView("redirect:/bookings/booking/" + booking.getUUID() + "/directdebit");
    }

    public ModelAndView redirectToCreditCard(Booking booking) throws Exception {
        return new ModelAndView("redirect:/bookings/booking/" + booking.getUUID() + "/creditcard");
    }

    @RequestMapping(value = "booking/{UUID}/directdebit", method = GET)
    public ModelAndView onGetDebitCard(@PathVariable("UUID") String UUID) {
        Booking booking = bookingDAO.findByUUID(UUID);
        return getDirectDebitView(booking);
    }

    @RequestMapping(value = "booking/{UUID}/creditcard", method = GET)
    public ModelAndView onGetCreditCard(@PathVariable("UUID") String UUID) {
        Booking booking = bookingDAO.findByUUID(UUID);
        return getCreditCardView(booking);
    }

    @RequestMapping(value = "booking/{UUID}/directdebit", method = POST)
    public ModelAndView onPostDebitCard(@PathVariable("UUID") String UUID, @RequestParam String token) {
        Booking booking = bookingDAO.findByUUID(UUID);
        ModelAndView mav = getDirectDebitView(booking);
        return handlePost(mav, booking, token);
    }

    @RequestMapping(value = "booking/{UUID}/creditcard", method = POST)
    public ModelAndView onPostCreditCard(@PathVariable("UUID") String UUID, @RequestParam String token) {
        Booking booking = bookingDAO.findByUUID(UUID);
        ModelAndView mav = getCreditCardView(booking);
        return handlePost(mav, booking, token);
    }

    @RequestMapping(value = "booking/{UUID}/translate", method = GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String translate(@RequestParam("key") String key, HttpServletResponse response) throws IOException {
        String translation = msg.get(key);
        LOG.error("Error while creating PayMill payment: " + translation);
        return translation;
    }

    private ModelAndView handlePost(ModelAndView mav, Booking booking, String token) {
        if (StringUtils.isEmpty(token)) {
            mav.addObject("error", msg.get("MissingRequiredPaymentToken"));
            return mav;
        }
        if (booking.getConfirmed()) {
            mav.addObject("error", msg.get("BookingAlreadyConfirmed"));
            return mav;
        }
        try {
            PayMillConfig config = payMillConfigDAO.findFirst();
            PaymillContext paymillContext = new PaymillContext(config.getPrivateApiKey());
            TransactionService transactionService = paymillContext.getTransactionService();
            BigDecimal amount = booking.getAmount().multiply(new BigDecimal("100"), MathContext.DECIMAL128);
            Transaction transaction = transactionService.createWithToken(token, amount.intValue(), booking.getCurrency().toString(), getBookingDescription(booking));
            if (!transaction.getStatus().equals(Transaction.Status.CLOSED)) {
                throw new Exception("Payment Backend Returned Unexpected Status: [Code: " + transaction.getStatus() + ", Response Code: " + transaction.getResponseCode() + ", Response Code Detail: " + transaction.getResponseCodeDetail() + "]");
            }
            booking.setPaymentConfirmed(true);
            bookingDAO.saveOrUpdate(booking);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            mav.addObject("error", e.getMessage());
            return mav;
        }
        return BookingsController.getRedirectToSuccessView(booking);
    }

    private ModelAndView getDirectDebitView(Booking booking) {
        ModelAndView directDebitView = new ModelAndView("bookings/directdebit", "Booking", booking);
        addPublicApiKeyToModel(directDebitView);
        return directDebitView;
    }

    private ModelAndView getCreditCardView(Booking booking) {
        ModelAndView creditCardView = new ModelAndView("bookings/creditcard", "Booking", booking);
        addPublicApiKeyToModel(creditCardView);
        return creditCardView;
    }

    private void addPublicApiKeyToModel(ModelAndView directDebitView) {
        PayMillConfig config = payMillConfigDAO.findFirst();
        directDebitView.addObject("PAYMILL_PUBLIC_KEY", config.getPublicApiKey());
        List<String> years = new ArrayList<>();
        LocalDate date = new LocalDate();
        years.add(date.toString("yyyy"));
        int i = 0;
        while (i < 10) {
            date = date.plusYears(1);
            years.add(date.toString("yyyy"));
            i++;
        }
        directDebitView.addObject("Years", years);
    }
}