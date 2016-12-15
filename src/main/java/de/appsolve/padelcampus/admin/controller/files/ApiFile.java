/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.admin.controller.files;

import java.util.Objects;

/**
 *
 * @author dominik
 */
public class ApiFile implements Comparable<ApiFile>{
    
    private String type;
    private String name;
    private String url;
    private Long fileSize;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public int compareTo(ApiFile o) {
        if (getType().equals(o.getType())){
            switch (o.getType()){
                case "file":
                    return getFileSize().compareTo(o.getFileSize());
                default:
                    return getName().compareToIgnoreCase(o.getName());
            }
        }
        return getType().equals("folder") ? -1 : 1;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.type);
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.url);
        hash = 97 * hash + Objects.hashCode(this.fileSize);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ApiFile other = (ApiFile) obj;
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        return Objects.equals(this.fileSize, other.fileSize);
    }
}
