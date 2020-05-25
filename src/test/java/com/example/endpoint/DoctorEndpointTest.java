package com.example.endpoint;

import com.example.exception.DoctorNotFoundException;
import com.example.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tutorial.soapservice.AddDoctorRequest;
import tutorial.soapservice.AddDoctorResponse;
import tutorial.soapservice.GetDoctorRequest;
import tutorial.soapservice.GetDoctorResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorEndpointTest {
    private static final int ID = 1;
    private static final String NAME = "name";
    private static final String LICENSE = "License";
    private static final String SPECIALIZATION = "Specialization";
    private static final com.example.entity.Doctor DOCTOR_ENTITY = new com.example.entity.Doctor(ID, NAME, LICENSE, SPECIALIZATION);


    @InjectMocks
    DoctorEndpoint endpoint;

    @Mock
    DoctorRepository repository;

    @Mock
    GetDoctorRequest request;

    @Mock
    AddDoctorRequest addDoctorRequest;

    @Test
    void getDoctorShouldReturnResponseIfSuchTeamExists() {
        when(request.getId()).thenReturn(ID);
        when(repository.findById(request.getId())).thenReturn(Optional.of(DOCTOR_ENTITY));

        GetDoctorResponse actual = endpoint.getDoctor(request);

        assertEquals(DOCTOR_ENTITY.getName(), actual.getDoctor().getName());
    }

    @Test
    void getDoctorShouldThrowsDoctorNotFoundIfDoctorNotExist() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> endpoint.getDoctor(request));
    }

    @Test
    void saveDoctorShouldReturnResponseWithId() {
        when(addDoctorRequest.getName()).thenReturn(NAME);
        when(addDoctorRequest.getLicense()).thenReturn(LICENSE);
        when(addDoctorRequest.getSpecialization()).thenReturn(SPECIALIZATION);
        when(repository.save(any(com.example.entity.Doctor.class))).thenReturn(DOCTOR_ENTITY);

        AddDoctorResponse actual = endpoint.addDoctor(addDoctorRequest);

        verify(repository).save(any(com.example.entity.Doctor.class));
        assertEquals(ID, actual.getId());
    }


    private tutorial.soapservice.Doctor getGeneratedDoctor() {
        tutorial.soapservice.Doctor doctor = new tutorial.soapservice.Doctor();
        doctor.setId(1);
        doctor.setName("name");
        doctor.setLicense("license");
        doctor.setSpecialization("specialization");
        return doctor;
    }
}
