package si.rso.banksimulator.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import si.rso.banksimulator.dtos.BankDTO;
import si.rso.banksimulator.entities.Bank;

@Mapper(componentModel = "spring")
public interface BankMapper {

    BankDTO bankToBankDTO(Bank bank);

    Bank bankDTOToBank(BankDTO bankDTO);

    @Mapping(target = "customers", ignore = true)
    void mapToBank(BankDTO bankDTO, @MappingTarget Bank bank);

    void mapToBankDTO(Bank bank, @MappingTarget BankDTO bankDTO);
}
