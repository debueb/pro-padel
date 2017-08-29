/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.appsolve.padelcampus.db.model;

import de.appsolve.padelcampus.constants.ImageCategory;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

/**
 * @author dominik
 */
@Entity
public class Image extends CustomerEntity {

    private static final long serialVersionUID = 1L;

    private String filePath;

    private String sha256;

    private Integer width;

    private Integer height;

    private String contentType;

    private Long contentLength;

    @Enumerated(EnumType.STRING)
    private ImageCategory category;

    @Lob
    private byte[] content;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public ImageCategory getCategory() {
        return category;
    }

    public void setCategory(ImageCategory category) {
        this.category = category;
    }
}
