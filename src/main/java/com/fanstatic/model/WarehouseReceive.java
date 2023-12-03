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
 * The persistent class for the warehouse_receive database table.
 */

@Table(name = "warehouse_receive")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseReceive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private byte active;

    private String description;

    private String code;

    @Column(name = "check_out_by")
    private String checkOutBy;

    @Column(name = "cancel_reason")
    private String cancelReason;

    // bi-directional many-to-one association to File
    @ManyToOne
    @JoinColumn(name = "image_id")
    private File imageFile;

    // bi-directional many-to-one association to Supplier
    @ManyToOne
    private Supplier supplier;

    // bi-directional many-to-one association to WarehouseReceiveItem
    @OneToMany(mappedBy = "warehouseReceive")
    private List<WarehouseReceiveItem> warehouseReceiveItemList;

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
        return "WarehouseReceive{" +
                "id=" + id +
                ", active=" + active +
                ", description='" + description + '\'' +
                ", imageFile=" + imageFile +
                ", supplier=" + supplier +
                ", warehouseReceiveItemList=" + warehouseReceiveItemList +
                ", updateAt=" + updateAt +
                ", updateBy=" + updateBy +
                ", createAt=" + createAt +
                ", createBy=" + createBy +
                ", deleteAt=" + deleteAt +
                ", deleteBy=" + deleteBy +
                '}';
    }
}