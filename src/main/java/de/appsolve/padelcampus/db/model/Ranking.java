package de.appsolve.padelcampus.db.model;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 * @author dominik
 */
@Entity
public class Ranking extends CustomerEntity {

    public Ranking() {
        //empty
    }

    public Ranking(Participant participant, BigDecimal value, LocalDate date) {
        this.participant = participant;
        this.value = value;
        this.date = date;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    private Participant participant;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate date;

    @Column
    private BigDecimal value;

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public int compareTo(BaseEntityI o) {
        if (o instanceof Ranking) {
            Ranking other = (Ranking) o;
            int result = 0;
            if (value == null && other.value == null) {
                return result;
            }
            if (value == null) {
                return -1;
            }
            if (other.value == null) {
                return 1;
            }
            result = other.value.compareTo(value);
            if (result == 0) {
                result = participant.compareTo(other.participant);
            }
            return result;
        }
        return super.compareTo(o);
    }
}