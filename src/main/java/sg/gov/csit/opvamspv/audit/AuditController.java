package sg.gov.csit.opvamspv.audit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
public class AuditController {
    @GetMapping("/foo")
    public String getPendingAudits() {
        throw new NotImplementedException();
    }
}
