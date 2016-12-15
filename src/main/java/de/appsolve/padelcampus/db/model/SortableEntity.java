/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author dominik
 */
@MappedSuperclass
public abstract class SortableEntity extends ComparableEntity{
    
    @Column
    private Long position;

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }
    
    @Override
    public int compareTo(BaseEntityI o){
        if (o instanceof SortableEntity){
            SortableEntity other = (SortableEntity) o;
            if (getPosition() != null && other.getPosition() != null){
                return getPosition().compareTo(other.getPosition());
            }
            if (getPosition() == null && other.getPosition() == null){
                return 0;
            }
            if (getPosition() == null){
                return 1;
            }
            if (other.getPosition() == null){
                return -1;
            }
        }
        return super.compareTo(o);
    }
}
