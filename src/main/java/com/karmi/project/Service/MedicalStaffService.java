package com.karmi.project.Service;

import com.karmi.project.Interface.IMedicalStaffService;
import com.karmi.project.entitie.MedicalStaff;
import com.karmi.project.repositories.MedicalStaffRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MedicalStaffService implements IMedicalStaffService {
    @Autowired
    MedicalStaffRepository medicalStaffRepository;

    @Override
    public void ajouterMedicalStaff(MedicalStaff medicalStaff) {

    }
}
