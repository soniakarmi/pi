package com.karmi.project.repositories;

import com.karmi.project.entitie.Holiday;
import com.karmi.project.entitie.MedicalStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday,Long> {
    Holiday findHolidayByMedicalStaff(MedicalStaff medicalStaff);
    Holiday findHolidayByMedicalStaffIdStaff(Long idStaff);
    List<Holiday> findByType(String type);


    @Modifying
    @Transactional

    @Query("DELETE FROM Holiday h WHERE h.endDate=:endDate")
    void deleteHolidayByendDate(@Param("endDate") LocalDate endDate);

    List<Holiday> findHolidayByMedicalStaffIdStaffAndStartDateAndAndEndDate(long idStaff,LocalDate startDate,LocalDate  endDate);
    List<Holiday> findByMedicalStaff(MedicalStaff medicalStaff);



}
