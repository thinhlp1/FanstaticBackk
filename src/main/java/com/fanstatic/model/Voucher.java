package com.fanstatic.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * /**
 * The persistent class for the voucher database table.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_at")
    private Date endAt;

    private String name;

    private int percent;

    // thịnh thêm thuộc tính quantity
    private Integer quantity;

    private byte active;

    @Column(name = "price_condition")
    private Long priceCondition;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_at")
    private Date startAt;

    private Long value;

    @Column(name = "voucher_code")
    private String voucherCode;

    // bi-directional many-to-one association to OrderVoucher
    @JsonIgnore
    @OneToMany(mappedBy = "voucher")
    private List<OrderVoucher> orderVouchers;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_at")
    private Date updateAt;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "update_by")
    private User updateBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "create_by")
    private User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "delete_at")
    private Date deleteAt;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "delete_by")
    private User deleteBy;

}