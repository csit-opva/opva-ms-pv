package sg.gov.csit.opvamspv.paymentvoucher;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sg.gov.csit.opvamspv.station.Station;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PaymentVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long sapDocNoc;

    @NotNull
    // Size rather than Length is used because Length is hibernate specific:
    // https://stackoverflow.com/questions/34588354/difference-between-size-length-and-columnlength-value-when-using-jpa-and-h
//    @Size(min = 16, max = 16)
    private String pvNumber;

    // Need some length constraints
    @NotNull
    @Size(max = 40)
    private String requestorName;

    @NotNull
    private String pvDate;

    @NotNull
    @Size(max = 16)
    private String payee;

    @NotNull
    @Size(max = 16)
    private String referenceNumber;

    @NotNull
    @Size(max = 5)
    private String currency;

    @NotNull
    private BigDecimal totalTaxForeign;

    private BigDecimal totalAmountWithGst;

    private BigDecimal withholdTaxBaseAmt;

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

    private BigDecimal rate;

    @NotNull
    @Size(max = 2)
    private String taxCode;

    @NotNull
    private PVStatus status;

    @NotNull
    @ManyToOne
    private Station station;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSapDocNoc() {
        return sapDocNoc;
    }

    public void setSapDocNoc(Long sapDocNoc) {
        this.sapDocNoc = sapDocNoc;
    }

    public String getPvNumber() {
        return pvNumber;
    }

    public void setPvNumber(String pvNumber) {
        this.pvNumber = pvNumber;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getPvDate() {
        return pvDate;
    }

    public void setPvDate(String pvDate) {
        this.pvDate = pvDate;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalTaxForeign() {
        return totalTaxForeign;
    }

    public void setTotalTaxForeign(BigDecimal totalTaxForeign) {
        this.totalTaxForeign = totalTaxForeign;
    }

    public BigDecimal getTotalAmountWithGst() {
        return totalAmountWithGst;
    }

    public void setTotalAmountWithGst(BigDecimal totalAmountWithGst) {
        this.totalAmountWithGst = totalAmountWithGst;
    }

    public BigDecimal getWithholdTaxBaseAmt() {
        return withholdTaxBaseAmt;
    }

    public void setWithholdTaxBaseAmt(BigDecimal withholdTaxBaseAmt) {
        this.withholdTaxBaseAmt = withholdTaxBaseAmt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getHousebank() {
        return housebank;
    }

    public void setHousebank(String housebank) {
        this.housebank = housebank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionLongText() {
        return descriptionLongText;
    }

    public void setDescriptionLongText(String descriptionLongText) {
        this.descriptionLongText = descriptionLongText;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public PVStatus getStatus() {
        return status;
    }

    public void setStatus(PVStatus status) {
        this.status = status;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
