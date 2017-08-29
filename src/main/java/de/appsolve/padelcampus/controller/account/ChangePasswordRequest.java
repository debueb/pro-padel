/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.controller.account;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author dominik
 */
public class ChangePasswordRequest {

    @NotEmpty(message = "{NotEmpty.oldPass}")
    private String oldPass;

    @NotEmpty(message = "{NotEmpty.newPass}")
    private String newPass;

    @NotEmpty(message = "{NotEmpty.newPassRepeat}")
    private String newPassRepeat;

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getNewPassRepeat() {
        return newPassRepeat;
    }

    public void setNewPassRepeat(String newPassRepeat) {
        this.newPassRepeat = newPassRepeat;
    }


}
