/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author dominik
 */
@Entity
public class Transaction extends CustomerEntity {

    @Transient
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER)
    private Player player;

    @Column
    @NotNull(message = "{NotEmpty.amount}")
    private BigDecimal amount;

    @Column(length = 4000)
    @NotEmpty(message = "{NotEmpty.comment}")
    private String comment;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate date;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("Transaction [Amount: %s, Comment: %s, Date: %s]", getAmount(), getComment(), getDate());
    }
}
