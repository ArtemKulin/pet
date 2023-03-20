package com.vet24.web.controllers.qrcode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.models.dto.pet.PetContactDto;
import com.vet24.web.ControllerAbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;

@DBRider
public class PetContactQrCodeControllerTest extends ControllerAbstractIntegrationTest {

    final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    // get create qr code for petContact by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testCreateQrCodeForViewOnePetContactSuccess() throws Exception {
        final String URL_GET_PET_CONTACT = "/api/client/pet/103/qr";
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_GET_PET_CONTACT)).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    // get pet by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testCreateQrCodeForViewOnePetContactError404Pet() throws Exception {
        final String URL_GET_NOT_FOUND_ID_PET = "/api/client/pet/1000/qr";
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_GET_NOT_FOUND_ID_PET))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    // post update petContact by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testUpdatePetContactForPetSuccess() throws Exception {
        final String URL_POST_UPDATE = "/api/client/pet/103/qr";
        PetContactDto petContact1 = new PetContactDto("Мария", "Невского 17", "4854789899");
        String bodyUpdate = (new ObjectMapper()).valueToTree(petContact1).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_POST_UPDATE)
                .content(bodyUpdate).contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    // post create petContact by id - success
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testCreatePetContactForPetSuccess() throws Exception {
        final String URL_POST_CREATE = "/api/client/pet/106/qr";
        PetContactDto petContact2 = new PetContactDto("Мария", "Невского 17", "5647564343");
        String bodyCreate = (new ObjectMapper()).valueToTree(petContact2).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_POST_CREATE)
                .content(bodyCreate).contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    // post pet by id - error 404 pet not found
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/pet-contact.yml", "/datasets/user-entities.yml", "/datasets/pet-entities.yml"})
    public void testUpdateAndCreatePetContactForPetError404Pet() throws Exception {
        final String URL_POST_NOT_FOUND_ID_PET = "/api/client/pet/1000/qr";
        PetContactDto petContact3 = new PetContactDto("Мария", "Невского 17", "2456786957");
        String body = (new ObjectMapper()).valueToTree(petContact3).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_POST_NOT_FOUND_ID_PET)
                .content(body).contentType(APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
