package com.karmi.project.Service;


import com.karmi.project.Interface.IRepairCenterService;
import com.karmi.project.entitie.MaterialUnderRepair;
import com.karmi.project.entitie.RepairCenter;
import com.karmi.project.entitie.State;
import com.karmi.project.repositories.MaterialUnderRepository;
import com.karmi.project.repositories.RepairCenterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.*;

@Slf4j
@Service
public class RepairCenterService implements IRepairCenterService {
    @Autowired
    private RepairCenterRepository repairCenterRepository;
    @Autowired
    MaterialUnderRepository materialUnderRepository;


    @Override
    public RepairCenter ajouterRepairCenter(RepairCenter repairCenter) {
        return repairCenterRepository.save(repairCenter);
    }
    @Override
    public RepairCenter updateRepairCenter(RepairCenter repairCenter) {
        repairCenterRepository.save(repairCenter);
        return repairCenter;
    }

    @Override
    public void removeRepaircenter(Integer code) {
        repairCenterRepository.deleteById(code);
    }

    @Override
    public List<RepairCenter> retrieveAllRepairCenter() {
        return repairCenterRepository.findAll();
    }

    @Override
    public int reteriveMaterialNotGet() {
        Map<String,Object> map=new HashMap<>();
        List<MaterialUnderRepair> materialUnderRepairs=materialUnderRepository.findAll();
        for(MaterialUnderRepair materialUnderRepair:materialUnderRepairs){

            if(materialUnderRepair.getState().equals(State.Not_yet)){
                map.put("liste",materialUnderRepair);

            }
        }
        return map.size();
    }
    @Override
    public RepairCenter addAndAffecter(RepairCenter repairCenter, Integer id) {
        MaterialUnderRepair  materialUnderRepair=materialUnderRepository.findById(id).get();
        if(Objects.nonNull(materialUnderRepair)){
            repairCenter.getMaterialUnderRepairSet().add(materialUnderRepair);
            return repairCenterRepository.save(repairCenter);
        }
       else return null;
    }
    public void   TraiterMaterial(int id){
        MaterialUnderRepair materialUnderRepair=materialUnderRepository.findById(id).get();

    }

}
