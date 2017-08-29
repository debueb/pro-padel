/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Player;
import de.appsolve.padelcampus.utils.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/reports/players")
public class AdminReportsTopPlayersController extends BaseController {

    @Autowired
    BookingDAOI bookingDAO;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping()
    public ModelAndView getIndex() throws JsonProcessingException {
        return getIndexView();
    }

    private ModelAndView getIndexView() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("admin/reports/players/index");
        List<Booking> bookings = bookingDAO.findBlockedBookings();

        Map<Player, Integer> map = new HashMap<>();
        for (Booking booking : bookings) {
            Player player = booking.getPlayer();
            Integer count = map.get(player);
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            map.put(player, count);
        }

        Map<Player, Integer> sortedMap = SortUtil.sortMap(map);

        //keep only top 20
        Iterator<Map.Entry<Player, Integer>> iterator = sortedMap.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            iterator.next();
            if (i > 20) {
                iterator.remove();
            }
            i++;
        }

        mav.addObject("chartData", objectMapper.writeValueAsString(sortedMap));

        return mav;
    }

}
