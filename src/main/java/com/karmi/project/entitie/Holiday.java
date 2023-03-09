package com.karmi.project.entitie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Holiday implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long idHoliday;
    private LocalDate startDate;
    private LocalDate endDate;
    private String type;
    private Boolean isValid;
    @JsonIgnore
    @ManyToOne
    private MedicalStaff medicalStaff;


}
