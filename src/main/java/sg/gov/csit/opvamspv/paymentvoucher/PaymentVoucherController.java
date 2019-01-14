package sg.gov.csit.opvamspv.paymentvoucher;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;
import sg.gov.csit.opvamspv.station.Station;
import sg.gov.csit.opvamspv.station.StationRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.bind.JAXB;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PaymentVoucherController {

    private final PaymentVoucherRepository paymentVoucherRepository;
    private final StationRepository stationRepository;
    private final LineItemRepository lineItemRepository;

    public PaymentVoucherController(PaymentVoucherRepository paymentVoucherRepository, StationRepository stationRepository, LineItemRepository lineItemRepository) {
        this.paymentVoucherRepository = paymentVoucherRepository;
        this.stationRepository = stationRepository;
        this.lineItemRepository = lineItemRepository;
    }

    @GetMapping("/api/v1/Checking/PaymentVouchers")
    public List<PaymentVoucher> getCheckingPaymentVouchers(@RequestAttribute String pfNo) {
        return paymentVoucherRepository.findCheckingPaymentVouchers(pfNo);
    }

    @GetMapping("/api/v1/Approving/PaymentVouchers")
    public List<PaymentVoucher> getApprovingPaymentVouchers(@RequestAttribute String pfNo) {
        return paymentVoucherRepository.findApprovingPaymentVouchers(pfNo);
    }

    @GetMapping("/api/v1/Supporting/PaymentVouchers")
    public List<PaymentVoucher> getSupportingPaymentVouchers(@RequestAttribute String pfNo) {
        return paymentVoucherRepository.findSupportingPaymentVouchers(pfNo);
    }

    @GetMapping("/api/v1/PaymentVouchers/{paymentVoucherId}")
    public PaymentVoucher getPaymentVoucher(@PathVariable Long paymentVoucherId) {
        return paymentVoucherRepository.getOne(paymentVoucherId);
    }

    @GetMapping("/api/v1/PaymentVouchers")
    public List<PaymentVoucher> getPaymentVouchers() {
        return paymentVoucherRepository.findAll();
    }

    @PostMapping(value = "/api/v1/PaymentVouchers")
    public PaymentVoucher createPaymentVoucher(
            @RequestParam("claimForm") MultipartFile claimForm,
            @RequestParam("sapResult") MultipartFile sapResult,
            @RequestParam("receipts") MultipartFile[] receipts
    ) {
        XmlClaimForm xmlClaimForm = getXmlClaimForm(claimForm);
        String[] pvHeaders = parsePvHeader(xmlClaimForm.header.pvFormId);
        String stationCode = pvHeaders[2];
        String pvFormNo = String.join("", pvHeaders);
        String docNoStr = "";

        // TODO: Extract to method
        try (BufferedReader br = new BufferedReader(new InputStreamReader(sapResult.getInputStream()))) {
            String line;
            br.readLine(); // Skip headers
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                String formNo = tokens[1]; // the PV Form No. is the second entry
                if (formNo.equalsIgnoreCase(pvFormNo)) {
                    docNoStr = tokens[2]; // The Doc. Num is the third entry
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Station station = stationRepository
                .findById(stationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Station"));

        PaymentVoucher pv = extractPaymentVoucher(xmlClaimForm, station, pvFormNo, docNoStr);

        // TODO: Make this a transaction with the lineItems below, or consider using OneToMany mapping in PV
        paymentVoucherRepository.save(pv);

        List<XmlClaimForm.Item> items = xmlClaimForm.items;
        List<LineItem> lineItems = extractLineItems(items, pv);
        lineItemRepository.saveAll(lineItems);

        return pv;
    }

    @PostMapping("/api/v1/PaymentVouchers/{paymentVoucherId}/check")
    public PaymentVoucher setPVChecked(@PathVariable Long paymentVoucherId) {
        PaymentVoucher pv = paymentVoucherRepository
                .findById(paymentVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        pv.setStatus(PVStatus.PENDING_SUPPORTING);
        return paymentVoucherRepository.save(pv);
    }

    @PostMapping("/api/v1/PaymentVouchers/{paymentVoucherId}/support")
    public String supportPV(@PathVariable Long paymentVoucherId) {
        throw new NotImplementedException();
    }

    @PostMapping("/api/v1/PaymentVouchers/{paymentVoucherId}/approve")
    public PaymentVoucher approvePV(@PathVariable Long paymentVoucherId) {
        PaymentVoucher pv = paymentVoucherRepository
                .findById(paymentVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        pv.setStatus(PVStatus.PENDING_PAYMENT);
        return paymentVoucherRepository.save(pv);
    }

    @PostMapping("/api/v1/PaymentVouchers/{paymentVoucherId}/reject")
    public PaymentVoucher rejectPV(@PathVariable Long paymentVoucherId) {
        PaymentVoucher pv = paymentVoucherRepository
                .findById(paymentVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        pv.setStatus(PVStatus.REJECTED);
        return paymentVoucherRepository.save(pv);
    }

    // TODO: Consider a struct/simple class for this rather than String[]
    private String[] parsePvHeader(String header) {
        return header.split("-");
    }

    private PaymentVoucher extractPaymentVoucher(XmlClaimForm xmlClaimForm, Station station, String pvNumber, String docNoStr) {
        long docNo = Long.parseLong(docNoStr);
        PaymentVoucher paymentVoucher = new PaymentVoucher();

        paymentVoucher.setSapDocNoc(docNo);
        paymentVoucher.setPvNumber(pvNumber);
        paymentVoucher.setRequestorName(xmlClaimForm.basic.requestorName);
        paymentVoucher.setPvDate(xmlClaimForm.basic.pvDate);
        paymentVoucher.setPayee(xmlClaimForm.basic.payee);
        paymentVoucher.setReferenceNumber(xmlClaimForm.basic.referenceNumber);
        paymentVoucher.setCurrency(xmlClaimForm.calculation.currency);
        paymentVoucher.setTotalTaxForeign(xmlClaimForm.calculation.totalTax);
        paymentVoucher.setTotalAmountWithGst(xmlClaimForm.calculation.totalAmountLocal);
        paymentVoucher.setWithholdTaxBaseAmt(xmlClaimForm.calculation.withholdTaxBaseAmt);
        paymentVoucher.setPaymentMethod(xmlClaimForm.basic.paymentMethod);
        paymentVoucher.setHousebank(xmlClaimForm.basic.housebank);
        paymentVoucher.setDescription(xmlClaimForm.basic.description);
        paymentVoucher.setDescriptionLongText(xmlClaimForm.basic.mainDescription);
        paymentVoucher.setCompanyCode(xmlClaimForm.basic.companyCode);
        paymentVoucher.setRate(xmlClaimForm.calculation.currencyRate);
        paymentVoucher.setTaxCode(xmlClaimForm.basic.taxCode);
        paymentVoucher.setStatus(PVStatus.PENDING_CHECK);
        paymentVoucher.setStation(station); // Link to Station

        return paymentVoucher;
    }

    private XmlClaimForm getXmlClaimForm(MultipartFile claimForm) {
        File file = null;
        try {
            file = File.createTempFile("something", ".xml");
            claimForm.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        XmlClaimForm xmlClaimForm = JAXB.unmarshal(file, XmlClaimForm.class); // NullPointerException?
        return xmlClaimForm;
    }

    private List<LineItem> extractLineItems(List<XmlClaimForm.Item> items, PaymentVoucher pv) {
        return items.stream().map(item -> {
            LineItem lineItem = new LineItem();

            lineItem.setAgencyService(item.agencyCode);
            lineItem.setPrepaymentFrom(item.prepaymentPeriodFrom);
            lineItem.setPrepaymentTo(item.prepaymentPeriodTo);
            lineItem.setFundCentre(item.fundCentre);
            lineItem.setCostCentre(item.costCentre);
            lineItem.setCountry(item.country);
            lineItem.setFundNo(item.fundNumber);
            lineItem.setFixedAssetIndicator(item.fixedAssetIndicator);
            lineItem.setTransactionType(item.transactionType);
            lineItem.setGlAccount(item.glAccount);
            lineItem.setFixedAssetNumber(item.fixedAssetNo);
            lineItem.setFixedAssetQty(item.unitsOfFixedAsset);
            lineItem.setReceiptNo(item.receiptNo);
            lineItem.setReceiptDate(item.receiptDate);
            lineItem.setAmount(item.amountNoTax);
            lineItem.setExtraChargesSgd(item.extraCharges);
            lineItem.setTaxCode(item.taxCode);
            lineItem.setAmountWithGst(item.amountWithTax);
            lineItem.setAssignmentNumber(item.assignment);
            lineItem.setProject(item.project);
            lineItem.setDetailDescription(item.desc);
            lineItem.setPaymentVoucher(pv); // Link to PV

            return lineItem;
        }).collect(Collectors.toList());
    }

}
