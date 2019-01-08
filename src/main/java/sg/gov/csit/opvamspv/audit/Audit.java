package sg.gov.csit.opvamspv.audit;

import sg.gov.csit.opvamspv.paymentvoucher.PaymentVoucher;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String findings;

    private String settlementReport;

    @OneToOne(optional = false)
    private PaymentVoucher paymentVoucher;

    @NotNull
    private AuditStatus auditStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFindings() {
        return findings;
    }

    public void setFindings(String findings) {
        this.findings = findings;
    }

    public String getSettlementReport() {
        return settlementReport;
    }

    public void setSettlementReport(String settlementReport) {
        this.settlementReport = settlementReport;
    }

    public AuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }

    public PaymentVoucher getPaymentVoucher() {
        return paymentVoucher;
    }

    public void setPaymentVoucher(PaymentVoucher paymentVoucher) {
        this.paymentVoucher = paymentVoucher;
    }
}
