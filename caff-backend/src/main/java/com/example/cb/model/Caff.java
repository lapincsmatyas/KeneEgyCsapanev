package com.example.cb.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.example.cb.payload.CommentPayload;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "caffs")
public class Caff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String tags;

    @OneToMany(mappedBy = "caff")
    private List<Comment> comments;

    @OneToOne()
    @JoinColumn(name = "file_id")
    @JsonIgnore
    private CaffFile file;

    @Lob
    private byte[] previewFile;

    private String fileMd5;

    private int width;
    private int height;

    public Caff() {
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public CaffFile getFile() {
        return file;
    }

    public void setFile(CaffFile file) {
        this.file = file;
    }

    public String[] getTags() {
        return tags.split("$");
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public byte[] getPreviewFile() {
        return previewFile;
    }

    public void setPreviewFile(byte[] previewFile) {
        this.previewFile = previewFile;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }
}
