package com.fanstatic.model;

import java.util.Date;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brower_token")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrowserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;
    @Column(name = "user_id")
    private Integer userId;

    private String token;

    private String browser;

    // @Temporal(TemporalType.TIMESTAMP)
    // @Column(name = "subscribe_at")
    // private Date subscribeAt;

}
