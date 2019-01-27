package sg.gov.csit.opvamspv.bankaccount;

import org.springframework.web.bind.annotation.*;
import sg.gov.csit.opvamspv.exception.ResourceNotFoundException;
import sg.gov.csit.opvamspv.station.Station;
import sg.gov.csit.opvamspv.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BankAccountController {
    private final BankAccountRepository bankAccountRepository;
    private final StationRepository stationRepository;

    public BankAccountController(BankAccountRepository bankAccountRepository, StationRepository stationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.stationRepository = stationRepository;
    }

    @GetMapping("/api/v1/BankAccounts/{bankAccountId}")
    public BankAccountDto getBankAccount(@PathVariable Long bankAccountId) {
        return convertToDto(bankAccountRepository.getOne(bankAccountId));
    }

    @GetMapping("/api/v1/BankAccounts")
    public List<BankAccountDto> getBankAccounts() {
        return bankAccountRepository
                .findAll()
                .stream()
                .map(BankAccountController::convertToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/api/v1/BankAccounts/{bankAccountId}")
    public BankAccountDto updateBankAccount(@RequestBody BankAccountDto account) {
        String stationCode = account.getStationCode();
        Station station = stationRepository
                .findById(stationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Station of " + stationCode + " not found"));

        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(account.getId());
        bankAccount.setBankName(account.getBankName());
        bankAccount.setAccountNo(account.getAccountNo());
        bankAccount.setStation(station);

        return convertToDto(bankAccountRepository.save(bankAccount));
    }

    @PostMapping("/api/v1/BankAccounts")
    public BankAccountDto createBankAccount(@RequestBody BankAccountDto account) {
        String stationCode = account.getStationCode();
        Station station = stationRepository
                .findById(stationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Station of " + stationCode + " not found"));

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBankName(account.getBankName());
        bankAccount.setAccountNo(account.getAccountNo());
        bankAccount.setStation(station);

        return convertToDto(bankAccountRepository.save(bankAccount));
    }

    @DeleteMapping("/api/v1/BankAccounts/{bankAccountId}")
    public void deleteBankAccount(@PathVariable Long bankAccountId) {
        bankAccountRepository.deleteById(bankAccountId);
    }

    // TODO: Consider using model mapper in Maven
    private static BankAccountDto convertToDto(BankAccount bankAccount) {
        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setId(bankAccount.getId());
        bankAccountDto.setAccountNo(bankAccount.getAccountNo());
        bankAccountDto.setBankName(bankAccount.getBankName());
        bankAccountDto.setStationCode(bankAccount.getStation().getStationCode());

        return bankAccountDto;
    }
}
