/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import de.appsolve.padelcampus.constants.CalendarWeekDay;
import static de.appsolve.padelcampus.constants.Constants.VOUCHER_NUM_CHARS;
import de.appsolve.padelcampus.db.model.Offer;
import de.appsolve.padelcampus.db.model.Voucher;
import java.util.Set;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 *
 * @author dominik
 */
public class VoucherUtil {
    
    public static Voucher createNewVoucher(Voucher model){
        return createNewVoucher(model.getComment(), model.getDuration(), model.getValidUntil(), model.getValidFromTime(), model.getValidUntilTime(), model.getCalendarWeekDays(), model.getOffers());
    }
    
    public static Voucher createNewVoucher(String comment, Long duration, LocalDate validUntil, LocalTime validFromTime, LocalTime validUntilTime, Set<CalendarWeekDay> calendarWeekDays, Set<Offer> offers) {
        String UUID = RandomStringUtils.randomAlphanumeric(VOUCHER_NUM_CHARS);
        Voucher voucher = new Voucher();
        voucher.setUUID(UUID);
        voucher.setComment(comment);
        voucher.setDuration(duration);
        voucher.setValidUntil(validUntil);
        voucher.setValidFromTime(validFromTime);
        voucher.setValidUntilTime(validUntilTime);
        voucher.setCalendarWeekDays(calendarWeekDays);
        voucher.setOffers(offers);
        return voucher;
    }
    
}
