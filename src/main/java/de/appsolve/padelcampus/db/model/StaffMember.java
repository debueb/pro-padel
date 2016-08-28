/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.appsolve.padelcampus.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author dominik
 */
@Entity
public class StaffMember extends SortableEntity{
    
    @Transient
    private static final long serialVersionUID = 1L;
    
    @Column
    @NotEmpty(message = "{NotEmpty.name}")
    private String name;
    
    @Column
    @NotEmpty(message = "{NotEmpty.teaser}")
    @Length(max = 50, message = "{Length.StaffMember.teaser}")
    private String teaser;
    
    @Column
    @NotEmpty(message = "{NotEmpty.description}")
    @Length(max = 255, message = "{Length.StaffMember.description")
    private String description;
    
    @OneToOne
    private Image profileImage;
    
    @Transient
    private MultipartFile profileImageFile;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public MultipartFile getProfileImageFile() {
        return profileImageFile;
    }

    public void setProfileImageFile(MultipartFile profileImageFile) {
        this.profileImageFile = profileImageFile;
    }

    @Override
    public String toString() {
        return getName();
    }
}