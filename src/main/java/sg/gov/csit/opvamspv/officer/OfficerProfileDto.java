package sg.gov.csit.opvamspv.officer;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OfficerProfileDto {
    private String pfNo;
    private String name;
    private String role;

    public OfficerProfileDto(Officer officer) {
        this.pfNo = officer.getPf();
        this.name = officer.getName();
        this.role = officer.getRole().value();
    }

    public String getPfNo() {
        return pfNo;
    }

    public void setPfNo(String pfNo) {
        this.pfNo = pfNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
