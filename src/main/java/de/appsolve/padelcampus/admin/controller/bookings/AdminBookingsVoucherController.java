/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.bookings;

import de.appsolve.padelcampus.admin.controller.AdminBaseController;
import de.appsolve.padelcampus.constants.CalendarWeekDay;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.EventDAOI;
import de.appsolve.padelcampus.db.dao.GameDAOI;
import de.appsolve.padelcampus.db.dao.OfferDAOI;
import de.appsolve.padelcampus.db.dao.VoucherDAOI;
import de.appsolve.padelcampus.db.dao.generic.BaseEntityDAOI;
import de.appsolve.padelcampus.db.model.*;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.spring.LocalDateEditor;
import de.appsolve.padelcampus.utils.RequestUtil;
import de.appsolve.padelcampus.utils.VoucherUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.Map.Entry;

import static de.appsolve.padelcampus.constants.Constants.*;
import static de.appsolve.padelcampus.utils.FormatUtils.DATE_HUMAN_READABLE_PATTERN;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/bookings/voucher")
public class AdminBookingsVoucherController extends AdminBaseController<Voucher> {

    private static final Logger LOG = Logger.getLogger(AdminBookingsVoucherController.class);

    @Autowired
    VoucherDAOI voucherDAO;

    @Autowired
    OfferDAOI offerDAO;

    @Autowired
    EventDAOI eventDAO;

    @Autowired
    GameDAOI gameDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new LocalDateEditor(DATE_HUMAN_READABLE_PATTERN, false));
        binder.registerCustomEditor(SortedSet.class, "offers", new CustomCollectionEditor(SortedSet.class) {
            @Override
            protected Object convertElement(Object element) {
                Long id = Long.parseLong((String) element);
                return offerDAO.findById(id);
            }
        });
    }

    @Override
    public ModelAndView showAddView(HttpServletRequest request) {
        return getEditView(createNewInstance());
    }

    @RequestMapping(value = {"send"}, method = GET)
    public ModelAndView showSendView() {
        return getSendView(createNewInstance());
    }

    @Override
    public ModelAndView getEditView(Voucher voucher) {
        ModelAndView mav = super.getEditView(voucher);
        addObjects(mav);
        mav.addObject("voucherCount", 1);
        return mav;
    }

    private ModelAndView getSendView(Voucher voucher) {
        ModelAndView mav = new ModelAndView("/" + getModuleName() + "/send", "Model", voucher);
        addObjects(mav);
        mav.addObject("Events", eventDAO.findAllActive());
        return mav;
    }

    @Override
    @RequestMapping(value = {"add", "edit/{modelId}"}, method = POST)
    public ModelAndView postEditView(@ModelAttribute("Model") Voucher model, HttpServletRequest request, BindingResult result) {
        ModelAndView editView = getEditView(model);
        Integer voucherCount = Integer.parseInt(request.getParameter("voucherCount"));
        editView.addObject("voucherCount", voucherCount);
        validator.validate(model, result);
        if (StringUtils.isEmpty(model.getComment())) {
            result.addError(new ObjectError("comment", msg.get("NotEmpty.comment")));
        }
        if (!result.hasErrors()) {
            List<Voucher> vouchers = new ArrayList<>();
            for (int i = 0; i < voucherCount; i++) {
                Voucher voucher = VoucherUtil.createNewVoucher(model);
                voucherDAO.saveOrUpdate(voucher);
                vouchers.add(voucher);
            }
            editView.addObject("Vouchers", vouchers);
        }
        return editView;
    }

    @RequestMapping(value = {"send"}, method = POST)
    public ModelAndView postSendView(@ModelAttribute("Model") Voucher model, HttpServletRequest request, BindingResult result) {
        validator.validate(model, result);
        String[] eventIds = request.getParameterValues("events");
        if (eventIds == null) {
            result.addError(new ObjectError("events", msg.get("NotEmpty.events")));
            return getSendView(model);
        }
        if (!result.hasErrors()) {
            for (String eventId : eventIds) {
                Long id = Long.parseLong(eventId);
                Event event = eventDAO.findByIdFetchWithParticipantsAndPlayers(id);

                Map<Player, List<Game>> playerGameMap = new HashMap<>();

                List<Game> eventGames = gameDAO.findByEvent(event);

                for (Game game : eventGames) {
                    Voucher voucher = VoucherUtil.createNewVoucher(model);
                    voucher.setComment(event.getName());
                    voucher.setGame(game);
                    voucherDAO.saveOrUpdate(voucher);

                    game.setVoucherUUID(voucher.getUUID());
                    gameDAO.saveOrUpdate(game);

                    Set<Participant> participants = game.getParticipants();
                    for (Participant participant : participants) {
                        if (participant instanceof Team) {
                            Team team = (Team) participant;
                            Set<Player> players = team.getPlayers();
                            for (Player player : players) {
                                addGame(playerGameMap, player, game);
                            }
                        } else if (participant instanceof Player) {
                            Player player = (Player) participant;
                            addGame(playerGameMap, player, game);
                        }
                    }
                }

                for (Entry<Player, List<Game>> entry : playerGameMap.entrySet()) {
                    Player player = entry.getKey();
                    List<Game> games = entry.getValue();

                    StringBuilder sb = new StringBuilder();
                    sb.append(msg.get("NewVoucherListEmailBodyStart", new Object[]{player.toString(), event.toString()}));
                    for (Game game : games) {
                        sb.append(game);
                        sb.append(": ");
                        sb.append(game.getVoucherUUID());
                        sb.append("\n");
                    }
                    sb.append("\n\n");
                    sb.append(msg.get("NewVoucherListEmailBodyEnd", new Object[]{RequestUtil.getBaseURL(request)}));

                    Mail mail = new Mail();
                    mail.addRecipient(player);
                    mail.setSubject(event.getName());
                    mail.setBody(sb.toString());
                    try {
                        mailUtils.send(mail, request);
                    } catch (MailException ex) {
                        LOG.error("Error while sending voucher list to " + player.getEmail(), ex);
                    }
                }
            }
        }
        return redirectToIndex(request);
    }

    @Override
    public BaseEntityDAOI getDAO() {
        return voucherDAO;
    }

    @Override
    public String getModuleName() {
        return "admin/bookings/voucher";
    }

    @Override
    protected Voucher createNewInstance() {
        Voucher voucher = new Voucher();
        voucher.setDuration(60L);
        voucher.setValidUntil(new LocalDate(DEFAULT_TIMEZONE).plusDays(VOUCHER_DEFAULT_VALIDITY_IN_DAYS));
        voucher.setValidFromHour(BOOKING_DEFAULT_VALID_FROM_HOUR);
        voucher.setValidFromMinute(BOOKING_DEFAULT_VALID_FROM_MINUTE);
        voucher.setValidUntilHour(BOOKING_DEFAULT_VALID_UNTIL_HOUR);
        voucher.setValidUntilMinute(BOOKING_DEFAULT_VALID_UNTIL_MINUTE);
        voucher.setCalendarWeekDays(new HashSet<>(Arrays.asList(CalendarWeekDay.values())));
        return voucher;
    }

    private void addObjects(ModelAndView mav) {
        mav.addObject("Durations", new Integer[]{60, 90, 120});
        mav.addObject("WeekDays", CalendarWeekDay.values());
        mav.addObject("Offers", offerDAO.findAll());
    }

    private void addGame(Map<Player, List<Game>> playerGameMap, Player player, Game game) {
        List<Game> games;
        if (playerGameMap.containsKey(player)) {
            games = playerGameMap.get(player);
        } else {
            games = new ArrayList<>();
        }
        games.add(game);
        playerGameMap.put(player, games);
    }
}
