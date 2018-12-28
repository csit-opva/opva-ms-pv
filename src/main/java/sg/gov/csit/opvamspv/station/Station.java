package sg.gov.csit.opvamspv.station;

import org.hibernate.validator.constraints.Length;
import sg.gov.csit.opvamspv.officer.Officer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Station {
    @Id
    @Length(min = 3, max = 3) // Not sure this is the best way to enforce constraint
    private String stationCode;

    @NotNull
    private String name;

    @NotNull
    private LocalDate validFrom;

    @NotNull
    private LocalDate validTo;

    @OneToOne(fetch = FetchType.LAZY)
    private Officer checkingOfficer;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Officer> supportingOfficers;

    @OneToOne(fetch = FetchType.LAZY)
    private Officer approvingOfficer;

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public Officer getCheckingOfficer() {
        return checkingOfficer;
    }

    public void setCheckingOfficer(Officer checkingOfficer) {
        this.checkingOfficer = checkingOfficer;
    }

    public Set<Officer> getSupportingOfficers() {
        return supportingOfficers;
    }

    public void setSupportingOfficers(Set<Officer> supportingOfficers) {
        this.supportingOfficers = supportingOfficers;
    }

    public Officer getApprovingOfficer() {
        return approvingOfficer;
    }

    public void setApprovingOfficer(Officer approvingOfficer) {
        this.approvingOfficer = approvingOfficer;
    }
}
