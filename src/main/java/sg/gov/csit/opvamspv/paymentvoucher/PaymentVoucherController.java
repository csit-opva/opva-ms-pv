package sg.gov.csit.opvamspv.paymentvoucher;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController
public class PaymentVoucherController {

    private final PaymentVoucherRepository paymentVoucherRepository;

    public PaymentVoucherController(PaymentVoucherRepository paymentVoucherRepository) {
        this.paymentVoucherRepository = paymentVoucherRepository;
    }

    @GetMapping("/api/v1/PaymentVouchers/{paymentVoucherId}")
    public PaymentVoucher getPaymentVoucher(@PathVariable Long paymentVoucherId) {
        return paymentVoucherRepository.getOne(paymentVoucherId);
    }

    @GetMapping("/api/v1/PaymentVouchers")
    public List<PaymentVoucher> getPaymentVouchers() {
        return paymentVoucherRepository.findAll();
    }

    @GetMapping("/api/v1/PaymentVouchers/checking")
    public List<PaymentVoucher> getCheckingPaymentVouchers() {
        throw new NotImplementedException();
    }

    @GetMapping("/api/v1/PaymentVouchers/supporting")
    public List<PaymentVoucher> getSupportingPaymentVouchers() {
        throw new NotImplementedException();
    }

    @GetMapping("/api/v1/PaymentVouchers/approving")
    public List<PaymentVoucher> getApprovingPaymentVouchers() {
        throw new NotImplementedException();
    }

    @PostMapping(value = "/api/v1/PaymentVouchers", consumes = MediaType.APPLICATION_XML_VALUE)
    public PaymentVoucher createPaymentVoucher(@RequestBody PaymentVoucher paymentVoucher) {
        return paymentVoucherRepository.save(paymentVoucher);
    }

}