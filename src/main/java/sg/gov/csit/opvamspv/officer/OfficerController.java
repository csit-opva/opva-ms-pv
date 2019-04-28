package sg.gov.csit.opvamspv.officer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import sg.gov.csit.opvamspv.exception.OfficerNotFoundException;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OfficerController {
    private final OfficerRepository officerRepository;
    private static final Logger log = LoggerFactory.getLogger(OfficerController.class);


    public OfficerController(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    @GetMapping("/api/v1/Officers/{officerId}")
    public Officer getOfficer(@PathVariable String officerId) {
//        try {
            return officerRepository.findById(officerId).
                    orElseThrow(() -> new OfficerNotFoundException(officerId));
//            log.info("Result" + officer);


//            if (officer == null) {
//                throw new ResourceNotFoundException("Who are you looking for?");
//            } else {
//
//                log.info("Result" + officer);
//
//                return officer;
//            }

//        } catch (Exception ex) {
//            throw new ResourceNotFoundException("Who are you looking for?");
//        }
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

    @PostMapping("/api/v1/Officer")
    public Officer createOfficer(@RequestBody Officer officer) {
        return officerRepository.save(officer);
    }

    @DeleteMapping("/api/v1/Officers/{officerId}")
    public void deleteOfficer(@PathVariable String officerId) {
        officerRepository.deleteById(officerId);
    }

    @PutMapping("/api/v1/Officers/{officerId}/Admin")
    public Officer makeAdmin(@PathVariable String officerId) {
        Officer officer = officerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("Officer of " + officerId + " not found."));
        officer.setAdmin(true);
        return officerRepository.save(officer);
    }

    @DeleteMapping("/api/v1/Officers/{officerId}/Admin")
    public Officer removeAdminRole(@PathVariable String officerId) {
        Officer officer = officerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("Officer of " + officerId + " not found."));
        officer.setAdmin(false);
        return officerRepository.save(officer);
    }

    @PutMapping("/api/v1/Officers/{officerId}/Auditor")
    public Officer makeAuditor(@PathVariable String officerId) {
        Officer officer = officerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("Officer of " + officerId + " not found."));
        officer.setAuditor(true);
        return officerRepository.save(officer);
    }

    @DeleteMapping("/api/v1/Officers/{officerId}/Auditor")
    public Officer removeAuditorRole(@PathVariable String officerId) {
        Officer officer = officerRepository
                .findById(officerId)
                .orElseThrow(() -> new ResourceNotFoundException("Officer of " + officerId + " not found."));
        officer.setAuditor(false);
        return officerRepository.save(officer);
    }

    @GetMapping("/api/v1/SystemOfficers")
    public List<Officer> getSystemOfficers() {
        return officerRepository
                .findAll()
                .stream()
                .filter(officer -> officer.isAdmin() || officer.isAuditor())
                .collect(Collectors.toList());
    }
}
