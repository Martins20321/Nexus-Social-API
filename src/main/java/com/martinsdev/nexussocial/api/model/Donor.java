package com.martinsdev.nexussocial.api.model;

import com.martinsdev.nexussocial.api.dto.InsertDonorDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonorDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_donor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Donor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;

    @ToString.Exclude
    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL)
    private List<Donation> donations = new ArrayList<>();

    public Donor(InsertDonorDTO dto) {
        this.name = dto.name();
        this.phone = dto.phone();
        this.email = dto.email();
    }

    public void updateData(UpdateDonorDTO dto) {
        this.name = dto.name();
        this.phone = dto.phone();
        this.email = dto.email();
    }
}
