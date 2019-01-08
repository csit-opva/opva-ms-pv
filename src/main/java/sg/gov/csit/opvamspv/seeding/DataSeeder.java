package sg.gov.csit.opvamspv.seeding;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import sg.gov.csit.opvamspv.bankaccount.BankAccount;
import sg.gov.csit.opvamspv.bankaccount.BankAccountRepository;
import sg.gov.csit.opvamspv.claim.Claim;
import sg.gov.csit.opvamspv.claim.ClaimRepository;
import sg.gov.csit.opvamspv.officer.Officer;
import sg.gov.csit.opvamspv.officer.OfficerRepository;
import sg.gov.csit.opvamspv.paymentvoucher.PaymentVoucherRepository;
import sg.gov.csit.opvamspv.station.Station;
import sg.gov.csit.opvamspv.station.StationRepository;

import java.time.LocalDate;

@Component
public class DataSeeder {
    private final OfficerRepository officerRepository;
    private final StationRepository stationRepository;
    private final ClaimRepository claimRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PaymentVoucherRepository paymentVoucherRepository;

    public DataSeeder(BankAccountRepository bankAccountRepository, ClaimRepository claimRepository, OfficerRepository officerRepository, PaymentVoucherRepository paymentVoucherRepository, StationRepository stationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.claimRepository = claimRepository;
        this.officerRepository = officerRepository;
        this.paymentVoucherRepository = paymentVoucherRepository;
        this.stationRepository = stationRepository;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedOfficers();
        seedStations();
        seedClaims();
        seedBanks();
        seedPaymentVouchers();
    }

    private void seedOfficers() {
        Officer officer = new Officer();

        officer.setPf("11011");
        officer.setName("Tan Miam Miam");
        officerRepository.save(officer);
        officer.setPf("11012");
        officer.setName("Koh Ee Giam");
        officerRepository.save(officer);
        officer.setPf("11013");
        officer.setName("Tan Yang Guo");
        officerRepository.save(officer);
        officer.setPf("11014");
        officer.setName("Ng Yang Ming");
        officerRepository.save(officer);
        officer.setPf("11015");
        officer.setName("Kim Huat");
        officerRepository.save(officer);
        officer.setPf("11016");
        officer.setName("Toh Hao Hao");
        officerRepository.save(officer);
        officer.setPf("11017");
        officer.setName("Teo Hut Kim");
        officerRepository.save(officer);
        officer.setPf("1081");
        officer.setName("Janet Loh");
        officerRepository.save(officer);
        officer.setPf("1082");
        officer.setName("Tan Heng Heng");
        officerRepository.save(officer);
        officer.setPf("1083");
        officer.setName("Ong Lee Koon");
        officerRepository.save(officer);
        officer.setPf("1084");
        officer.setName("Tan Shu Mun");
        officerRepository.save(officer);
        officer.setPf("1085");
        officer.setName("Tay Ya Hui");
        officerRepository.save(officer);
        officer.setPf("105");
        officer.setName("Ang Ming See");
        officerRepository.save(officer);
        officer.setPf("106");
        officer.setName("Jamie Ho");
        officerRepository.save(officer);
        officer.setPf("107");
        officer.setName("Kelvin Tan Kay Si");
        officerRepository.save(officer);
        officer.setPf("108");
        officer.setName("John Ho");
        officerRepository.save(officer);
    }

    private void seedStations() {
        Station station = new Station();

        station.setName("Hong Kong");
        station.setStationCode("HOK");
        station.setValidFrom(LocalDate.now());
        station.setValidTo(LocalDate.now());
        stationRepository.save(station);
        station.setName("Kuala Lumpur");
        station.setStationCode("KUL");
        station.setValidFrom(LocalDate.now());
        station.setValidTo(LocalDate.now());
        stationRepository.save(station);
        station.setName("Jakarta");
        station.setStationCode("JKT");
        station.setValidFrom(LocalDate.now());
        station.setValidTo(LocalDate.now());
        stationRepository.save(station);
    }

    private void seedClaims() {
        Claim claim = new Claim();

        claim.setClaimDesc("Entertainment");
        claimRepository.save(claim);

        claim = new Claim();
        claim.setClaimDesc("Others");
        claimRepository.save(claim);
    }

    private void seedBanks() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBankName("Bank A");
        bankAccount.setAccountNo("1234567890");
        Station station = stationRepository.findById("HOK").get();
        bankAccount.setStation(station);
        bankAccountRepository.save(bankAccount);
        bankAccount = new BankAccount();
        bankAccount.setBankName("Bank B");
        bankAccount.setAccountNo("9876543210");
        station = stationRepository.findById("JKT").get();
        bankAccount.setStation(station);
        bankAccountRepository.save(bankAccount);
        bankAccount = new BankAccount();
        bankAccount.setBankName("Bank C");
        bankAccount.setAccountNo("6664201337");
        station = stationRepository.findById("KUL").get();
        bankAccount.setStation(station);
        bankAccountRepository.save(bankAccount);
    }

    private void seedPaymentVouchers() {
    }
}

