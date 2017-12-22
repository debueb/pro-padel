package de.appsolve.padelcampus.controller.events;

import de.appsolve.padelcampus.constants.PaymentMethod;
import de.appsolve.padelcampus.db.model.Community;
import de.appsolve.padelcampus.db.model.Player;

import java.util.List;
import java.util.Set;

public class EventBookingRequest {

    /*
       single partner, e.g. for all event types but PullRoundRobin and CommunityRoundRobin
    */
    private Player player;

    /*
       set of existing players, e.g. for CommunityRoundRobin
    */
    private Set<Player> players;

    /*
       set of new players, e.g. for CommunityRoundRobin
    */
    private List<Player> newPlayers;

    private PaymentMethod paymentMethod;

    /*
        for CommunityRoundRobin
     */
    private Community community;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public List<Player> getNewPlayers() {
        return newPlayers;
    }

    public void setNewPlayers(List<Player> newPlayers) {
        this.newPlayers = newPlayers;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }
}
