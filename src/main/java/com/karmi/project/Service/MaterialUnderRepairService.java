package com.karmi.project.Service;

import com.karmi.project.Interface.IMaterialUnderService;
import com.karmi.project.entitie.MaterialUnderRepair;
import com.karmi.project.repositories.MaterialUnderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MaterialUnderRepairService implements IMaterialUnderService {

    @Autowired
    MaterialUnderRepository materialUnderRepository;

    @Override
    public void ajouterMaterialUnderRepair(MaterialUnderRepair materialUnderRepair) {

    }

    @Override
    public int reteriveMaterialNotGet() {
        return 0;
    }
}