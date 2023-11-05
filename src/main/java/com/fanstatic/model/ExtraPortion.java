package com.fanstatic.model;


import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * /**
 * The persistent class for the extra_portion database table.
 */

@Table(name = "extra_portion")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExtraPortion {

    @Id
    @Column(name = "extra_portion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int extraPortionId;

    private byte active;

    private String code;

    private String name;

    private int price;

    private String type;

    //bi-directional many-to-one association to Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    //bi-directional many-to-one association to File
    @ManyToOne
    @JoinColumn(name = "image_id")
    private File imageFile;

    //bi-directional many-to-one association to OrderExtraPortion
    @OneToMany(mappedBy = "extraPortion")
    private List<OrderExtraPortion> orderExtraPortions;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_at")
    private Date updateAt;

    @OneToOne
    @JoinColumn(name = "update_by")
    private User updateBy;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;

    @OneToOne
    @JoinColumn(name = "create_by")
    private User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delete_at")
    private Date deleteAt;

    @OneToOne
    @JoinColumn(name = "delete_by")
    private User deleteBy;

    @Override
    public String toString() {
        return "ExtraPortion{" +
                "extraPortionId=" + extraPortionId +
                ", active=" + active +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", category=" + category +
                ", image=" + imageFile +
                '}';
    }
}