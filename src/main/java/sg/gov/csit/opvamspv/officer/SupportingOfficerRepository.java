package sg.gov.csit.opvamspv.officer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportingOfficerRepository extends JpaRepository<SupportingOfficer, Long> {
}