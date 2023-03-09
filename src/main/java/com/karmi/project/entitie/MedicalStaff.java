package com.karmi.project.entitie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MedicalStaff implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idStaff;
    private String firstName;
    private String lastName;
    private Integer nationalIdCard;
    private LocalDate dateOfBirth;
    private Integer phoneNumber;
    private String email;
    private String job;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "medicalStaff")
    private Set<Holiday> listHoliday=new HashSet<>();


}
