package com.martinsdev.nexussocial.api.model;

import com.martinsdev.nexussocial.api.dto.InsertAddressDTO;
import com.martinsdev.nexussocial.api.dto.UpdateAddressDTO;
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

    public Address(InsertAddressDTO dto) {
        this.street = dto.street();
        this.number = dto.number();
        this.neighborhood = dto.neighborhood();
        this.city = dto.city();
        this.state = dto.state();
    }

    public void updateData(UpdateAddressDTO dto) {
        if (dto.street() != null) {
            this.street = dto.street();
        }
        if (dto.number() != null) {
            this.number = dto.number();
        }
        if (dto.neighborhood() != null) {
            this.neighborhood = dto.neighborhood();
        }
        if (dto.city() != null) {
            this.city = dto.city();
        }
        if (dto.state() != null) {
            this.state = dto.state();
        }
    }
}
