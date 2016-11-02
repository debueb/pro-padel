/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.bookings;

import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/bookings/recording")
public class BookingsRecordingController {
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    MailUtils mailUtils;
    
    @Autowired
    Msg msg;
    
    @RequestMapping(method = POST, value = "/{bookingUUID}")
    public ResponseEntity<String> recordingCallback(@PathVariable("bookingUUID") String bookingUUID, HttpServletRequest request) throws Exception {
        Booking booking = bookingDAO.findByUUID(bookingUUID);
        if (booking == null){
            return new ResponseEntity<>("Booking does not exist", HttpStatus.PRECONDITION_FAILED);
        }
        String youtubeVideoId = request.getParameter(("youtubeVideoId"));
        if (StringUtils.isEmpty(youtubeVideoId)){
            return new ResponseEntity<>("Missing youtubeVideoId param", HttpStatus.BAD_REQUEST);
        }
        String youtubeUrl = String.format("https://www.youtube.com/watch?v=%s", youtubeVideoId);
        
        //notify user
        Mail mail = new Mail();
        mail.addRecipient(booking.getPlayer());
        mail.setSubject(msg.get("YoutubeVideoReadySubject"));
        mail.setBody(msg.get("YoutubeVideoReadyBody", new Object[]{booking.getPlayer(), youtubeUrl, booking.getHostUrl()}));
        mailUtils.send(mail, request);
        
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }
    
}
