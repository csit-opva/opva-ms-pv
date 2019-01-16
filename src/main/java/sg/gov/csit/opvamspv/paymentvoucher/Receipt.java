package sg.gov.csit.opvamspv.paymentvoucher;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Blob;

@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Blob receiptFile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Blob getReceiptFile() {
        return receiptFile;
    }

    public void setReceiptFile(Blob receiptFile) {
        this.receiptFile = receiptFile;
    }
}
