/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.spring;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * @author dominik
 */

/**
 * Custom PropertyEditorSupport to convert from String to
 * Date using JODA.
 */
public class LocalDateEditor extends PropertyEditorSupport {
// ------------------------------ FIELDS ------------------------------

    private final DateTimeFormatter formatter;
    private final boolean allowEmpty;

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Create a new DateTimeEditor instance, using the given format for
     * parsing and rendering.
     * <p/>
     * The "allowEmpty" parameter states if an empty String should be allowed
     * for parsing, i.e. get interpreted as null value. Otherwise, an
     * IllegalArgumentException gets thrown.
     *
     * @param dateFormat DateFormat to use for parsing and rendering
     * @param allowEmpty if empty strings should be allowed
     */
    public LocalDateEditor(String dateFormat, boolean allowEmpty) {
        this.formatter = DateTimeFormat.forPattern(dateFormat);
        this.allowEmpty = allowEmpty;
    }

// ------------------------ OVERRIDING METHODS ------------------------

    /**
     * Format the YearMonthDay as String, using the specified format.
     *
     * @return DateTime formatted string
     */
    @Override
    public String getAsText() {
        return getValue() != null ? new LocalDate(getValue()).toString(formatter) : "";
    }

    /**
     * Parse the value from the given text, using the specified format.
     *
     * @param text
     * @throws IllegalArgumentException
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        } else {
            setValue(new LocalDate(formatter.parseDateTime(text)));
        }
    }
}