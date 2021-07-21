package com.severo.personapi.controller;

import com.severo.personapi.dto.reponse.MessageResponseDTO;
import com.severo.personapi.dto.request.PersonDTO;
import com.severo.personapi.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.severo.personapi.utils.PersonUtils.asJsonString;
import static com.severo.personapi.utils.PersonUtils.createFakeDTO;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    private static final String PERSON_API_URL_PATH = "/api/v1/person";

    private MockMvc mockMvc;

    private PersonController personController;

    @Mock
    private PersonService personService;

    @BeforeEach
    void setup() {
        personController = new PersonController(personService);
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void testWhenPOSTIsCalledThenPersonShouldBeCreated() throws Exception {
        PersonDTO expectedPersonDTO = createFakeDTO();
        MessageResponseDTO expectedResponseMessage = createMessageResponse("Created person with ID", 1L);

        when(personService.create(expectedPersonDTO)).thenReturn(expectedResponseMessage);

        mockMvc.perform(post(PERSON_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(expectedPersonDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
    }

    private MessageResponseDTO createMessageResponse(String message, Long id) {
        return MessageResponseDTO.builder()
                .message(message + id)
                .build();
    }
}
