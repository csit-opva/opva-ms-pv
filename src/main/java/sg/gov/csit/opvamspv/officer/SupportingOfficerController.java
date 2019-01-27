package sg.gov.csit.opvamspv.officer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;
import sg.gov.csit.opvamspv.station.Station;
import sg.gov.csit.opvamspv.station.StationRepository;

import java.util.Set;

@RestController
public class SupportingOfficerController {
    private final SupportingOfficerRepository supportingOfficerRepository;
    private final StationRepository stationRepository;

    public SupportingOfficerController(SupportingOfficerRepository supportingOfficerRepository, StationRepository stationRepository) {
        this.supportingOfficerRepository = supportingOfficerRepository;
        this.stationRepository = stationRepository;
    }

    @GetMapping("/api/v1/SupportingOfficers/{stationId}")
    public Set<SupportingOfficer> getSupportingOfficers(@PathVariable String stationId) {
        Station station = stationRepository
                .findById(stationId)
                .orElseThrow(() -> new ResourceNotFoundException(stationId));

        return station.getSupportingOfficers();
    }
}
