package de.appsolve.padelcampus.utils;

import com.sparkpost.Client;
import com.sparkpost.exception.SparkPostException;
import com.sparkpost.model.*;
import com.sparkpost.resources.ResourceTransmissions;
import com.sparkpost.transport.RestConnection;
import de.appsolve.padelcampus.data.Attachment;
import de.appsolve.padelcampus.data.CustomerI;
import de.appsolve.padelcampus.data.EmailContact;
import de.appsolve.padelcampus.data.Mail;
import de.appsolve.padelcampus.exceptions.MailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author dominik
 */
@Component
public class MailUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(".+@.+\\.[a-zA-Z]{2,}");
    @Autowired
    SessionUtil sessionUtil;
    @Autowired
    Environment environment;

    public void send(Mail mail, HttpServletRequest request) throws MailException, IOException {

        String from = environment.getProperty("SPARKPOST_DEFAULT_SENDER");
        if (request != null) {
            CustomerI customer = sessionUtil.getCustomer(request);
            if (customer != null) {
                if (!StringUtils.isEmpty(customer.getDefaultEmail())) {
                    from = customer.getDefaultEmail();
                }
            }
        }
        String replyTo = StringUtils.isEmpty(mail.getFrom()) ? from : mail.getFrom();

        Client client = new Client(environment.getProperty("SPARKPOST_API_KEY"));

        try {
            TransmissionWithRecipientArray transmission = new TransmissionWithRecipientArray();

            List<RecipientAttributes> recipientArray = new ArrayList<>();
            for (EmailContact contact : mail.getRecipients()) {
                if (EMAIL_PATTERN.matcher(contact.getEmailAddress()).matches()) {
                    RecipientAttributes recipientAttribs = new RecipientAttributes();
                    recipientAttribs.setAddress(new AddressAttributes(contact.getEmailAddress()));
                    recipientArray.add(recipientAttribs);
                }
            }
            if (recipientArray.isEmpty()) {
                throw new MailException("No recipient specified for mail " + mail);
            }
            transmission.setRecipientArray(recipientArray);

            TemplateContentAttributes contentAttributes = new TemplateContentAttributes();
            contentAttributes.setFrom(new AddressAttributes(from));
            contentAttributes.setReplyTo(replyTo);

            if (StringUtils.isEmpty(mail.getTemplateId()) || mail.getTemplateId().equals("TextEmail")) {
                contentAttributes.setSubject(mail.getSubject());
                contentAttributes.setHtml(getHTML(mail.getBody()));
                contentAttributes.setText(mail.getBody());
                if (mail.getAttachments() != null) {
                    contentAttributes.setAttachments(getAttachmentAttributes(mail.getAttachments()));
                }
            } else {
                contentAttributes.setTemplateId(mail.getTemplateId());
            }

            transmission.setContentAttributes(contentAttributes);

            RestConnection connection = new RestConnection(client);
            ResourceTransmissions.create(connection, 0, transmission);
        } catch (SparkPostException e) {
            throw new MailException(e.getMessage(), e);
        }
    }

    private List<AttachmentAttributes> getAttachmentAttributes(Set<Attachment> attachments) {
        return attachments.stream().map(attachment -> {
            AttachmentAttributes attributes = new AttachmentAttributes();
            attributes.setName(attachment.getName());
            attributes.setType(attachment.getType());
            attributes.setData(attachment.getData());
            return attributes;
        }).collect(Collectors.toList());
    }

    private String getHTML(String string) {
        string = string.replaceAll("(\r\n|\n)", "<br />");
        return string;
    }
}
