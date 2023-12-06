package si.rso.banksimulator.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import si.rso.banksimulator.dtos.TransactionDTO;
import si.rso.banksimulator.entities.Customer;
import si.rso.banksimulator.entities.Transaction;
import si.rso.banksimulator.enums.TransactionStatus;
import si.rso.banksimulator.mappers.TransactionMapper;
import si.rso.banksimulator.repositories.CustomerRepository;
import si.rso.banksimulator.repositories.TransactionRepository;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    private final ContactService contactService;

    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Transactional
    public Transaction internalTransaction(
            Customer source, String targetEmail, Transaction transaction) {
        Customer target = customerService.findByEmail(targetEmail);
        source.setSuspenseBalance(
                source.getSuspenseBalance().subtract(transaction.getSourceAmount()));
        target.setBalance(target.getBalance().add(transaction.getTargetAmount()));
        transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        customerRepository.save(source);
        customerRepository.save(target);
        transactionRepository.save(transaction);

        return transaction;
    }

    public void completeTransaction(UUID transactionUuid, String bankBIC) {
        final Transaction toComplete = findTransactionByUuid(transactionUuid);
        final Customer target = customerService.findByEmail(toComplete.getContact().getEmail());
        target.setBalance(target.getBalance().add(toComplete.getTargetAmount()));

        customerRepository.save(target);

        String dataToSend = transactionUuid.toString() + " " + bankBIC;
    }

    public void finalizeCompletedTransaction(UUID transactionUuid, String bankBIC) {
        final Transaction toFinalize = findTransactionByUuid(transactionUuid);
        final Customer source = toFinalize.getCustomer();
        toFinalize.setTransactionStatus(TransactionStatus.COMPLETED);
        source.setSuspenseBalance(
                source.getSuspenseBalance().subtract(toFinalize.getSourceAmount()));

        transactionRepository.save(toFinalize);
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::transactionToTransactionDTO)
                .collect(Collectors.toList());
    }

    public Transaction findTransactionByUuid(UUID uuid) {
        return transactionRepository
                .findTransactionByUuid(uuid)
                .orElseThrow(
                        () ->
                                new ElementNotFoundException(
                                        "Transaction with uuid " + uuid + " not exists."));
    }

    public TransactionDTO findTransactionByUuidDTO(UUID uuid) {
        return transactionMapper.transactionToTransactionDTO(
                transactionRepository
                        .findTransactionByUuid(uuid)
                        .orElseThrow(
                                () ->
                                        new ElementNotFoundException(
                                                "Transaction with uuid " + uuid + " not exists.")));
    }

    public List<TransactionDTO> getTransactionByStatus(TransactionStatus transactionStatus) {
        return Optional.of(
                        transactionRepository.findByTransactionStatus(transactionStatus).stream()
                                .map(transactionMapper::transactionToTransactionDTO)
                                .collect(Collectors.toList()))
                .filter(list -> !list.isEmpty())
                .orElseThrow(
                        () ->
                                new ElementNotFoundException(
                                        "Transaction with status "
                                                + transactionStatus
                                                + " not found."));
    }
}
