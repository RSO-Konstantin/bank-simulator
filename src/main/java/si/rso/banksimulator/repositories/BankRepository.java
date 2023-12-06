package si.rso.banksimulator.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.rso.banksimulator.entities.Bank;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    Optional<Bank> findBankByUuid(UUID uuid);

    Optional<Bank> findBankByBic(String bic);

    List<Bank> findByBankNameContaining(String bankName);
}
