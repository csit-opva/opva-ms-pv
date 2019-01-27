package sg.gov.csit.opvamspv.lineitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.gov.csit.opvamspv.paymentvoucher.PaymentVoucher;

import java.util.List;

@Repository
public interface LineItemRepository extends JpaRepository<LineItem, Long> {
    List<LineItem> findByPaymentVoucher(PaymentVoucher paymentVoucher);
}
