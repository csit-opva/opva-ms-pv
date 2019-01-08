package sg.gov.csit.opvamspv.officer;

import sg.gov.csit.opvamspv.claim.Claim;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class SupportingOfficer extends Officer {
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Claim> claims;

    public Set<Claim> getClaims() {
        return claims;
    }

    public void setClaims(Set<Claim> claims) {
        this.claims = claims;
    }
}
