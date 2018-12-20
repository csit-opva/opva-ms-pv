package sg.gov.csit.opvamspv.paymentvoucher;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class PaymentVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long sapDocNoc;

    // Need some length constraints
    @NotNull
    private String pvNumber;

    // Need some length constraints
    @NotNull
    private String requestorName;

}
