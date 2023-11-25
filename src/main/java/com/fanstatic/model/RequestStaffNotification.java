package com.fanstatic.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "request_staff_notification")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestStaffNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    @JoinColumn(name = "customer_id")
    @ManyToOne
    private User customer;

    @JoinColumn(name = "emmployee_confirm")
    @ManyToOne
    private User employeeConfirm;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "confirm_at")
    private Date confirmAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deny_at")
    private Date denyAt;
}
