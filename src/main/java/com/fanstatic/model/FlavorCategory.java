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
 * The persistent class for the flavor database table.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flavor_category")
public class FlavorCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private byte active;

    private String code;

    private String name;

    //bi-directional many-to-one association to WarehouseReceiveItem
    @OneToMany(mappedBy = "flavorCategory")
    private List<Flavor> flavor;

    @OneToMany(mappedBy = "flavorCategory")
    private List<WarehouseInventory> warehouseInventoryList;


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

}