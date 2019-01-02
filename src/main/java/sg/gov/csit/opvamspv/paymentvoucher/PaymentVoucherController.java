package sg.gov.csit.opvamspv.paymentvoucher;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;
import sg.gov.csit.opvamspv.station.Station;
import sg.gov.csit.opvamspv.station.StationRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    @GetMapping("/api/v1/PaymentVouchers/{paymentVoucherId}")
    public PaymentVoucher getPaymentVoucher(@PathVariable Long paymentVoucherId) {
        return paymentVoucherRepository.getOne(paymentVoucherId);
    }

    @GetMapping("/api/v1/PaymentVouchers")
    public List<PaymentVoucher> getPaymentVouchers() {
        return paymentVoucherRepository.findAll();
    }

    @PostMapping(value = "/api/v1/PaymentVouchers", consumes = MediaType.APPLICATION_XML_VALUE)
    public PaymentVoucher createPaymentVoucher(@RequestBody Ivc ivc) {
        String stationCode = parsePvHeader(ivc.header.pvFormId)[2];
        Station station = stationRepository
                .findById(stationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Station"));

        PaymentVoucher pv = extractPaymentVoucher(ivc, station);

        // TODO: Make this a transaction with the lineItems below, or consider using OneToMany mapping in PV
        paymentVoucherRepository.save(pv);

        List<Ivc.Item> items = ivc.items;
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

    private PaymentVoucher extractPaymentVoucher(Ivc ivc, Station station) {
        PaymentVoucher paymentVoucher = new PaymentVoucher();

//        paymentVoucher.setSapDocNoc(ivc.header.pvFormId);
//        paymentVoucher.setPvNumber();
        paymentVoucher.setRequestorName(ivc.basic.requestorName);
//        paymentVoucher.setPvDate(ivc.basic.pvDate);
        paymentVoucher.setPayee(ivc.basic.payee);
//        paymentVoucher.setReceiptNumber(); // receipt belongs to LineItem
        paymentVoucher.setCurrency(ivc.calculation.currency);
        paymentVoucher.setTotalTaxForeign(ivc.calculation.totalTax);
        paymentVoucher.setTotalAmountWithGst(ivc.calculation.totalAmountLocal);
        paymentVoucher.setWithholdTaxBaseAmt(ivc.calculation.withholdTaxBaseAmt);
        paymentVoucher.setPaymentMethod(ivc.basic.paymentMethod);
        paymentVoucher.setHousebank(ivc.basic.housebank);
        paymentVoucher.setDescription(ivc.basic.description);
        paymentVoucher.setDescriptionLongText(ivc.basic.mainDescription);
        paymentVoucher.setCompanyCode(ivc.basic.companyCode);
        paymentVoucher.setRate(ivc.calculation.currencyRate);
        paymentVoucher.setTaxCode(ivc.basic.taxCode);
        paymentVoucher.setStatus(PVStatus.PENDING_CHECK);
        paymentVoucher.setStation(station); // Link to Station

        return paymentVoucher;
    }

    private List<LineItem> extractLineItems(List<Ivc.Item> items, PaymentVoucher pv) {
        return items.stream().map(item -> {
            LineItem lineItem = new LineItem();

            lineItem.setAgencyService(item.agencyCode);
//            lineItem.setPrepaymentFrom(item.prepaymentPeriodFrom);
//            lineItem.setPrepaymentTo(item.prepaymentPeriodTo);
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
//            lineItem.setAmount(item.amountNoTax);
//            lineItem.setExtraChargesSgd(item.extraCharges);
//            lineItem.setGst(item);
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
