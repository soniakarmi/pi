package com.karmi.project.repositories;

import com.karmi.project.entitie.MaterialUnderRepair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialUnderRepository  extends JpaRepository<MaterialUnderRepair,Integer> {
}
