/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.test;

import com.google.common.collect.Sets;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.db.model.Contact;
import de.appsolve.padelcampus.utils.MailUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author dominik
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MailTest extends TestBase {
    
    @Autowired
    MailUtils mailUtils;

    private static final Logger LOG = Logger.getLogger(MailTest.class);

    @Test
    public void sendMail() throws Exception {
        Mail mail = new Mail();
        mail.setBody("Test Email @#$%^&.\n\nThis is a new line.");
        mail.setFrom("dominik@pro-padel.de");
        Contact contact = new Contact();
        contact.setEmailAddress("d.wisskirchen@gmail.com");
        contact.setEmailDisplayName("Dominik Wißkirchen");
        mail.setRecipients(Sets.newHashSet(new EmailContact[]{contact}));
        mail.setReplyTo("noreply@pro-padel.de");
        mail.setSubject(MailTest.class.getSimpleName());
        mailUtils.send(mail, null);
    }
}
