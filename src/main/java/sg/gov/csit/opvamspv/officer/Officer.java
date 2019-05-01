package sg.gov.csit.opvamspv.officer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import sg.gov.csit.opvamspv.accesscontrollist.Role;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Officer {
    @Id
    private String pf;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    private boolean isAdmin;

    @NotNull
    private boolean isAuditor;

    public Officer(){}

    public Officer(String pf, String name, Role role) {
        this.pf = pf;
        this.name = name;
        this.role = role;
        this.isAdmin=true;
        this.isAuditor = false;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isAuditor() {
        return isAuditor;
    }

    public void setAuditor(boolean auditor) {
        isAuditor = auditor;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
