package sg.gov.csit.opvamspv.bankaccount;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountController {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountController(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @GetMapping("/api/v1/BankAccounts/{bankAccountId}")
    public BankAccount getBankAccount(@PathVariable Long bankAccountId) {
        return bankAccountRepository.getOne(bankAccountId);
    }

    @GetMapping("/api/v1/BankAccounts")
    public List<BankAccount> getBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @PostMapping("/api/v1/BankAccounts")
    public BankAccount createBankAccount(@RequestBody BankAccount account) {
        return bankAccountRepository.save(account);
    }

    @DeleteMapping("/api/v1/BankAccounts/{bankAccountId}")
    public void deleteBankAccount(@PathVariable Long accountId) {
        bankAccountRepository.deleteById(accountId);
    }

}
