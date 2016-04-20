/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.admin.controller.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.appsolve.padelcampus.comparators.MapValueComparator;
import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/admin/reports/players")
public class AdminReportsTopPlayersController extends BaseController{

    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    ObjectMapper objectMapper;
    
    @RequestMapping()
    public ModelAndView getIndex() throws JsonProcessingException{
        return getIndexView();
    }
    
    private ModelAndView getIndexView() throws JsonProcessingException {
        ModelAndView mav = new ModelAndView("admin/reports/players/index");
        List<Booking> bookings = bookingDAO.findBlockedBookings();
        
        Map<Player, Integer> map = new HashMap<>();
        for (Booking booking: bookings){
            Player player = booking.getPlayer();
            Integer count = map.get(player);
            if (count==null){
                count = 1;
            } else {
                count++;
            }
            map.put(player, count);
        }
        
        SortedMap<Player, Integer> sortedMap = new TreeMap<>(new MapValueComparator<>(map));
        sortedMap.putAll(map);
        mav.addObject("chartData", objectMapper.writeValueAsString(sortedMap));
        
        return mav;
    }
    
}
