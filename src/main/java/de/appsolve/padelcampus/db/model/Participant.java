/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

/**
 *
 * @author dominik
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="participantType",
    discriminatorType=DiscriminatorType.STRING
)
public abstract class Participant extends ComparableEntity implements ParticipantI{
    
    @Column
    private String UUID;
    
    @Column
    private Integer initialRanking;
    
    @Transient
    public String getDiscriminatorValue(){
    DiscriminatorValue val = this.getClass().getAnnotation( DiscriminatorValue.class );
        return val == null ? null : val.value();
    }
    
    @Override
    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public void setInitialRanking(Integer initialRanking) {
        this.initialRanking = initialRanking;
    }
    
    public Integer getInitialRanking(){
        return 1300;
    }
    
    public BigDecimal getInitialRankingAsBigDecimal(){
        return new BigDecimal(getInitialRanking());
    }
}
