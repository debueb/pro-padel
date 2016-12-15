/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.bookings;

import de.appsolve.padelcampus.controller.BaseController;
import de.appsolve.padelcampus.db.model.Booking;
import de.appsolve.padelcampus.utils.FormatUtils;

/**
 *
 * @author dominik
 */
class BookingsPaymentController extends BaseController {
    
    protected String getBookingDescription(Booking booking) {
        return msg.get("Booking")+" "+booking.getBookingDate().toString(FormatUtils.DATE_MEDIUM) + " "+booking.getBookingTime().toString(FormatUtils.TIME_HUMAN_READABLE) + " - "+booking.getAmountDouble()+" "+booking.getCurrency();
    }
}
