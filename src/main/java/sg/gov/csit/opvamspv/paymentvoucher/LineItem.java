package sg.gov.csit.opvamspv.paymentvoucher;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
public class LineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private int lineItemNo;

    @Size(max = 20)
    private String agencyService;

    @NotNull
    private Date prepaymentFrom;

    @NotNull
    private Date prepaymentTo;

    @NotNull
    @Size(min = 10, max = 10)
    private String fundCentre;

    @NotNull
    @Size(min = 10, max = 10)
    private String costCentre;

    @NotNull
    @Size(max = 3)
    private String country;

    @NotNull
    @Column(length = 30)
    private String fundNo;

    @NotNull
    private char fixedAssetIndicator;

    @Size(max = 3)
    private String transactionType;

    @Column(length = 10)
    private String glAccount;

    @Column(length = 12)
    private String fixedAssetNumber;

    @Column(length = 12)
    private String fixedAssetQty;

    @NotNull
    @Size(max = 16)
    private String receiptNo;

    @NotNull
    private Date receiptDate;

    private BigDecimal amount;

    private BigDecimal extraChargesSgd;

    private double gst;

    @NotNull
    @Size(max = 2)
    private String taxCode;

    private double amountWithGst;

    @NotNull
    @Size(max = 8)
    private String assignmentNumber;

    @NotNull
    @Size(max = 12)
    private String project;

    @NotNull
    @Size(max = 800)
    private String detailDescription;

    // This is the mapping to a PaymentVoucher
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private PaymentVoucher paymentVoucher;
}
