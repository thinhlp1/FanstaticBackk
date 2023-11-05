package com.fanstatic.model;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * /**
 * The persistent class for the file database table.
 */

@Entity
@Table(name = "file")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File implements Serializable {

    public final static String FILE_TYPE_IMAGE = "IMAGE";
    public final static String FILE_TYPE_QR = "QR";
    public final static String FILE_TYPE_TABLE_LAYOUT = "TABLE_LAYOUT";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private byte active;

    private String extension;

    @Column(name = "link_in_project")
    private String link;

    private String name;

    private long size;

    @Column(name = "file_type")
    private String fileType;

    @OneToMany(mappedBy = "imageFile")
    private List<ExtraPortion> extraPortions;

    public File(byte active, String extension, String link, String name, long size, String fileType) {
        this.active = active;
        this.extension = extension;
        this.link = link;
        this.name = name;
        this.size = size;
        this.fileType = fileType;
    }

}