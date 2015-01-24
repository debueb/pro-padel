/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

/**
 *
 * @author dominik
 */
public class Court {
    
    private Boolean blocked;

    public Boolean getBlocked() {
        return blocked == null ? false : blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }
}
