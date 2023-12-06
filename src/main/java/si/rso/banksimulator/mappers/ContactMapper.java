package si.rso.banksimulator.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import si.rso.banksimulator.dtos.ContactDTO;
import si.rso.banksimulator.entities.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactDTO contactToContactDTO(Contact contact);

    Contact contactDTOToContact(ContactDTO contactDTO);

    @Mapping(target = "transaction", ignore = true)
    void mapToContact(ContactDTO contactDTO, @MappingTarget Contact contact);

    void mapToContactDTO(Contact contact, @MappingTarget ContactDTO contactDTO);
}
