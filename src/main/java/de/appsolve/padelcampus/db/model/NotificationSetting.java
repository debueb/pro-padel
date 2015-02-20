/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.SkillLevel;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author dominik
 */
@Entity
public class NotificationSetting extends BaseEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @OneToOne(fetch = FetchType.EAGER)
    private Player player;
    
    @Column
    private Boolean enabled;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @NotEmpty(message = "{NotEmpty.skillLevels}")
    @Enumerated(EnumType.STRING)
    private Set<SkillLevel> skillLevels;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Boolean getEnabled() {
        return enabled == null ? false : enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<SkillLevel> getSkillLevels() {
        return skillLevels;
    }

    public void setSkillLevels(Set<SkillLevel> skillLevels) {
        this.skillLevels = skillLevels;
    }
}
