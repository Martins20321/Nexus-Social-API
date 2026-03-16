package com.martinsdev.nexussocial.api.model;

import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import com.martinsdev.nexussocial.api.model.enums.UrgencyLevel;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_necessity")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class Necessity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Integer requiredQuantity;
    private Integer reachedQuantity;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private UrgencyLevel urgencyLevel;

    @Enumerated(EnumType.STRING)
    private NecessityStatus necessityStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    private Institution institution;
}
