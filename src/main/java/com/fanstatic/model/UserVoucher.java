package com.fanstatic.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user_voucher")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "collect_at")
    private Date collectAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // bi-directional many-to-one association to Product
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "use_at")
    private Date useAt;
}
