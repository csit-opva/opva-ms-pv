package sg.gov.csit.opvamspv.officer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OfficerController {
    private final OfficerRepository officerRepository;

    public OfficerController(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    @GetMapping("/api/v1/Officers/{officerId}")
    public Officer getOfficer(@PathVariable String officerId) {
        return officerRepository.getOne(officerId);
    }

    @GetMapping("/api/v1/Officers")
    public List<Officer> getOfficers() {
        return officerRepository.findAll();
    }

    @PutMapping("/api/v1/Officers/{officerId}")
    public Officer updateOfficer(@PathVariable Long officerId, @RequestBody Officer officer) {
        return officerRepository.save(officer);
    }

    @PostMapping("/api/v1/Officers")
    public List<Officer> createOfficers(@RequestBody List<Officer> officers) {
        return officerRepository.saveAll(officers);
    }

    @DeleteMapping("/api/v1/Officers/{officerId}")
    public void deleteOfficer(@PathVariable String officerId) {
        officerRepository.deleteById(officerId);
    }

}
