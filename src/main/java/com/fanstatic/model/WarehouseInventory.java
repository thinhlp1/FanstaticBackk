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
 * The persistent class for the warehouse_inventory database table.
 */

@Table(name = "warehouse_inventory")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    @Column(name = "cancel_reason")
    private String cancelReason;

    private byte active;

    @OneToMany(mappedBy = "warehouseInventory")
    private List<WarehouseInventoryItem> warehouseInventoryItemList;

    @ManyToOne
    @JoinColumn(name = "flavor_category_id")
    private FlavorCategory flavorCategory;
    @Column(name = "to_date")
    private Date toDate;

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