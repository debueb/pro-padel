/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.mail;

import com.google.gson.Gson;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 *
 * @author dominik
 */
public class MailinResponse {
    
    private String code;
    private String message;
    private List<Object> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
    
    public boolean isSuccessful(){
        return !StringUtils.isEmpty(getCode()) && getCode().equals("success");
    }
}
