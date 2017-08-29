package de.appsolve.padelcampus.db.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @author dominik
 */
@Entity
public class Subscription extends CustomerEntity {

    @Column
    private String name;

    @Column
    private BigDecimal price;

    @Column
    private Integer maxMinutesPerDay;

    @Column
    private Integer maxMinutesPerWeek;

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("firstName, lastName")
    private Set<Player> players;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMaxMinutesPerDay() {
        return maxMinutesPerDay;
    }

    public void setMaxMinutesPerDay(Integer maxMinutesPerDay) {
        this.maxMinutesPerDay = maxMinutesPerDay;
    }

    public Integer getMaxMinutesPerWeek() {
        return maxMinutesPerWeek;
    }

    public void setMaxMinutesPerWeek(Integer maxMinutesPerWeek) {
        this.maxMinutesPerWeek = maxMinutesPerWeek;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        return getName();
    }
}