package sg.gov.csit.opvamspv.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OfficerNotFoundException extends RuntimeException{

    public OfficerNotFoundException(String pf) {
        super("Officer pf not found : " + pf);
    }

}
