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
                .orElseThrow(() -> new ResourceNotFoundException("alamak"));

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

        paymentVoucher.setStation(station);
        paymentVoucher.setDescription(ivc.basic.description);
        // TODO: Rest of the attributes

        return paymentVoucher;
    }

    private List<LineItem> extractLineItems(List<Ivc.Item> items, PaymentVoucher pv) {
        return items.stream().map(item -> {
            LineItem lineItem = new LineItem();

            lineItem.setAgencyService(item.agencyCode);
            lineItem.setPaymentVoucher(pv); // Link to PV
            // TODO: Rest of the attributes

            return lineItem;
        }).collect(Collectors.toList());
    }

}
