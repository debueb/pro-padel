/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.data;

import java.util.List;

/**
 * @author dominik
 */
public class NestableItem {

    private Long id;
    private List<NestableItem> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<NestableItem> getChildren() {
        return children;
    }

    public void setChildren(List<NestableItem> children) {
        this.children = children;
    }
}
