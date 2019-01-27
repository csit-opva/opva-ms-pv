package sg.gov.csit.opvamspv.lineitem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;
import sg.gov.csit.opvamspv.paymentvoucher.PaymentVoucher;
import sg.gov.csit.opvamspv.paymentvoucher.PaymentVoucherRepository;
import sg.gov.csit.opvamspv.receipt.Receipt;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LineItemController {
    private final LineItemRepository lineItemRepository;
    private final PaymentVoucherRepository paymentVoucherRepository;

    public LineItemController(LineItemRepository lineItemRepository, PaymentVoucherRepository paymentVoucherRepository) {
        this.lineItemRepository = lineItemRepository;
        this.paymentVoucherRepository = paymentVoucherRepository;
    }

    @GetMapping("/api/v1/LineItems")
    public List<LineItem> getLineItems() {
        return lineItemRepository.findAll();
    }

    @GetMapping("/api/v1/PvLineItems/{pvId}")
    public List<LineItemDTO> getPvLineItems(@PathVariable Long pvId) {
        PaymentVoucher pv = paymentVoucherRepository
                .findById(pvId)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentVoucher of " + pvId + " not found."));

        return lineItemRepository
                .findByPaymentVoucher(pv)
                .stream()
                .map(this::lineItemToDto)
                .collect(Collectors.toList());
    }

    private LineItemDTO lineItemToDto(LineItem lineItem) {
        LineItemDTO lineItemDTO = new LineItemDTO();
        lineItemDTO.setId(lineItem.getId());
        lineItemDTO.setLineItemNo(lineItem.getLineItemNo());
        lineItemDTO.setAgencyService(lineItem.getAgencyService());
        lineItemDTO.setPrepaymentFrom(lineItem.getPrepaymentFrom());
        lineItemDTO.setPrepaymentTo(lineItem.getPrepaymentTo());
        lineItemDTO.setFundCentre(lineItem.getFundCentre());
        lineItemDTO.setCostCentre(lineItem.getCostCentre());
        lineItemDTO.setCountry(lineItem.getCountry());
        lineItemDTO.setFundNo(lineItem.getFundNo());
        lineItemDTO.setFixedAssetIndicator(lineItem.getFixedAssetIndicator());
        lineItemDTO.setTransactionType(lineItem.getTransactionType());
        lineItemDTO.setGlAccount(lineItem.getGlAccount());
        lineItemDTO.setFixedAssetNumber(lineItem.getFixedAssetNumber());
        lineItemDTO.setFixedAssetQty(lineItem.getFixedAssetQty());
        lineItemDTO.setReceiptNo(lineItem.getReceiptNo());
        lineItemDTO.setReceiptDate(lineItem.getReceiptDate());
        lineItemDTO.setAmount(lineItem.getAmount());
        lineItemDTO.setExtraChargesSgd(lineItem.getExtraChargesSgd());
        lineItemDTO.setTaxCode(lineItem.getTaxCode());
        lineItemDTO.setAmountWithGst(lineItem.getAmountWithGst());
        lineItemDTO.setAssignmentNumber(lineItem.getAssignmentNumber());
        lineItemDTO.setProject(lineItem.getProject());
        lineItemDTO.setDetailDescription(lineItem.getDetailDescription());
        lineItemDTO.setPaymentVoucher(lineItem.getPaymentVoucher());
        lineItemDTO.setReceipts(getLineItemReceiptIds(lineItem));
        return lineItemDTO;
    }

    private List<Long> getLineItemReceiptIds(LineItem lineItem) {
        return lineItem.getReceipts()
                .stream()
                .map(Receipt::getId)
                .collect(Collectors.toList());
    }

}
