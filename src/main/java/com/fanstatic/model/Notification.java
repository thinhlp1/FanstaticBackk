package com.fanstatic.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

public class Notification {
    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private User receiver;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "send_at")
    private Date sendAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "seen_at")
    private Date SeenAt;
}
