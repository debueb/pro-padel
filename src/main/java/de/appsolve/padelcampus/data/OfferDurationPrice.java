/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import de.appsolve.padelcampus.db.model.CalendarConfig;
import de.appsolve.padelcampus.db.model.Offer;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author dominik
 */
public class OfferDurationPrice implements Comparable<OfferDurationPrice> {
    
    private Offer offer;
    
    private Map<Integer, BigDecimal> durationPriceMap;
    
    private CalendarConfig config;
    
    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Map<Integer, BigDecimal> getDurationPriceMap() {
        return durationPriceMap;
    }

    public void setDurationPriceMap(Map<Integer, BigDecimal> durationPriceMap) {
        this.durationPriceMap = durationPriceMap;
    }

    public CalendarConfig getConfig() {
        return config;
    }

    public void setConfig(CalendarConfig config) {
        this.config = config;
    }

    @Override
    public int compareTo(OfferDurationPrice o) {
        if (offer==null || o==null || o.getOffer() == null){
            return -1;
        }
        return offer.getName().compareTo(o.getOffer().getName());
    }
}
