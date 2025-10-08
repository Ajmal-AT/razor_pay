package com.sample.codes.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "payment")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "amount")
    private int amount;

    @Column(name = "key_id")
    private String keyId;

    @Column(name = "form_entity_id")
    private Long formEntityId;

}

