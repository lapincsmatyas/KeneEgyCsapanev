package com.example.cb.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.example.cb.payload.CommentPayload;

@Entity
@Table(name = "caffs")
public class CAFF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String tags;

    @OneToMany(mappedBy = "caff")
    private List<Comment> comments;

    public CAFF(){

    }

    public CAFF(String name, String type, byte[] data, byte[] imgdata, String imguri, List<Comment> comments) {
        this.name = name;
        this.tags = tags;
        this.comments = comments;
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
}
