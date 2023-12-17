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
 * The persistent class for the warehouse_deliver database table.
 */

@Table(name = "warehouse_deliver")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDeliver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    private byte active;

    @Column(name = "cancel_reason")
    private String cancelReason;

    // bi-directional many-to-one association to WarehouseDeliverItem
    @OneToMany(mappedBy = "warehouseDeliver")
    private List<WarehouseDeliverItem> warehouseDeliverItems;

    @ManyToOne
    @JoinColumn(name = "reason_id")
    private WarehouseDeliverReason warehouseDeliverReason;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private WarehouseDeliverSolution warehouseDeliverSolution;

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