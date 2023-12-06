package si.rso.banksimulator.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import si.rso.banksimulator.dtos.CustomerDTO;
import si.rso.banksimulator.entities.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO customerToCustomerDTO(Customer customer);

    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "contact", ignore = true)
    void mapToCustomer(CustomerDTO customerDTO, @MappingTarget Customer customer);

    void mapToCustomerDTO(Customer customer, @MappingTarget CustomerDTO customerDTO);
}
