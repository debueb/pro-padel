package de.appsolve.padelcampus.db.model;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class EmailConfirmation extends BaseEntity {

    @Column
    private String uuid;

    @Column
    private String email;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
