package sg.gov.csit.opvamspv.bankaccount;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BankAccountController {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountController(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
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
    public BankAccount updateBankAccount(@RequestBody BankAccount account) {
        return bankAccountRepository.save(account);
    }

    @PostMapping("/api/v1/BankAccounts")
    public BankAccount createBankAccount(@RequestBody BankAccount account) {
        return bankAccountRepository.save(account);
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
