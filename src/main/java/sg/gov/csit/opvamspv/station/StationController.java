package sg.gov.csit.opvamspv.station;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class StationController {

    private final StationRepository stationRepository;

    public StationController(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @GetMapping("/api/v1/Stations/{stationId}")
    public Station getStation(@PathVariable Long stationId) {
         return stationRepository.getOne(stationId);
    }

    @GetMapping("/api/v1/Stations")
    public List<Station> getStations() {
        return stationRepository.findAll();
    }

    @PostMapping("/api/v1/Stations")
    public Station createStation(@RequestBody Station station) {
        return stationRepository.save(station);
    }

    @DeleteMapping("/api/v1/Stations/{stationId}")
    public void deleteStation(@PathVariable Long stationId) {
        stationRepository.deleteById(stationId);
    }

}
