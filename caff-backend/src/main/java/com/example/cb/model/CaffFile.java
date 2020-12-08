package com.example.cb.model;

import javax.persistence.*;

@Entity
@Table(name = "caff_files")
public class CaffFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "caff_file_id")
    private int id;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] data;

    @OneToOne(mappedBy = "file")
    private Caff caff;

    public int getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Caff getCaff() {
        return caff;
    }

    public void setCaff(Caff caff) {
        this.caff = caff;
    }

}
