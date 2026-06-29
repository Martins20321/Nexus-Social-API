package com.martinsdev.nexussocial.api.model;

import com.martinsdev.nexussocial.api.model.embedded.Address;
import com.martinsdev.nexussocial.api.model.enums.AreaOfActivity;
import com.martinsdev.nexussocial.api.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_institution")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Institution implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cnpj;

    @Enumerated(EnumType.STRING)
    private AreaOfActivity areaOfActivity;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
    private boolean enabled;

    @Embedded
    private Address address;

    @ToString.Exclude //Evitando loop infinito
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<Necessity> necessities;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
