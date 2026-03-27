package com.martinsdev.nexussocial.api.model;

import com.martinsdev.nexussocial.api.dto.InsertDonationDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonationDTO;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_donation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString
public class Donation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer donatedQuantity;
    private LocalDateTime moment = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "necessity_id")
    private Necessity necessity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private Donor donor;

    public Donation(InsertDonationDTO dto, Donor donor, Necessity necessity) {
        this.donatedQuantity = dto.donatedQuantity();
        this.donor = donor;
        this.necessity = necessity;
    }

    public void updateData(UpdateDonationDTO dto) {
        if(dto.donatedQuantity() != null){
            this.donatedQuantity = dto.donatedQuantity();
        }
    }
}
