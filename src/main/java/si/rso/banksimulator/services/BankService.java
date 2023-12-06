package si.rso.banksimulator.services;

import io.micrometer.core.instrument.util.StringUtils;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import si.rso.banksimulator.dtos.BankDTO;
import si.rso.banksimulator.entities.Bank;
import si.rso.banksimulator.mappers.BankMapper;
import si.rso.banksimulator.repositories.BankRepository;

@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

    public List<BankDTO> getAllBanks() {
        return bankRepository.findAll().stream()
                .map(bankMapper::bankToBankDTO)
                .collect(Collectors.toList());
    }

    public BankDTO findByUuid(UUID uuid) {
        return bankMapper.bankToBankDTO(
                bankRepository
                        .findBankByUuid(uuid)
                        .orElseThrow(
                                () ->
                                        new ElementNotFoundException(
                                                "Bank with uuid " + uuid + " not exists.")));
    }

    public Bank findByBic(String bic) {
        return bankRepository
                .findBankByBic(bic)
                .orElseThrow(
                        () ->
                                new ElementNotFoundException(
                                        "Bank with bic " + bic + " not exists."));
    }

    public List<BankDTO> getBankByNameContaining(String name) {
        return Optional.of(
                        bankRepository.findByBankNameContaining(name).stream()
                                .map(bankMapper::bankToBankDTO)
                                .collect(Collectors.toList()))
                .filter(list -> !list.isEmpty())
                .orElseThrow(
                        () ->
                                new ElementNotFoundException(
                                        "Bank with name " + name + " not found."));
    }

    public void validate(BankDTO bankDTO) {
        if (StringUtils.isBlank(bankDTO.getBankName()) || bankDTO.getBankName().length() > 255)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name");
        if (StringUtils.isBlank(bankDTO.getBic()) || bankDTO.getBic().length() > 11)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid bic code");
        if (StringUtils.isBlank(bankDTO.getCountry()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid country name");
        if (StringUtils.isBlank(bankDTO.getCurrency()) || bankDTO.getCurrency().length() > 3)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid currency code");
    }
}
