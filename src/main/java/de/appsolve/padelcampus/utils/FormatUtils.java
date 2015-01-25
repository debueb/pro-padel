/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.utils;

import static de.appsolve.padelcampus.constants.Constants.DEFAULT_LOCALE;
import static de.appsolve.padelcampus.constants.Constants.DEFAULT_TIMEZONE;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author dominik
 */
public class FormatUtils {
    
    public static final String DATE_HUMAN_READABLE_PATTERN          = "yyyy-MM-dd";
    
    public static final DateTimeFormatter DATE_WITH_DAY             = DateTimeFormat.forPattern("EEE, dd. MMM yyyy").withZone(DEFAULT_TIMEZONE).withLocale(DEFAULT_LOCALE);
    public static final DateTimeFormatter DATE_MEDIUM               = DateTimeFormat.forPattern("dd. MMM yyyy").withZone(DEFAULT_TIMEZONE).withLocale(DEFAULT_LOCALE);
    public static final DateTimeFormatter DATE_HUMAN_READABLE       = DateTimeFormat.forPattern(DATE_HUMAN_READABLE_PATTERN).withZone(DEFAULT_TIMEZONE);
    public static final DateTimeFormatter TIME_HUMAN_READABLE       = DateTimeFormat.forPattern("HH:mm").withZone(DEFAULT_TIMEZONE);
    public static final DateTimeFormatter DATE_TIME_HUMAN_READABLE  = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").withZone(DEFAULT_TIMEZONE);
    
    public static final DecimalFormat TWO_FRACTIONAL_DIGITS_FORMAT;
    
    static{
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        symbols.setDecimalSeparator('.');
        TWO_FRACTIONAL_DIGITS_FORMAT = new DecimalFormat("#.00", symbols); 
    }
}
