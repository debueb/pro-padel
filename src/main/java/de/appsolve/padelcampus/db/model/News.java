/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

/**
 *
 * @author dominik
 */
@Entity
public class News extends BaseEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    private LocalDate newsDate;
    
    @NotEmpty(message = "{NotEmpty.title}")
    @Column
    private String title;
    
    @NotEmpty(message = "{NotEmpty.message}")
    @Column(length=21584)
    private String message;
    
    @Column
    private Boolean showOnHomepage;

    public LocalDate getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(LocalDate newsDte) {
        this.newsDate = newsDte;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getShowOnHomepage() {
        return showOnHomepage == null ? Boolean.FALSE : showOnHomepage;
    }

    public void setShowOnHomepage(Boolean showOnHomepage) {
        this.showOnHomepage = showOnHomepage;
    }
    
    @Override
    public String toString(){
        return title;
    }
    
    @Override
    public int compareTo(BaseEntityI o) {
        if (o instanceof News){
            News otherNews = (News) o;
            return otherNews.getNewsDate().compareTo(getNewsDate());
        }
        return super.compareTo(o);
    }
}
