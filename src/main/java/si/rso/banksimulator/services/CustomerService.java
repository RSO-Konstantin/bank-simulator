package si.rso.banksimulator.services;

import io.micrometer.core.instrument.util.StringUtils;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import si.rso.banksimulator.dtos.CustomerDTO;
import si.rso.banksimulator.entities.Bank;
import si.rso.banksimulator.entities.Customer;
import si.rso.banksimulator.mappers.CustomerMapper;
import si.rso.banksimulator.repositories.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    private final BankService bankService;

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO findByUuidDTO(UUID uuid) {
        return customerMapper.customerToCustomerDTO(
                customerRepository
                        .findCustomerByUuid(uuid)
                        .orElseThrow(
                                () ->
                                        new ElementNotFoundException(
                                                "Customer with uuid " + uuid + " not exists.")));
    }

    public Customer findByUuid(UUID uuid) {
        return customerRepository
                .findCustomerByUuid(uuid)
                .orElseThrow(
                        () ->
                                new ElementNotFoundException(
                                        "Customer with uuid " + uuid + " not exists."));
    }

    public CustomerDTO findByEmailDTO(String email) {
        Customer customer =
                customerRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new ElementNotFoundException(
                                                "Customer with email " + email + " not found."));
        return customerMapper.customerToCustomerDTO(customer);
    }

    public Customer findByEmail(String email) {
        Customer customer =
                customerRepository
                        .findByEmail(email)
                        .orElseThrow(
                                () ->
                                        new ElementNotFoundException(
                                                "Customer with email " + email + " not found."));
        return customer;
    }

    public Boolean checkIfExistsCustomerByEmailAndBank(String email, Bank bank) {
        return customerRepository.existsCustomerByEmailAndBank(email, bank);
    }

    public void validate(CustomerDTO customerDTO) {
        if (StringUtils.isBlank(customerDTO.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name");
        if (StringUtils.isBlank(customerDTO.getSurname()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid surname");
        if (StringUtils.isBlank(customerDTO.getEmail()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        if (customerDTO.getBalance() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid balance");
        if (customerDTO.getSuspenseBalance() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid suspense balance");
    }
}
