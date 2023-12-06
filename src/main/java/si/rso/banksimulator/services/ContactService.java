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
import si.rso.banksimulator.dtos.ContactDTO;
import si.rso.banksimulator.entities.Contact;
import si.rso.banksimulator.entities.Customer;
import si.rso.banksimulator.mappers.ContactMapper;
import si.rso.banksimulator.repositories.ContactRepository;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    private final CustomerService customerService;

    public List<ContactDTO> getAllContacts() {
        return contactRepository.findAll().stream()
                .map(contactMapper::contactToContactDTO)
                .collect(Collectors.toList());
    }

    public List<ContactDTO> getContactByNameContaining(String name) {
        return Optional.of(
                        contactRepository.findByNameContaining(name).stream()
                                .map(contactMapper::contactToContactDTO)
                                .collect(Collectors.toList()))
                .filter(list -> !list.isEmpty())
                .orElseThrow(
                        () ->
                                new ElementNotFoundException(
                                        "Contact with name " + name + " not found."));
    }

    public ContactDTO findByUuid(UUID uuid) {
        return contactMapper.contactToContactDTO(
                contactRepository
                        .findContactByUuid(uuid)
                        .orElseThrow(
                                () ->
                                        new ElementNotFoundException(
                                                "Contact with uuid " + uuid + " not exists.")));
    }

    public Contact findByCustomerAndEmail(Customer customer, String email) {
        return contactRepository
                .findByCustomerUuidAndEmail(customer.getUuid(), email)
                .orElseThrow(
                        () ->
                                new ElementNotFoundException(
                                        "Contact with email " + email + " not found."));
    }

    public void validate(ContactDTO contactDTO) {
        if (StringUtils.isBlank(contactDTO.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name");
        if (StringUtils.isBlank(contactDTO.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
    }
}
