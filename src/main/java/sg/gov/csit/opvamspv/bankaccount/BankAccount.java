package sg.gov.csit.opvamspv.bankaccount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import sg.gov.csit.opvamspv.station.Station;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String bankName;

    @NotNull
    private String accountNo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private Station station;
}
