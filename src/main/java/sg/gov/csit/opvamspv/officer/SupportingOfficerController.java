package sg.gov.csit.opvamspv.officer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SupportingOfficerController {
    private final SupportingOfficerRepository supportingOfficerRepository;

    public SupportingOfficerController(SupportingOfficerRepository supportingOfficerRepository) {
        this.supportingOfficerRepository = supportingOfficerRepository;
    }

    @GetMapping("/api/v1/SupportingOfficers")
    public List<SupportingOfficer> getSupportingOfficers() {
        return supportingOfficerRepository.findAll();
    }
}
