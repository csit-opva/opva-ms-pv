package sg.gov.csit.opvamspv.receipt;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;

import java.sql.Blob;
import java.sql.SQLException;

@RestController
public class ReceiptController {
    private final ReceiptRepository receiptRepository;

    public ReceiptController(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @GetMapping(value = "/api/v1/Receipts/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getReceiptPdf(@PathVariable Long id) {
        Blob blob = receiptRepository
                .findById(id)
                .map(Receipt::getReceiptFile)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to find Receipt of id: " + id));

        try {
            long blobLength = blob.length();
            return blob.getBytes(1, (int) blobLength);
        } catch (SQLException e) {
            return null;
        }
    }
}
