package sg.gov.csit.opvamspv.lineitem;

import sg.gov.csit.opvamspv.paymentvoucher.PaymentVoucher;

import java.math.BigDecimal;
import java.util.List;

public class LineItemDTO {
    private Long id;
    private int lineItemNo;
    private String agencyService;
    private String prepaymentFrom;
    private String prepaymentTo;
    private String fundCentre;
    private String costCentre;
    private String country;
    private String fundNo;
    private char fixedAssetIndicator;
    private String transactionType;
    private String glAccount;
    private String fixedAssetNumber;
    private String fixedAssetQty;
    private String receiptNo;
    private String receiptDate;
    private BigDecimal amount;
    private BigDecimal extraChargesSgd;
    private String taxCode;
    private double amountWithGst;
    private String assignmentNumber;
    private String project;
    private String detailDescription;
    private PaymentVoucher paymentVoucher;
    private List<Long> receipts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLineItemNo() {
        return lineItemNo;
    }

    public void setLineItemNo(int lineItemNo) {
        this.lineItemNo = lineItemNo;
    }

    public String getAgencyService() {
        return agencyService;
    }

    public void setAgencyService(String agencyService) {
        this.agencyService = agencyService;
    }

    public String getPrepaymentFrom() {
        return prepaymentFrom;
    }

    public void setPrepaymentFrom(String prepaymentFrom) {
        this.prepaymentFrom = prepaymentFrom;
    }

    public String getPrepaymentTo() {
        return prepaymentTo;
    }

    public void setPrepaymentTo(String prepaymentTo) {
        this.prepaymentTo = prepaymentTo;
    }

    public String getFundCentre() {
        return fundCentre;
    }

    public void setFundCentre(String fundCentre) {
        this.fundCentre = fundCentre;
    }

    public String getCostCentre() {
        return costCentre;
    }

    public void setCostCentre(String costCentre) {
        this.costCentre = costCentre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFundNo() {
        return fundNo;
    }

    public void setFundNo(String fundNo) {
        this.fundNo = fundNo;
    }

    public char getFixedAssetIndicator() {
        return fixedAssetIndicator;
    }

    public void setFixedAssetIndicator(char fixedAssetIndicator) {
        this.fixedAssetIndicator = fixedAssetIndicator;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public String getFixedAssetNumber() {
        return fixedAssetNumber;
    }

    public void setFixedAssetNumber(String fixedAssetNumber) {
        this.fixedAssetNumber = fixedAssetNumber;
    }

    public String getFixedAssetQty() {
        return fixedAssetQty;
    }

    public void setFixedAssetQty(String fixedAssetQty) {
        this.fixedAssetQty = fixedAssetQty;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExtraChargesSgd() {
        return extraChargesSgd;
    }

    public void setExtraChargesSgd(BigDecimal extraChargesSgd) {
        this.extraChargesSgd = extraChargesSgd;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public double getAmountWithGst() {
        return amountWithGst;
    }

    public void setAmountWithGst(double amountWithGst) {
        this.amountWithGst = amountWithGst;
    }

    public String getAssignmentNumber() {
        return assignmentNumber;
    }

    public void setAssignmentNumber(String assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public PaymentVoucher getPaymentVoucher() {
        return paymentVoucher;
    }

    public void setPaymentVoucher(PaymentVoucher paymentVoucher) {
        this.paymentVoucher = paymentVoucher;
    }

    public List<Long> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Long> receipts) {
        this.receipts = receipts;
    }
}
