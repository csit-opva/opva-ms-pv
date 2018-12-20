package sg.gov.csit.opvamspv.station;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sg.gov.csit.opvamspv.station.Station;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

}
