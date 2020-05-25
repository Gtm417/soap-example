package com.example.endpoint;

import com.example.entity.Doctor;
import com.example.exception.DoctorNotFoundException;
import com.example.repository.DoctorRepository;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import tutorial.soapservice.*;

@Endpoint
public class DoctorEndpoint {
    private static final String NAMESPACE_URI = "http://soapService.tutorial";

    private final DoctorRepository doctorRepository;

    public DoctorEndpoint(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDoctorRequest")
    @ResponsePayload
    public GetDoctorResponse getDoctor(@RequestPayload GetDoctorRequest request) {
        ObjectFactory objectFactory = new ObjectFactory();
        GetDoctorResponse response = objectFactory.createGetDoctorResponse();

        Doctor doctorFromDb = doctorRepository.findById(request.getId())
                .orElseThrow(DoctorNotFoundException::new);

        tutorial.soapservice.Doctor doctor  = objectFactory.createDoctor();
        doctor.setId(doctorFromDb.getId());
        doctor.setName(doctorFromDb.getName());
        doctor.setLicense(doctorFromDb.getLicense());
        doctor.setSpecialization(doctorFromDb.getSpecialization());
        response.setDoctor(doctor);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addDoctorRequest")
    @ResponsePayload
    public AddDoctorResponse addDoctor(@RequestPayload AddDoctorRequest request) {
        ObjectFactory objectFactory = new ObjectFactory();
        AddDoctorResponse response = objectFactory.createAddDoctorResponse();
        Doctor doctor = doctorRepository.save(mapDoctorFromRequest(request));
        response.setId(doctor.getId());
        return response;
    }

    private Doctor mapDoctorFromRequest(AddDoctorRequest request) {
        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setLicense(request.getLicense());
        doctor.setSpecialization(request.getSpecialization());
        return doctor;
    }


}
