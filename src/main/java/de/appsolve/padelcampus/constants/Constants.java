/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.constants;

import java.util.Locale;
import org.joda.time.DateTimeZone;

/**
 *
 * @author dominik
 */
public class Constants {
    
    public static final String UTF8                         = "UTF-8";
    
    public static final int NUMBER_OF_SETS                  = 3;
    public static final int FIRST_SET                       = 1;
    public static final int MATCH_WIN_FACTOR                = 3;
    public static final int MATCH_PLAY_FACTOR               = 1;
    
    public static final int VOUCHER_NUM_CHARS               = 6;
    public static final int VOUCHER_DEFAULT_VALIDITY_IN_DAYS = 365;
    
    public static final int BOOKING_DEFAULT_VALID_FROM_HOUR     = 10;
    public static final int BOOKING_DEFAULT_VALID_FROM_MINUTE   = 00;
    public static final int BOOKING_DEFAULT_VALID_UNTIL_HOUR    = 21;
    public static final int BOOKING_DEFAULT_VALID_UNTIL_MINUTE  = 30;
    public static final int BOOKING_DEFAULT_DURATION            = 60;
    public static final int BOOKING_DEFAULT_MIN_INTERVAL        = 30;
    
    
    public static final String MANDRILL_API_KEY             = "U-kIRv0utJzLLWMkZVVZtQ";
    public static final String CONTACT_FORM_RECIPIENT_MAIL  = "d.wisskirchen@gmail.com";
    public static final String CONTACT_FORM_RECIPIENT_NAME  = "Dominik Wisskirchen";
    
    public static final String SESSION_USER                 = "user";
    public static final String SESSION_PRIVILEGES           = "privileges";
    public static final String SESSION_LOGIN_REDIRECT_PATH  = "loginRedirectPath";
    public static final String SESSION_BOOKING              = "booking";
    
    public static final String COOKIE_LOGIN_TOKEN           = "loginToken";
    
    //allow next 3 months to be selected
    public static final Integer CALENDAR_MAX_DATE           = 90;
    
    public static final Integer CANCELLATION_POLICY_DEADLINE= 24;
    
    
    public static final String DEFAULT_LOCALE_STRING        = "de_DE";
    public static final Locale DEFAULT_LOCALE               = new Locale("de", "DE");
    public static final String DEFAULT_TIMEZONE_STRING      = "Europe/Berlin";
    public static final DateTimeZone DEFAULT_TIMEZONE       = DateTimeZone.forID(DEFAULT_TIMEZONE_STRING);
    public static final String DEFAULT_HOLIDAY_KEY          = "GERMANY-nw";
    public static final String NO_HOLIDAY_KEY               = "No Holidays";
    
    public static final String MAIL_NOREPLY_SENDER_NAME     = "noreply";
    
}
