package com.severo.personapi.service;

import com.severo.personapi.dto.reponse.MessageResponseDTO;
import com.severo.personapi.dto.request.PersonDTO;
import com.severo.personapi.entity.Person;
import com.severo.personapi.exception.PersonNotFoundException;
import com.severo.personapi.mapper.PersonMapper;
import com.severo.personapi.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.severo.personapi.utils.PersonUtils.createFakeDTO;
import static com.severo.personapi.utils.PersonUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonService personService;

    @Test
    void testGivenPersonDTOThenReturnSuccessSavedMessage() {
        PersonDTO personDTO = createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();

        when(personRepository.save(any(Person.class))).thenReturn(expectedSavedPerson);

        MessageResponseDTO expectedSuccessMessage = createExpectedMessageResponse(expectedSavedPerson.getId());
        MessageResponseDTO successMessage = personService.create(personDTO);

        assertEquals(expectedSuccessMessage, successMessage);
    }

    @Test
    void testGivenValidPersonIdThenReturnThisPerson() throws PersonNotFoundException {
        PersonDTO expectedPersonDTO = createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();
        expectedPersonDTO.setId(expectedSavedPerson.getId());

        when(personRepository.findById(expectedSavedPerson.getId())).thenReturn(Optional.of(expectedSavedPerson));
        when(personMapper.toDTO(expectedSavedPerson)).thenReturn(expectedPersonDTO);

        PersonDTO personDTO = personService.findById(expectedSavedPerson.getId());

        assertEquals(expectedPersonDTO, personDTO);
        assertEquals(expectedSavedPerson.getId(), personDTO.getId());
        assertEquals(expectedSavedPerson.getFirstName(), personDTO.getFirstName());
    }

    @Test
    void testGivenInvalidPersonIdThenReturnThrowException() {
        var invalidId = 1L;
        when(personRepository.findById(invalidId)).thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.findById(invalidId));
    }

    @Test
    void testGivenNoDataThenReturnAllPersonSaved() {
        List<Person> expectedSavedPersons = Collections.singletonList(createFakeEntity());
        PersonDTO personDTO = createFakeDTO();

        when(personRepository.findAll()).thenReturn(expectedSavedPersons);
        when(personMapper.toDTO(any(Person.class))).thenReturn(personDTO);

        List<PersonDTO> expectedPersonsDTOList = personService.listAll();

        assertFalse(expectedPersonsDTOList.isEmpty());
        assertEquals(expectedPersonsDTOList.get(0).getId(), personDTO.getId());
    }

    private MessageResponseDTO createExpectedMessageResponse(Long id) {
        return MessageResponseDTO
                .builder()
                .message("Created person with ID " + id)
                .build();
    }
}
