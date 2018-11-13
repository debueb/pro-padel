package de.appsolve.padelcampus.constants;

public enum BookingMailVariables {
    BOOKING_PLAYER,
    BOOKING_DATE,
    BOOKING_TIME,
    BOOKING_OFFER,
    BOOKING_PAYMENT_METHOD,
    BOOKING_AMOUNT,
    BOOKING_CURRENCY,
    BOOKING_CANCELLATION_POLICY_DEADLINE,
    BOOKING_CANCELLATION_URL,
    BOOKING_INVOICE_URL,
    HOMEPAGE_URL;

    public String getKey() {
        return name();
    }
}
