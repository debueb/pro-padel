/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.api.controllers;

import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.dao.BookingDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.Contact;
import de.appsolve.padelcampus.exceptions.MailException;
import de.appsolve.padelcampus.utils.MailUtils;
import de.appsolve.padelcampus.utils.Msg;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author dominik
 */
@Controller()
@RequestMapping("/api/recordings")
public class ApiRecordingsController {
    
    @Autowired
    BookingDAOI bookingDAO;
    
    @Autowired
    MailUtils mailUtils;
    
    @Autowired
    Msg msg;
    
    @RequestMapping(method = POST, value = "/booking/{bookingUUID}")
    public ResponseEntity<String> bookingCallback(@PathVariable("bookingUUID") String bookingUUID, @RequestParam(required = true) String youtubeVideoId, HttpServletRequest request) throws Exception {
        Booking booking = bookingDAO.findByUUID(bookingUUID);
        if (booking == null){
            return new ResponseEntity<>("Booking does not exist", HttpStatus.PRECONDITION_FAILED);
        }
        sendMail(booking.getPlayer(), booking.getPlayer().toString(), youtubeVideoId, request);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
    
    @RequestMapping(method = POST, value = "/email")
    public ResponseEntity<String> emailCallback(@RequestParam String email, @RequestParam(required = true) String youtubeVideoId, HttpServletRequest request) throws Exception {
        Contact contact = new Contact();
        contact.setEmailAddress(email);
        sendMail(contact, "", youtubeVideoId, request);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    private void sendMail(EmailContact contact, String name, String youtubeVideoId, HttpServletRequest request) throws MailException, IOException {
        String youtubeUrl = String.format("https://www.youtube.com/watch?v=%s", youtubeVideoId);
        
        Mail mail = new Mail();
        mail.addRecipient(contact);
        mail.setSubject(msg.get("YoutubeVideoReadySubject"));
        mail.setBody(msg.get("YoutubeVideoReadyBody", new Object[]{name, youtubeUrl, ""}));
        mailUtils.send(mail, request);
    }
}
