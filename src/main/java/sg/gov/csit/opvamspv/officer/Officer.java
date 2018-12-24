package sg.gov.csit.opvamspv.officer;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

// import javax.validation.constraints.NotNull;

@Entity
public class Officer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String pf;

    @NotBlank
    private String name;

    public Long getId() {
        return this.id;
    }

    public String getPf() {
        return this.pf;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public void setName(String name) {
        this.name = name;
    }

}
