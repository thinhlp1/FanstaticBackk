package com.fanstatic.model;


import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * /**
 * The persistent class for the warehouse_inventory_item database table.
 */

@Table(name = "warehouse_inventory_item")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseInventoryItem {

    @Id
    private int id;

    @Column(name = "quantity_in_inventory")
    private int quantityInInventory;

    @Column(name = "quantity_in_paper")
    private int quantityInPaper;

    @Column(name = "quantity_different")
    private int quantityDifferent;

    private String reason;

    private String solution;

    private byte active;

    // bi-directional many-to-one association to Flavor
    @ManyToOne
    @JoinColumn(name = "flavor_id")
    private Flavor flavor;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private WarehouseInventory warehouseInventory;

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