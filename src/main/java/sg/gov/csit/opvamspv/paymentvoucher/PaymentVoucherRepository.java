package sg.gov.csit.opvamspv.paymentvoucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentVoucherRepository extends JpaRepository<PaymentVoucher, Long> {
    @Query(value = "SELECT * FROM payment_voucher pv " +
            "LEFT JOIN station s ON s.station_code = pv.station_station_code " +
            "WHERE s.checking_officer_pf = :pfNo",
            nativeQuery = true)
    List<PaymentVoucher> findCheckingPaymentVouchers(@Param("pfNo") String pfNo);

    @Query(value = "SELECT * FROM payment_voucher pv " +
            "LEFT JOIN station s ON s.station_code = pv.station_station_code " +
            "WHERE s.approving_officer_pf = :pfNo",
            nativeQuery = true)
    List<PaymentVoucher> findApprovingPaymentVouchers(@Param("pfNo") String pfNo);

    // TODO: Check if we want to join Claims as well, and only display the relevant claims
    @Query(value = "SELECT * FROM payment_voucher pv " +
            "JOIN station s ON s.station_code = pv.station_station_code " +
            "JOIN station_supporting_officers sso ON sso.station_station_code = s.station_code " +
            "WHERE sso.supporting_officers_pf = :pfNo",
            nativeQuery = true)
    List<PaymentVoucher> findSupportingPaymentVouchers(@Param("pfNo") String pfNo);
}
