package de.appsolve.padelcampus.controller.bookings;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.dao.PayPalConfigDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.PayPalConfig;
import de.appsolve.padelcampus.utils.RequestUtil;
import de.appsolve.padelcampus.utils.SessionUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/bookings")
public class BookingsPayPalController extends BookingsPaymentController{
    
    private static final Logger LOG = Logger.getLogger(BookingsPayPalController.class);
    
    @Autowired
    SessionUtil sessionUtil;
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    PayPalConfigDAOI payPalConfigDAO;
    
    public ModelAndView redirectToPaypal(Booking booking, HttpServletRequest request) throws Exception {
        if (booking.getPaymentConfirmed()){
            return BookingsController.getRedirectToSuccessView(booking);
        }
        APIContext apiContext = getApiContext();
        
        Details details = new Details();
        details.setShipping("0");
        details.setSubtotal(booking.getAmountDouble());
        details.setTax("0");

        Amount amount = new Amount();
        amount.setCurrency(booking.getCurrency().toString());
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal(booking.getAmountDouble());
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(getBookingDescription(booking));
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(RequestUtil.getBaseURL(request) + booking.getAbortUrl());
        redirectUrls.setReturnUrl(RequestUtil.getBaseURL(request) + booking.getBaseUrl().append("/paypal/return").toString());
        payment.setRedirectUrls(redirectUrls);
        
        Payment createdPayment = payment.create(apiContext);
        LOG.info("Created PayPal payment with id = "+ createdPayment.getId() + " and status = "+ createdPayment.getState());
        
        //TODO check payment state
        Iterator<Links> links = createdPayment.getLinks().iterator();
        while (links.hasNext()) {
            Links link = links.next();
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                return new ModelAndView("redirect:"+link.getHref());
            }
        }
        throw new Exception("PayPal did not return a valid approval URL");
    }
    
    @RequestMapping("booking/{UUID}/paypal/return")
    public ModelAndView onPaymentReturn(@PathVariable("UUID") String UUID, HttpServletRequest request){
        Booking booking = bookingDAO.findByUUID(UUID);
        if (booking.getPaymentConfirmed()){
            return BookingsController.getRedirectToSuccessView(booking);
        }
        try {
            Payment payment = new Payment();
            payment.setId(request.getParameter("paymentId"));
            PaymentExecution paymentExecute = new PaymentExecution();
            paymentExecute.setPayerId(request.getParameter("PayerID"));
            payment = payment.execute(getApiContext(), paymentExecute);
            //check payment status, throw exception if not successful
            String state = payment.getState();
            if (!state.equals("approved")){
                throw new Exception("PayPal returned unexpected payment status: "+state);
            }
            LOG.info("Approved PayPal payment with id = "+ payment.getId());
            booking.setPaymentConfirmed(true);
            bookingDAO.saveOrUpdate(booking);
            LOG.info("Booking payment confirmed: "+booking.getPaymentConfirmed());
            LOG.info("Booking payment confirmed: "+bookingDAO.findByUUID(UUID).getPaymentConfirmed());
            return BookingsController.getRedirectToSuccessView(booking);
        } catch (Exception e){
            LOG.error("Error while executing paypal payment", e);
            ModelAndView bookingConfirmView = BookingsController.getBookingConfirmView(booking);
            bookingConfirmView.addObject("error", msg.get("PayPalError", new String[]{e.getMessage()}));
            return bookingConfirmView;
        }
    }
    
    private APIContext getApiContext() throws PayPalRESTException{
        PayPalConfig config = payPalConfigDAO.findFirst();
        APIContext apiContext = new APIContext(config.getClientId(), config.getClientSecret(), config.getPayPalEndpoint().getMode());
        return apiContext;
    }
}