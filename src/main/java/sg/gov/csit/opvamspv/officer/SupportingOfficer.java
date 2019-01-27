package sg.gov.csit.opvamspv.officer;

import sg.gov.csit.opvamspv.claim.Claim;

import javax.persistence.*;
import java.util.Set;

@Entity
public class SupportingOfficer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Officer officer;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Claim> claims;

    public Set<Claim> getClaims() {
        return claims;
    }

    public void setClaims(Set<Claim> claims) {
        this.claims = claims;
    }

    public Officer getOfficer() {
        return officer;
    }

    public void setOfficer(Officer officer) {
        this.officer = officer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
