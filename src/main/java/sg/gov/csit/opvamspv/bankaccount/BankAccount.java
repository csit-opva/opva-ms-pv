package sg.gov.csit.opvamspv.bankaccount;

import sg.gov.csit.opvamspv.station.Station;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
    @OneToOne
    private Station station;
}
