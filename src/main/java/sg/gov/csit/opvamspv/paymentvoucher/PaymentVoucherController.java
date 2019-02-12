package sg.gov.csit.opvamspv.paymentvoucher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;
import sg.gov.csit.opvamspv.exception.UnauthorizedException;
import sg.gov.csit.opvamspv.lineitem.LineItem;
import sg.gov.csit.opvamspv.lineitem.LineItemRepository;
import sg.gov.csit.opvamspv.officer.Officer;
import sg.gov.csit.opvamspv.officer.OfficerRepository;
import sg.gov.csit.opvamspv.officer.SupportingOfficer;
import sg.gov.csit.opvamspv.officer.SupportingOfficerRepository;
import sg.gov.csit.opvamspv.receipt.Receipt;
import sg.gov.csit.opvamspv.receipt.ReceiptRepository;
import sg.gov.csit.opvamspv.station.Station;
import sg.gov.csit.opvamspv.station.StationRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.sql.rowset.serial.SerialBlob;
import javax.xml.bind.JAXB;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
public class PaymentVoucherController {
    private final PaymentVoucherRepository paymentVoucherRepository;
    private final StationRepository stationRepository;
    private final LineItemRepository lineItemRepository;
    private final ReceiptRepository receiptRepository;
    private final OfficerRepository officerRepository;
    private final PvRejectionRepository pvRejectionRepository;

    public PaymentVoucherController(PaymentVoucherRepository paymentVoucherRepository,
                                    StationRepository stationRepository, LineItemRepository lineItemRepository,
                                    ReceiptRepository receiptRepository, OfficerRepository officerRepository,
                                    PvRejectionRepository pvRejectionRepository) {
        this.paymentVoucherRepository = paymentVoucherRepository;
        this.stationRepository = stationRepository;
        this.lineItemRepository = lineItemRepository;
        this.receiptRepository = receiptRepository;
        this.officerRepository = officerRepository;
        this.pvRejectionRepository = pvRejectionRepository;
    }

    @GetMapping("/api/v1/Checking/PaymentVouchers")
    public List<PaymentVoucher> getCheckingPaymentVouchers(@RequestAttribute String pfNo) {
        return paymentVoucherRepository.findCheckingPaymentVouchers(pfNo);
    }

    @GetMapping("/api/v1/Approving/PaymentVouchers")
    public List<PaymentVoucher> getApprovingPaymentVouchers(@RequestAttribute String pfNo) {
        return paymentVoucherRepository.findApprovingPaymentVouchers(pfNo);
    }

    @GetMapping("/api/v1/Supporting/PaymentVouchers")
    public List<PaymentVoucher> getSupportingPaymentVouchers(@RequestAttribute String pfNo) {
        return paymentVoucherRepository.findSupportingPaymentVouchers(pfNo);
    }

    @GetMapping("/api/v1/PaymentVouchers/{paymentVoucherId}")
    public Optional<PaymentVoucher> getPaymentVoucher(@PathVariable Long paymentVoucherId) {
        return paymentVoucherRepository.findById(paymentVoucherId);
//        return paymentVoucherRepository.getOne(paymentVoucherId);
    }

    @GetMapping("/api/v1/PaymentVouchers")
    public List<PaymentVoucher> getPaymentVouchers() {
        return paymentVoucherRepository.findAll();
    }

    @PostMapping(value = "/api/v1/PaymentVouchers")
    public PaymentVoucher createPaymentVoucher(@RequestParam("claimForm") MultipartFile claimForm,
                                               @RequestParam("sapResult") MultipartFile sapResult,
                                               @RequestParam("receipts") MultipartFile[] receipts,
                                               // For future use, when supporting docs comes with a payment voucher
                                               @RequestParam(value = "supportingDocs", required = false) MultipartFile[] supportingDocs) {
        XmlClaimForm xmlClaimForm = getXmlClaimForm(claimForm);
        PVHeader pvHeader = new PVHeader(xmlClaimForm.header.pvFormId);
        String stationCode = pvHeader.getStationCode();
        String pvFormNo = pvHeader.toString();
        String docNoStr = getDocNoStr(sapResult, pvFormNo);

        Station station = stationRepository.findById(stationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Station"));

        PaymentVoucher pv = extractPaymentVoucher(xmlClaimForm, station, pvFormNo, docNoStr);

        paymentVoucherRepository.save(pv);

        List<XmlClaimForm.Item> items = xmlClaimForm.items;
        List<LineItem> lineItems = items
                .stream()
                .map(itemToLineItem(pv))
                .collect(Collectors.toList());
        Arrays.stream(receipts)
                // Properly tag every single receipt to the right line item
                .forEach(tagReceiptToLineItem(lineItems));
        lineItemRepository.saveAll(lineItems);

        return pv;
    }

    private Consumer<MultipartFile> tagReceiptToLineItem(List<LineItem> lineItems) {
        return receiptFile -> {
            // Get the filename part e.g. 01-01.pdf, but we only want 01-01
            String filename = Objects.requireNonNull(receiptFile.getOriginalFilename()).split("\\.")[0]; // e.g. 01-01

            // Make the receiptFile ready for seeding inside, so apparently SerialBlob works
            Receipt receipt = new Receipt();
            Blob blob = null;
            try {
                blob = new SerialBlob(receiptFile.getBytes());

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }

            receipt.setReceiptFile(blob);

            // split into line Item and receipt no
            // e.g. 01-01 splits into 01, -, 01
            // we only care about the 01 and 01, which are lineitem and receipt no
            String[] lineItemToReceiptNo = filename.split("-");

            int index = Integer.parseInt(lineItemToReceiptNo[0]) - 1;
            LineItem lineItem = lineItems.get(index);

            // Basically if the line Item didn't have a receipt in the first place, it's null as expected
            // To get around it, initialize your own Receipts hashset ¯\_(ツ)_/¯
            if (lineItem.getReceipts() == null) {
                lineItem.setReceipts(new HashSet<>());
            }
            // receiptRepository.save(receipt);
            lineItem.getReceipts().add(receipt);
        };
    }

    // TODO: When the requirements are confirmed, implement this API
    @PostMapping(value = "/api/v1/PaymentVouchers/{paymentVoucherId}/SupportingDocs")
    public String uploadSupportingDocs(@PathVariable Long paymentVoucherId,
                                       @RequestParam("supportingDocs") MultipartFile[] supportingDocs) {
        throw new NotImplementedException();
    }

    private String getDocNoStr(MultipartFile sapResult, String pvFormNo) {
        String docNoStr = "";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(sapResult.getInputStream()))) {
            String line;
            br.readLine(); // Read past the first to skip headers, which we don't need to work with

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                String formNo = tokens[1]; // the PV Form No. is the second entry
                if (formNo.equalsIgnoreCase(pvFormNo)) {
                    docNoStr = tokens[2]; // The Doc. Num is the third entry
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docNoStr;
    }

    @PostMapping("/api/v1/PaymentVouchers/{paymentVoucherId}/check")
    public PaymentVoucher setPVChecked(@PathVariable Long paymentVoucherId, @RequestAttribute String pfNo) {
        PaymentVoucher pv = paymentVoucherRepository
                .findById(paymentVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        Station station = stationRepository
                .findById(pv.getStation().getStationCode())
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        // Check if officer can perform this action
        Officer checkingOfficer = station.getCheckingOfficer();
        String coPfNo = checkingOfficer.getPf();
        if (!coPfNo.equalsIgnoreCase(pfNo)) {
            throw new UnauthorizedException("Officer of PF number " + pfNo + "  is not the checking officer of this station");
        }

        if (pv.getStatus() != PVStatus.PENDING_CHECK) {
            throw new ResourceNotFoundException("Payment Voucher of " + paymentVoucherId + "does not require checking at the moment."); // Needs better error for now
        }

        pv.setStatus(PVStatus.PENDING_SUPPORTING);
        return paymentVoucherRepository.save(pv);
    }

    @PostMapping("/api/v1/PaymentVouchers/{paymentVoucherId}/support")
    public PaymentVoucher supportPV(@PathVariable Long paymentVoucherId, @RequestAttribute String pfNo) {
        PaymentVoucher pv = paymentVoucherRepository.findById(paymentVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        // Validate that current user supporting is a supporting officer.
        // Could potentially be simplified with AOP (Aspect oriented programming)
        List<String> stationSupportingOfficerPvs = pv
                .getStation()
                .getSupportingOfficers()
                .stream()
                .map(SupportingOfficer::getOfficer)
                .map(Officer::getPf)
                .collect(Collectors.toList());
        if (!stationSupportingOfficerPvs.contains(pfNo)) {
            throw new UnauthorizedException("Officer of PF number " + pfNo + "  is not the supporting officer of this station");
        }

        // Validate that this PV is pending supporting
        if (pv.getStatus() != PVStatus.PENDING_SUPPORTING) {
            throw new ResourceNotFoundException("Payment Voucher of " + paymentVoucherId + "does not require supporting at the moment."); // Needs better error for now
        }

        pv.setStatus(PVStatus.PENDING_APPROVAL);
        return paymentVoucherRepository.save(pv);
    }

    @PostMapping("/api/v1/PaymentVouchers/{paymentVoucherId}/approve")
    public PaymentVoucher approvePV(@PathVariable Long paymentVoucherId, @RequestAttribute String pfNo) {
        PaymentVoucher pv = paymentVoucherRepository.findById(paymentVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        if (!pv.getStation().getApprovingOfficer().getPf().equalsIgnoreCase(pfNo)) {
            throw new UnauthorizedException("Officer of PF number " + pfNo + "  is not the approving officer of this station");
        }

        // Validate that this PV is pending approval
        if (pv.getStatus() != PVStatus.PENDING_APPROVAL) {
            throw new ResourceNotFoundException("Payment Voucher of " + paymentVoucherId + "does not require supporting at the moment."); // Needs better error for now
        }

        pv.setStatus(PVStatus.PENDING_PAYMENT);
        return paymentVoucherRepository.save(pv);
    }

    @PostMapping("/api/v1/PaymentVouchers/{paymentVoucherId}/paid")
    public PaymentVoucher payPv(@PathVariable Long paymentVoucherId, @RequestAttribute String pfNo) {
        PaymentVoucher pv = paymentVoucherRepository.findById(paymentVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        Officer officer = officerRepository
                .findById(pfNo)
                .orElseThrow(() -> new ResourceNotFoundException("Officer of " + pfNo + " not found"));

        if (!officer.isFinance()) {
            throw new UnauthorizedException("Officer of PF number " + pfNo + "  is not a Finance officer.");
        }

        // Validate that this PV is pending approval
        if (pv.getStatus() != PVStatus.PENDING_PAYMENT) {
            throw new ResourceNotFoundException("Payment Voucher of " + paymentVoucherId + "does not require payment at the moment."); // Needs better error for now
        }

        pv.setStatus(PVStatus.PAID);
        throw new NotImplementedException();
    }

    @PostMapping("/api/v1/PaymentVouchers/{paymentVoucherId}/reject")
    public ResponseEntity rejectPV(@PathVariable Long paymentVoucherId, @RequestBody String reason, @RequestAttribute String pfNo) {
        PaymentVoucher pv = paymentVoucherRepository.findById(paymentVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("not found"));

        Officer officer = officerRepository
                .findById(pfNo)
                .orElseThrow(() -> new ResourceNotFoundException("Officer of " + pfNo + " not found"));

        Station station = pv.getStation();

        // Doesn't allow user to reject PV if they're not authorized to do so
        // Ugly but working code for now
        Set<SupportingOfficer> supportingOfficers = station.getSupportingOfficers();
        List<String> pfNos = new ArrayList<>(supportingOfficers)
                .stream()
                .map(SupportingOfficer::getOfficer)
                .map(Officer::getPf)
                .collect(Collectors.toList());
        pfNos.add(station.getApprovingOfficer().getPf());
        pfNos.add(station.getCheckingOfficer().getPf());
        if (!pfNos.contains(pfNo) && !officer.isFinance()) {
            throw new UnauthorizedException("Officer of PF number " + pfNo + "  is not authorized to reject this PV.");
        }

        PvRejection pvRejection = new PvRejection();
        pvRejection.setPv(pv);
        pvRejection.setOfficer(officer);
        pvRejection.setReason(reason);

        pv.setStatus(PVStatus.REJECTED);
        paymentVoucherRepository.save(pv);

        pvRejectionRepository.save(pvRejection);

        return new ResponseEntity(HttpStatus.OK);
    }

    // TODO: Move out of controller since this is not really controller logic
    private PaymentVoucher extractPaymentVoucher(XmlClaimForm xmlClaimForm, Station station, String pvNumber,
                                                 String docNoStr) {
        long docNo = Long.parseLong(docNoStr);
        PaymentVoucher paymentVoucher = new PaymentVoucher();

        paymentVoucher.setSapDocNoc(docNo);
        paymentVoucher.setPvNumber(pvNumber);
        paymentVoucher.setRequestorName(xmlClaimForm.basic.requestorName);
        paymentVoucher.setPvDate(xmlClaimForm.basic.pvDate);
        paymentVoucher.setPayee(xmlClaimForm.basic.payee);
        paymentVoucher.setReferenceNumber(xmlClaimForm.basic.referenceNumber);
        paymentVoucher.setCurrency(xmlClaimForm.calculation.currency);
        paymentVoucher.setTotalTaxForeign(xmlClaimForm.calculation.totalTax);
        paymentVoucher.setTotalAmountWithGst(xmlClaimForm.calculation.totalAmountLocal);
        paymentVoucher.setWithholdTaxBaseAmt(xmlClaimForm.calculation.withholdTaxBaseAmt);
        paymentVoucher.setPaymentMethod(xmlClaimForm.basic.paymentMethod);
        paymentVoucher.setHousebank(xmlClaimForm.basic.housebank);
        paymentVoucher.setDescription(xmlClaimForm.basic.description);
        paymentVoucher.setDescriptionLongText(xmlClaimForm.basic.mainDescription);
        paymentVoucher.setCompanyCode(xmlClaimForm.basic.companyCode);
        paymentVoucher.setRate(xmlClaimForm.calculation.currencyRate);
        paymentVoucher.setTaxCode(xmlClaimForm.basic.taxCode);
        paymentVoucher.setStatus(PVStatus.PENDING_CHECK);

        paymentVoucher.setStation(station); // Link to Station

        return paymentVoucher;
    }

    // TODO: Move out of controller since this is not really controller logic
    private XmlClaimForm getXmlClaimForm(MultipartFile claimForm) {
        File file = null;
        try {
            // create temporary file to hold the xml file cos
            // JAXB.unmarshal takes in File as first argument, not MultipartFile
            file = File.createTempFile("something", ".xml");
            claimForm.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JAXB.unmarshal(Objects.requireNonNull(file), XmlClaimForm.class);
    }

    // TODO: Move out of controller since this is not really controller logic
    private List<LineItem> extractLineItems(List<XmlClaimForm.Item> items, PaymentVoucher pv) {
        return items
                .stream()
                .map(itemToLineItem(pv))
                .collect(Collectors.toList());
    }

    // TODO: Move out of controller since this is not really controller logic
    private Function<XmlClaimForm.Item, LineItem> itemToLineItem(PaymentVoucher pv) {
        return item -> {
            LineItem lineItem = new LineItem();

            lineItem.setAgencyService(item.agencyCode);
            lineItem.setPrepaymentFrom(item.prepaymentPeriodFrom);
            lineItem.setPrepaymentTo(item.prepaymentPeriodTo);
            lineItem.setFundCentre(item.fundCentre);
            lineItem.setCostCentre(item.costCentre);
            lineItem.setCountry(item.country);
            lineItem.setFundNo(item.fundNumber);
            lineItem.setFixedAssetIndicator(item.fixedAssetIndicator);
            lineItem.setTransactionType(item.transactionType);
            lineItem.setGlAccount(item.glAccount);
            lineItem.setFixedAssetNumber(item.fixedAssetNo);
            lineItem.setFixedAssetQty(item.unitsOfFixedAsset);
            lineItem.setReceiptNo(item.receiptNo);
            lineItem.setReceiptDate(item.receiptDate);
            lineItem.setAmount(item.amountNoTax);
            lineItem.setExtraChargesSgd(item.extraCharges);
            lineItem.setTaxCode(item.taxCode);
            lineItem.setAmountWithGst(item.amountWithTax);
            lineItem.setAssignmentNumber(item.assignment);
            lineItem.setProject(item.project);
            lineItem.setDetailDescription(item.desc);
            lineItem.setPaymentVoucher(pv); // Link to PV

            return lineItem;
        };
    }

}
