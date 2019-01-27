package sg.gov.csit.opvamspv.station;

import org.springframework.web.bind.annotation.*;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;
import sg.gov.csit.opvamspv.officer.Officer;
import sg.gov.csit.opvamspv.officer.OfficerRepository;
import sg.gov.csit.opvamspv.officer.SupportingOfficer;
import sg.gov.csit.opvamspv.officer.SupportingOfficerRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class StationController {
    private final StationRepository stationRepository;
    private final OfficerRepository officerRepository;
    private final SupportingOfficerRepository supportingOfficerRepository;

    public StationController(StationRepository stationRepository, OfficerRepository officerRepository, SupportingOfficerRepository supportingOfficerRepository) {
        this.stationRepository = stationRepository;
        this.officerRepository = officerRepository;
        this.supportingOfficerRepository = supportingOfficerRepository;
    }

    @GetMapping("/api/v1/Stations/{stationCode}")
    public Station getStation(@PathVariable String stationCode) {
        return stationRepository
                .findById(stationCode)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @GetMapping("/api/v1/Stations")
    public List<Station> getStations() {
        return stationRepository.findAll();
    }

    @PutMapping("/api/v1/Stations/{stationId}")
    public Station updateStation(@RequestBody Station station) {
        return stationRepository.save(station);
    }

//    @PostMapping("/api/v1/Stations")
//    public Station createStation(@RequestBody Station station) {
//        return stationRepository.save(station);
//    }

    @PostMapping("/api/v1/Stations")
    public List<Station> createStations(@RequestBody List<Station> stations) {
        return stationRepository.saveAll(stations);
    }

    @DeleteMapping("/api/v1/Stations/{stationId}")
    public void deleteStation(@PathVariable String stationId) {
        stationRepository.deleteById(stationId);
    }

    @PutMapping("/api/v1/Stations/{stationId}/Checking/{officerId}")
    public Station assignCheckingOfficer(@PathVariable String stationId, @PathVariable String officerId) {
        Officer officer = officerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        return stationRepository.findById(stationId).map(s -> {
            s.setCheckingOfficer(officer);
            return stationRepository.save(s);
        }).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @DeleteMapping("/api/v1/Stations/{stationId}/Checking")
    public Station unassignCheckingOfficer(@PathVariable String stationId) {
        return stationRepository.findById(stationId).map(s -> {
            s.setCheckingOfficer(null);
            return stationRepository.save(s);
        }).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @PutMapping("/api/v1/Stations/{stationId}/Approving/{officerId}")
    public Station assignApprovingOfficer(@PathVariable String stationId, @PathVariable String officerId) {
        Officer officer = officerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        return stationRepository.findById(stationId).map(s -> {
            s.setApprovingOfficer(officer);
            return stationRepository.save(s);
        }).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @DeleteMapping("/api/v1/Stations/{stationId}/Approving")
    public Station unassignApprovingfficer(@PathVariable String stationId) {
        return stationRepository.findById(stationId).map(s -> {
            s.setApprovingOfficer(null);
            return stationRepository.save(s);
        }).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    // Probably not idiomatic Spring, but it works :/
    @PutMapping("/api/v1/Stations/{stationId}/Supporting/{officerId}")
    public Station assignSupportingOfficer(@PathVariable String stationId, @PathVariable String officerId) {
        Officer officer = officerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        return stationRepository.findById(stationId).map(s -> {
            Optional<SupportingOfficer> so = supportingOfficerRepository
                    .findByOfficer(officer);

            Set<SupportingOfficer> supportingOfficers = s.getSupportingOfficers();

            if (supportingOfficers == null) {
                supportingOfficers = new HashSet<>();
            }

            SupportingOfficer supportingOfficer;

            if (so.isPresent()) {
                supportingOfficer = so.get();
            } else {
                supportingOfficer = new SupportingOfficer();
                supportingOfficer.setOfficer(officer);
                supportingOfficerRepository.save(supportingOfficer);
            }

            supportingOfficers.add(supportingOfficer);
            return stationRepository.save(s);
        }).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @DeleteMapping("/api/v1/Stations/{stationId}/Supporting/{officerId}")
    public Station unassignSupportingOfficer(@PathVariable String stationId, @PathVariable Long officerId) {
        SupportingOfficer supportingOfficer = supportingOfficerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException(officerId.toString()));

        return stationRepository.findById(stationId).map(station -> {
            Set<SupportingOfficer> supportingOfficers = station.getSupportingOfficers();
            supportingOfficers.remove(supportingOfficer);

            return stationRepository.save(station);
        }).orElseThrow(() -> new ResourceNotFoundException(stationId));
    }

}
