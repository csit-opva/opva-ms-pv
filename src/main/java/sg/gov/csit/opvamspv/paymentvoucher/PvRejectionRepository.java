package sg.gov.csit.opvamspv.paymentvoucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PvRejectionRepository extends JpaRepository<PvRejection, Long> {
}
