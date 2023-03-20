package com.vet24.web.medicine;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.vet24.dao.medicine.MedicineDaoImpl;
import com.vet24.models.dto.medicine.MedicineDto;
import com.vet24.models.mappers.medicine.MedicineMapper;
import com.vet24.models.medicine.Medicine;
import com.vet24.web.ControllerAbstractIntegrationTest;
import com.vet24.web.controllers.medicine.MedicineController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DBRider
public class MedicineControllerTest extends ControllerAbstractIntegrationTest {

    @Autowired
    private MedicineMapper medicineMapper;
    @Autowired
    private MedicineController medicineController;
    @Autowired
    private MedicineDaoImpl medicineDao;
    private final String URI = "http://localhost:8090/api/manager/medicine";
    private HttpHeaders headers = new HttpHeaders();
    private Medicine medicine;
    private MedicineDto medicineDto;

    @BeforeEach
    public void createNewMedicineAndMedicineDto() {
        medicine = new Medicine("daulet", "jm", "dsad", "test");
        medicine.setId(1L);
        medicineDto = medicineMapper.medicineToMedicineDto(medicine);
    }

    //test controller exist
    @Test
    public void shouldBeMedicineController() throws Exception {
        assertThat(medicineController).isNotNull();
    }

    //get medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/medicine.yml"})
    public void shouldBeGetMedicineById() throws Exception {
        Medicine medicine = medicineDao.getByKey(100L);
        ResponseEntity<MedicineDto> response = testRestTemplate
                .getForEntity(URI + "/{id}", MedicineDto.class, 100);

        assertThat(medicine).isNotNull();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //add medicine
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/medicine.yml"})
    public void shouldBeAddMedicine() throws URISyntaxException {
        List<Medicine> medicineListBefore = medicineDao.getAll();
        int countRow = medicineListBefore.size();
        Medicine medicine = new Medicine("tetetete", "etetete", "ttrtrt", "ttrrtr");
        medicine.setId(3L);
        MedicineDto medicineDto = medicineMapper.medicineToMedicineDto(medicine);
        HttpEntity<MedicineDto> request = new HttpEntity<>(medicineDto, headers);
        ResponseEntity<MedicineDto> response = testRestTemplate
                .postForEntity(URI, request, MedicineDto.class);
        List<Medicine> medicineList = medicineDao.getAll();
        int resultRow = medicineList.size();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(++countRow, resultRow);
    }

    //put medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/medicine.yml"})
    public void shouldBeUpdateMedicineById() throws Exception {
        medicineDto.setId(101L);
        HttpEntity<MedicineDto> entity = new HttpEntity<>(medicineDto, headers);
        ResponseEntity<MedicineDto> response =  testRestTemplate
                .exchange(URI + "/{id}", HttpMethod.PUT, entity, MedicineDto.class, 101);
        Medicine updateMedicine = medicineDao.getByKey(101L);
        medicine.setId(101L);
        assertEquals(medicine, updateMedicine);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //upload icon for medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/medicine.yml"})
    public void shouldBeUpdateMedicineIcon() throws Exception {
        LinkedMultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("file", new org.springframework.core.io.ClassPathResource("test.png"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(parameters, headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange(URI + "/{id}/set-pic", HttpMethod.POST, entity, String.class, 100);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //get icon for medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/medicine.yml"})
    public void shouldBeGetMedicineIconById() throws Exception {
        shouldBeUpdateMedicineIcon();
        ResponseEntity<byte[]> response = testRestTemplate
                .getForEntity(URI + "/{id}/set-pic", byte[].class, 100);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //delete medicine by id
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/medicine.yml"})
    public void shouldBeDeleteMedicine() throws Exception {
        List<Medicine> medicineListBefore = medicineDao.getAll();
        int countRow = medicineListBefore.size();
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response =  testRestTemplate
                .exchange(URI + "/{id}", HttpMethod.DELETE, entity, Void.class, 100);
        List<Medicine> medicineListAfter = medicineDao.getAll();
        int resultRow = medicineListAfter.size();
        assertEquals(--countRow, resultRow);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    //test search medicine
    @Test
    @DataSet(cleanBefore = true, value = {"/datasets/medicine.yml"})
    public void shouldBeSearchMedicine() throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URI + "/search")
                .queryParam("manufactureName")
                .queryParam("name")
                .queryParam("searchText", "java");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = testRestTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
