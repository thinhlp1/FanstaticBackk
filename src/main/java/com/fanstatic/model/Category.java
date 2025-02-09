package com.fanstatic.model;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * /**
 * The persistent class for the category database table.
 */
@Entity
@Table(name = "category")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private byte active;

    private String code;

    private int level;

    private String name;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private File image;

    // bi-directional many-to-one association to Category
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    // bi-directional many-to-one association to Category
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> categories;

    // bi-directional many-to-one association to ComboProduct
    @OneToMany(mappedBy = "category")
    private List<ComboProduct> comboProducts;

    // bi-directional many-to-one association to ExtraPortion
    @OneToMany(mappedBy = "category")
    private List<ExtraPortion> extraPortions;

    // bi-directional many-to-one association to Product
    @OneToMany(mappedBy = "category")
    private List<ProductCategory> productCategories;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_at")
    private Date updateAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_by")
    private User updateBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delete_at")
    private Date deleteAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delete_by")
    private User deleteBy;
}