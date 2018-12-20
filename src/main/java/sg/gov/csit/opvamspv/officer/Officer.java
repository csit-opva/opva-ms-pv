package sg.gov.csit.opvamspv.officer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Officer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String pf;

    @NotNull
    private String name;
}
