package sg.gov.csit.opvamspv.paymentvoucher;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

@Entity
public class PaymentVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long sapDocNoc;

    @NotNull
    // Size rather than Length is used because Length is hibernate specific:
    // https://stackoverflow.com/questions/34588354/difference-between-size-length-and-columnlength-value-when-using-jpa-and-h
    @Size(min = 16, max = 16)
    private String pvNumber;

    // Need some length constraints
    @NotNull
    @Size(max = 40)
    private String requestorName;

    @NotNull
    private Date pvDate;

    @NotNull
    @Size(max = 16)
    private String payee;

    @NotNull
    @Size(max = 16)
    private String receiptNumber;

    @NotNull
    @Size(max = 5)
    private String currency;

    @NotNull
    private double totalTaxForeign;

    @NotNull
    private double totalAmountWithGst;

    @NotNull
    private double withholdTaxBaseAmt;

    @NotNull
    @Size(max = 1) // Why this is a length 1 String? Can we use char instead? Or is it some constant?
    private String paymentMethod;

    @NotNull
    @Size(max = 5)
    private String housebank;

    @NotNull
    @Size(max = 50)
    private String description;

    @NotNull
    @Size(max = 800)
    private String descriptionLongText;

    @NotNull
    @Size(max = 4)
    private String companyCode;

    @NotNull
    @Max(999999999)
    private double rate;

    @NotNull
    @Size(max = 2)
    private String taxCode;

    @NotNull
    private PVStatus status;
}
