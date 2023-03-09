package com.karmi.project.repositories;

import com.karmi.project.entitie.MedicalStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalStaffRepository  extends JpaRepository<MedicalStaff,Long> {



   MedicalStaff findByEmail(String email);
}