/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.BookingType;
import de.appsolve.padelcampus.constants.Currency;
import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.utils.FormatUtils;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Set;

import static de.appsolve.padelcampus.utils.FormatUtils.TWO_FRACTIONAL_DIGITS_FORMAT;

/**
 * @author dominik
 */
@Entity
public class Booking extends CustomerEntity {

    @Transient
    private static final long serialVersionUID = 1L;
    @ManyToMany(fetch = FetchType.LAZY)
    Set<Player> players;
    @OneToOne
    private Player player;
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate bookingDate;
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
    private LocalTime bookingTime;
    @Column
    private Long duration;
    @Column
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Column
    private BigDecimal amount;
    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Column
    @Enumerated(EnumType.STRING)
    private BookingType bookingType;
    @OneToOne
    private Voucher voucher;
    @Column
    @Length(max = 8000, message = "{Length.Booking.comment}")
    private String comment;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<OfferOption> offerOptions;
    /**
     * indicates the time that the user has entered the checkout (payment) phase
     * to indicate that the court should be blocked to avoid duplicate bookings
     * this booking is to be removed if the payment is not confirmed after the session timeout
     * or if the user cancels the payment
     */
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime blockingTime;
    /**
     * if the booking has been confirmed to the user within the UI
     */
    @Column
    private Boolean confirmed;
    /**
     * if the booking has been cancelled
     */
    @Column
    private Boolean cancelled;
    /**
     * reason for cancellation
     */
    private String cancelReason;
    /**
     * if the booking has been confirmed to the user by mail
     */
    @Column
    private Boolean confirmationMailSent;
    /**
     * if the payment has been confirmed by the payment provider
     */
    @Column
    private Boolean paymentConfirmed;
    @Column
    private String UUID;
    @ManyToOne
    private Offer offer;
    @ManyToOne
    private Event event;
    @Column
    private Boolean publicBooking;

    @Column
    private String paypalPaymentId;

    @OneToOne
    private Community community;

    @Transient
    private Boolean playerParticipates;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalTime getBookingEndTime() {
        if (bookingTime != null && duration != null) {
            return bookingTime.plusMinutes(getDuration().intValue());
        }
        return null;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountDouble() {
        if (getAmount() == null) {
            return null;
        }
        return TWO_FRACTIONAL_DIGITS_FORMAT.format(getAmount().doubleValue());
    }

    public String getAmountInt() {
        if (getAmount() == null) {
            return null;
        }
        BigDecimal value = amount.multiply(new BigDecimal("100"), MathContext.DECIMAL64);
        return value.toPlainString();
    }

    public BookingType getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = bookingType;
    }

    public LocalDateTime getBlockingTime() {
        return blockingTime;
    }

    public void setBlockingTime(LocalDateTime blockingTime) {
        this.blockingTime = blockingTime;
    }

    public Boolean getConfirmed() {
        return confirmed == null ? Boolean.FALSE : confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean getCancelled() {
        return cancelled == null ? Boolean.FALSE : cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Boolean getConfirmationMailSent() {
        return confirmationMailSent;
    }

    public void setConfirmationMailSent(Boolean confirmationMailSent) {
        this.confirmationMailSent = confirmationMailSent;
    }

    public Boolean getPaymentConfirmed() {
        return paymentConfirmed == null ? Boolean.FALSE : paymentConfirmed;
    }

    public void setPaymentConfirmed(Boolean paymentConfirmed) {
        this.paymentConfirmed = paymentConfirmed;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Boolean getPublicBooking() {
        return publicBooking == null ? Boolean.FALSE : publicBooking;
    }

    public void setPublicBooking(Boolean publicBooking) {
        this.publicBooking = publicBooking;
    }

    public Set<OfferOption> getOfferOptions() {
        return offerOptions;
    }

    public void setOfferOptions(Set<OfferOption> offerOptions) {
        this.offerOptions = offerOptions;
    }

    public String getPaypalPaymentId() {
        return paypalPaymentId;
    }

    public void setPaypalPaymentId(String paypalPaymentId) {
        this.paypalPaymentId = paypalPaymentId;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Boolean getPlayerParticipates() {
        return playerParticipates;
    }

    public void setPlayerParticipates(Boolean playerParticipates) {
        this.playerParticipates = playerParticipates;
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s - %s", getPlayer(), getName(), getBookingDate() == null ? "no booking date" : getBookingDate().toString(FormatUtils.DATE_WITH_DAY), getBookingTime() == null ? "no booking time" : getBookingTime().toString(FormatUtils.TIME_HUMAN_READABLE));
    }

    public StringBuilder getBaseUrl() {
        StringBuilder sb = new StringBuilder();
        if (offer != null) {
            sb.append("/bookings/booking/");
        } else if (event != null) {
            sb.append("/events/bookings/");
        }
        sb.append(getUUID());
        return sb;
    }

    @Transient
    public String getSuccessUrl() {
        StringBuilder sb = getBaseUrl();
        sb.append("/success");
        return sb.toString();
    }

    @Transient
    public String getAbortUrl() {
        StringBuilder sb = getBaseUrl();
        sb.append("/abort");
        return sb.toString();
    }

    @Transient
    public String getName() {
        if (getOffer() != null) {
            return getOffer().getName();
        }
        if (getEvent() != null) {
            return getEvent().getName();
        }
        return null;
    }

    @Override
    public int compareTo(BaseEntityI o) {
        if (o instanceof Booking) {
            Booking other = (Booking) o;
            if (getBookingDate() != null && other.getBookingDate() != null) {
                int result = getBookingDate().compareTo(other.getBookingDate());
                if (result != 0) {
                    return result;
                }
                if (getBookingTime() != null && other.getBookingTime() != null) {
                    return getBookingTime().compareTo(other.getBookingTime());
                }
            }
        }
        return super.compareTo(o);
    }


}
