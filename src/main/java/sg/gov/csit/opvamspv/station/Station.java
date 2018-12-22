package sg.gov.csit.opvamspv.station;

import sg.gov.csit.opvamspv.bankaccount.BankAccount;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private boolean isActive;

    @NotNull
    private LocalDate validFrom;

    @NotNull
    private LocalDate validTo;

//    // TODO: Review
//    @NotNull
//    @OneToOne(mappedBy = "station")
//    private BankAccount bankAccount;

//    @NotNull
//    @OneToOne(mappedBy = "station")
//    private Set<BankAccount> bankAccounts;
}
