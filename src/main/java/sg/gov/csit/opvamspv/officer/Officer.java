package sg.gov.csit.opvamspv.officer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    private boolean isAdmin;

    @NotNull
    private boolean isAuditor;

    @NotNull
    private boolean isFinance;

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

    public boolean isFinance() {
        return isFinance;
    }

    public void setFinance(boolean finance) {
        isFinance = finance;
    }
}
