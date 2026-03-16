package com.martinsdev.nexussocial.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "tb_address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
}
