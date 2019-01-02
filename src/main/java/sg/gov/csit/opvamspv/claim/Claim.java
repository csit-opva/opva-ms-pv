package sg.gov.csit.opvamspv.claim;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(unique = true) // Not specified as business requirement, but makes sense really
    private String claimDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimDesc() {
        return claimDesc;
    }

    public void setClaimDesc(String claimDesc) {
        this.claimDesc = claimDesc;
    }
}
