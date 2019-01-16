package sg.gov.csit.opvamspv.officer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupportingOfficerRepository extends JpaRepository<SupportingOfficer, Long> {
    Optional<SupportingOfficer> findByOfficer(Officer officer);
}
