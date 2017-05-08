/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.constants;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.joda.time.DateTimeZone;

/**
 *
 * @author dominik
 */
public class Constants {
    
    public static final String UTF8                         = "UTF-8";
    
    public static final int FIRST_SET                       = 1;
    
    public static final int VOUCHER_NUM_CHARS               = 6;
    public static final int VOUCHER_DEFAULT_VALIDITY_IN_DAYS = 365;
    
    public static final int BOOKING_DEFAULT_VALID_FROM_HOUR     = 07;
    public static final int BOOKING_DEFAULT_VALID_FROM_MINUTE   = 00;
    public static final int BOOKING_DEFAULT_VALID_UNTIL_HOUR    = 23;
    public static final int BOOKING_DEFAULT_VALID_UNTIL_MINUTE  = 00;
    public static final int BOOKING_DEFAULT_DURATION            = 60;
    public static final int BOOKING_DEFAULT_MIN_INTERVAL        = 30;
    
    public static final String SESSION_USER                 = "user";
    public static final String SESSION_CUSTOMER             = "customer";
    public static final String SESSION_PRIVILEGES           = "privileges";
    public static final String SESSION_LOGIN_REDIRECT_PATH  = "loginRedirectPath";
    public static final String SESSION_PROFILE_REDIRECT_PATH= "profileRedirectPath";
    public static final String SESSION_BOOKING              = "booking";
    public static final String SESSION_ACCESS_LEVEL         = "accessLevel";
    public static final String SESSION_BOOKING_LIST_START_DATE  = "bookingListStartDate";
    public static final String SESSION_RESERVATION_LIST_START_DATE  = "reservationListStartDate";
    public static final String SESSION_RESERVATION_LIST_END_DATE    = "reservationListEndDate";
    public static final String SESSION_DEFAULT_LOCALE       = "sessionLocale";
    
    public static final String APPLICATION_CUSTOMER_MODULES = "customerModules";
    
    public static final String COOKIE_LOGIN_TOKEN           = "loginToken";
    
    //allow next 3 months to be selected
    public static final Integer CALENDAR_MAX_DATE           = 90;
    
    public static final Integer CANCELLATION_POLICY_DEADLINE= 24;
    
    public static final String PATH_HOME = "home";
    
    
    public static final List<String> VALID_LANGUAGES = Arrays.asList(new String[]{"de", "en", "es"});

    public static final String DEFAULT_LANGUAGE             = "de";
    public static final Locale DEFAULT_LOCALE               = new Locale(DEFAULT_LANGUAGE, "DE");
    public static final String DEFAULT_TIMEZONE_STRING      = "Europe/Berlin";
    public static final DateTimeZone DEFAULT_TIMEZONE       = DateTimeZone.forID(DEFAULT_TIMEZONE_STRING);
    public static final String DEFAULT_HOLIDAY_KEY          = "GERMANY-nw";
    public static final String NO_HOLIDAY_KEY               = "No Holidays";
    
    public static final String MAIL_NOREPLY_SENDER_NAME     = "noreply";
    
    public static final String DATA_DIR_PROFILE_PICTURES    = "profilePictures";
    public static final Integer PROFILE_PICTURE_WIDTH       = 200;
    public static final Integer PROFILE_PICTURE_HEIGHT      = 200;
    
    public static final String DATA_DIR_SUMMERNOTE_IMAGES    = "summernoteImages";
    public static final Integer SUMMERNOTE_IMAGE_WIDTH       = 800;
    public static final Integer SUMMERNOTE_IMAGE_HEIGHT      = 800;
    
    public static final String DATA_DIR_COMPANY_LOGO_IMAGES  = "companyLogoImages";
    public static final Integer COMPANY_LOGO_HEIGHT          = 88;
    
    public static final String DATA_DIR_TOUCH_ICON_IMAGE     = "touchIconImages";
    public static final Integer TOUCH_ICON_WIDTH             = 192;
    public static final Integer TOUCH_ICON_HEIGHT            = 192;
    
    public static final String DATA_DIR_STAFF_IMAGES         = "staffImages";
    public static final Integer STAFF_IMAGE_WIDTH             = 300;
    public static final Integer STAFF_IMAGE_HEIGHT            = 300;
    
    public static final String OPENSHIFT_DATA_DIR                  = "OPENSHIFT_DATA_DIR";
    
    public static final int BLOG_PAGE_SIZE                     = 10;
}
