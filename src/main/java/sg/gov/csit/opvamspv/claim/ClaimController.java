package sg.gov.csit.opvamspv.claim;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClaimController {
    private final ClaimRepository claimRepository;

    public ClaimController(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @GetMapping("/api/v1/Claims/{claimId}")
    public Claim getClaim(@PathVariable Long claimId) {
        return claimRepository.getOne(claimId);
    }

    @GetMapping("/api/v1/Claims")
    public List<Claim> getClaims() {
        return claimRepository.findAll();
    }

    @PutMapping("/api/v1/Claims/{claimId}")
    public Claim updateClaim(@PathVariable Long claimId, @RequestBody Claim claim) {
        return claimRepository.save(claim);
    }

    @PostMapping("/api/v1/Claims")
    public List<Claim> createClaims(@RequestBody List<Claim> claims) {
        return claimRepository.saveAll(claims);
    }

    @DeleteMapping("/api/v1/Claims/{claimId}")
    public void deleteClaim(@PathVariable Long claimId) {
        claimRepository.deleteById(claimId);
    }

}
