package com.vet24.web.controllers.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.models.dto.user.ClientDto;
import com.vet24.models.mappers.user.ClientMapper;
import com.vet24.service.user.ClientService;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DBRider
public class ClientControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private ClientController controller;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientService clientService;

    private final String URI = "http://localhost:8090/api/client";

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void getCurrentClient() {
        ClientDto clientDto = clientMapper.clientToClientDto(clientService.testGetCurrentClientEagerly());
        ResponseEntity<ClientDto> response = testRestTemplate
                .getForEntity(URI, ClientDto.class);

        assertThat(clientDto).isNotNull();
        assertEquals(clientDto, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void getClientAvatar() {
        persistClientAvatar();
        ResponseEntity<byte[]> response = testRestTemplate
                .getForEntity(URI + "/avatar", byte[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void persistClientAvatar() {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new ClassPathResource("test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange(URI + "/avatar", HttpMethod.POST, entity, String.class, 3);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}