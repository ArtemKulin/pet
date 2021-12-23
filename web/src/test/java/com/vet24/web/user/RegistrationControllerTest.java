package com.vet24.web.user;


import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import com.vet24.models.dto.user.RegisterDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@DBRider
public class RegistrationControllerTest extends ControllerAbstractIntegrationTest {

    final String URI = "http://localhost:8090/api/registration";

    private RegisterDto registerDto;
    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity<RegisterDto> entity;

    @Before
    public void createNewRegisterDto() {
        RegisterDto registerDto = new RegisterDto("342354234.com","Vera","P",
                "Congo","Congo");
        entity = new HttpEntity<>(registerDto, headers);
    }

    @Test
    @DataSet(value = "/datasets/registration.yml", cleanBefore = true)
    public void shouldBeNotAcceptableWrongEmail() {
        ResponseEntity<RegisterDto> responseEntity = testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DataSet(value = "/datasets/registration.yml", cleanBefore = true)
    public void shouldBeNotAcceptablePasswords(){
        ResponseEntity<RegisterDto> responseEntity = testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DataSet(value = "/datasets/registration.yml", cleanBefore = true)
    public void shouldBeCreated()  {
        RegisterDto rightDto = new RegisterDto("gkdsf@mail.ru", "lol", "lol", "passss", "passss");
        entity = new HttpEntity<>(rightDto);
        ResponseEntity<RegisterDto> responseEntity = testRestTemplate
                .exchange(URI, HttpMethod.POST, entity, RegisterDto.class);
        Assert.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}
