package sg.gov.csit.opvamspv.officer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import sg.gov.csit.opvamspv.accesscontrollist.Acl;
import sg.gov.csit.opvamspv.accesscontrollist.AllowRoles;
import sg.gov.csit.opvamspv.accesscontrollist.Role;
import sg.gov.csit.opvamspv.exception.OfficerNotFoundException;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OfficerController {
    private final OfficerRepository officerRepository;
    private static final Logger log = LoggerFactory.getLogger(OfficerController.class);


    public OfficerController(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }


    @AllowRoles(action = "Retrieve Officer", roles = {Role.Admin})
    @GetMapping("/api/v1/Officer/Profile")
    public OfficerProfileDto getOfficerProfile(@RequestAttribute("pfNo") String officerId, @RequestAttribute("txnId") String txnId) {
//        try {

        log.info("getOfficer txnId: " + txnId);
        log.info("requested pfNo: " + officerId);

        Officer officer = officerRepository.findById(officerId).
                orElseThrow(() -> new OfficerNotFoundException(officerId));

        return new OfficerProfileDto(officer);
//
    }

    @AllowRoles(action = "Retrieve Officer", roles = {Role.Admin})
    @GetMapping("/api/v1/Officers/{officerId}")
    public OfficerProfileDto getOfficer(@PathVariable String officerId, String transactionId, @RequestAttribute("txnId") String txnId) {
//        try {

        System.out.println("getOfficer txnId: " + txnId);

        Officer officer = officerRepository.findById(officerId).
                orElseThrow(() -> new OfficerNotFoundException(officerId));

        System.out.println(officer.getName());

        return new OfficerProfileDto(officer);

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
