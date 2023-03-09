package com.karmi.project.Interface;


import com.karmi.project.entitie.RepairCenter;

import java.util.List;

public interface IRepairCenterService {
    public RepairCenter ajouterRepairCenter (RepairCenter repairCenter);
    public RepairCenter updateRepairCenter (RepairCenter repairCenter);
    public void removeRepaircenter(Integer code);
    public List<RepairCenter> retrieveAllRepairCenter();
    public int reteriveMaterialNotGet();
    public RepairCenter addAndAffecter(RepairCenter repairCenter,Integer id);
}
