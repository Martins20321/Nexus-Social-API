package com.martinsdev.nexussocial.api.model;

import com.martinsdev.nexussocial.api.dto.InsertInstitutionDTO;
import com.martinsdev.nexussocial.api.dto.UpdateInstitutionDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_institution")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Institution implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cnpj;
    private String phone;
    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @ToString.Exclude //Evitando loop infinito
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<Necessity> necessities = new ArrayList<>();

    public Institution(InsertInstitutionDTO dto) {
        this.name = dto.name();
        this.cnpj = dto.cnpj();
        this.phone = dto.phone();
        this.email = dto.email();
    }

    public void updateData(UpdateInstitutionDTO dto){
        this.name = dto.name();
        this.phone = dto.phone();
    }
}
