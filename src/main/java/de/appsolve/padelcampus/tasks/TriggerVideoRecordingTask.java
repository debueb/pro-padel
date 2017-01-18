/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.tasks;

import com.jcabi.ssh.SSH;
import com.jcabi.ssh.Shell;
import static de.appsolve.padelcampus.constants.Constants.DEFAULT_TIMEZONE;
import de.appsolve.padelcampus.constants.OfferOptionType;
import de.appsolve.padelcampus.db.dao.BookingBaseDAOI;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.db.model.OfferOption;
import de.appsolve.padelcampus.reporting.ErrorReporter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author dominik
 */
@Component
public class TriggerVideoRecordingTask {

    private static final Logger LOG = Logger.getLogger(TriggerVideoRecordingTask.class);

    @Autowired
    BookingBaseDAOI bookingBaseDAO;
    
    @Autowired
    ErrorReporter errorReporter;

    //@Scheduled(cron = "0 * * * * ?") //second minute hour day month year, * = any, */5 = every 5
    @Scheduled(cron = "0 0/30 * * * ?") //second minute hour day month year, * = any, */5 = every 5
    public void triggerVideoRecording() {
        try {
            LocalDate date = new LocalDate();
            DateTime time = new DateTime(DEFAULT_TIMEZONE);
            time = time.withSecondOfMinute(0).withMillisOfSecond(0);
            LocalTime localTime = time.toLocalTime();

            LOG.info(String.format("Looking for bookings eligible to record at %s", localTime));
            List<Booking> bookings = bookingBaseDAO.findCurrentBookingsWithOfferOptions(date, localTime);
            LOG.info(String.format("Found %s bookings eligible for video recording", bookings.size()));

            for (Booking booking : bookings) {
                if (booking.getOfferOptions() != null) {
                    for (OfferOption offerOption : booking.getOfferOptions()) {
                        if (offerOption.getOfferOptionType().equals(OfferOptionType.VideoRecording)) {
                            try {
                                LOG.info(String.format("Triggering video recording for booking %s", booking));
                                Shell ssh = new Shell.Verbose(
                                    new SSH(offerOption.getCameraUrl(), offerOption.getCameraPort(), offerOption.getCameraUser(), offerOption.getCameraKey())
                                );
                                ByteArrayOutputStream stdOutStream = new ByteArrayOutputStream();
                                ByteArrayOutputStream stdErrStream = new ByteArrayOutputStream();

                                String callbackURL = String.format("%s/bookings/recording/%s", booking.getCustomer().getHostUrl(), booking.getUUID());
                                int code = ssh.exec(String.format("nohup ~/record.sh -d %s -c %s > /tmp/record.log 2>&1 &", booking.getDuration()*60, callbackURL), null, stdOutStream, stdErrStream);
                                LOG.info(String.format("Remote host returned [code: %s, stdout: %s, stderr: %s]", code, stdOutStream.toString("UTF-8"), stdErrStream.toString("UTF-8")));
                            } catch (UnknownHostException ex) {
                                LOG.info(ex);
                            } catch (UnsupportedEncodingException ex) {
                                LOG.info(ex);
                            } catch (IOException ex) {
                                LOG.info(ex);
                            }
                        }
                    }
                }
            }
        } catch (Throwable t){
            errorReporter.notify(t);
        }
    }
}
