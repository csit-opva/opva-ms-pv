package sg.gov.csit.opvamspv.station;

import org.springframework.web.bind.annotation.*;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;
import sg.gov.csit.opvamspv.officer.Officer;
import sg.gov.csit.opvamspv.officer.OfficerRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController
public class StationController {
    private final StationRepository stationRepository;
    private final OfficerRepository officerRepository;

    public StationController(StationRepository stationRepository, OfficerRepository officerRepository) {
        this.stationRepository = stationRepository;
        this.officerRepository = officerRepository;
    }

    @GetMapping("/api/v1/Stations/{stationId}")
    public Station getStation(@PathVariable String stationId) {
        return stationRepository.getOne(stationId);
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
    public Station assignCheckingOfficer(@PathVariable String stationId, @PathVariable Long officerId) {
        Officer officer = officerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        return stationRepository.findById(stationId).map(s -> {
            s.setCheckingOfficer(officer);
            return stationRepository.save(s);
        }).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @DeleteMapping("/api/v1/Stations/{stationId}/Checking/{officerId}")
    public String unassignCheckingOfficer(@PathVariable String stationId, @PathVariable Long officerId) {
        throw new NotImplementedException();
    }

    @PutMapping("/api/v1/Stations/{stationId}/Approving/{officerId}")
    public Station assignApprovingOfficer(@PathVariable String stationId, @PathVariable Long officerId) {
        Officer officer = officerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        return stationRepository.findById(stationId).map(s -> {
            s.setApprovingOfficer(officer);
            return stationRepository.save(s);
        }).orElseThrow(() -> new ResourceNotFoundException("not found"));
    }

    @DeleteMapping("/api/v1/Stations/{stationId}/Approving/{officerId}")
    public String unassignApprovingfficer(@PathVariable String stationId, @PathVariable Long officerId) {
        throw new NotImplementedException();
    }

}
