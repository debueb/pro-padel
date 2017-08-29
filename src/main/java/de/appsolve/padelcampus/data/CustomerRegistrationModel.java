/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.Customer;
import de.appsolve.padelcampus.db.model.Player;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author dominik
 */
public class CustomerRegistrationModel {

    @NotNull
    @Valid
    private Player player;

    @NotNull
    @Valid
    private Customer customer;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
