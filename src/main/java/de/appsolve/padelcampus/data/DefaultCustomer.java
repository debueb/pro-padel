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
public class DefaultCustomer implements CustomerI{

    @Override
    public String getName() {
        return "defaultCustomer";
    }
    
    @Override
    public String toString(){
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DefaultCustomer){
            return true;
        }
        return super.equals(obj); 
    }

    @Override
    public int hashCode() {
        return 1;
    }
    
    
    
}
