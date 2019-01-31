package sg.gov.csit.opvamspv.paymentvoucher;

import sg.gov.csit.opvamspv.officer.Officer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class PvRejection {
    @Id
    private Long id;

    @OneToOne
    @NotNull
    private PaymentVoucher pv;

    @OneToOne
    @NotNull
    private Officer officer;

    @NotBlank
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentVoucher getPv() {
        return pv;
    }

    public void setPv(PaymentVoucher pv) {
        this.pv = pv;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Officer getOfficer() {
        return officer;
    }

    public void setOfficer(Officer officer) {
        this.officer = officer;
    }
}
