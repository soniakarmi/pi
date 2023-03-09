package com.karmi.project.Controlleur;

import com.karmi.project.Interface.IMaterialUnderService;
import com.karmi.project.Interface.IRepairCenterService;
import com.karmi.project.entitie.RepairCenter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/repaircenter")
public class RepairCenterControlleur {
    @Autowired
    IRepairCenterService RepairCenterService;

    @PostMapping("/add_repaircenter")
    public RepairCenter addrepaircenter(@RequestBody RepairCenter repairCenter) {

        return RepairCenterService.ajouterRepairCenter(repairCenter);
    }

    @PutMapping("/update")
    public RepairCenter updateRepaircenter(@RequestBody RepairCenter repairCenter) {
        return RepairCenterService.updateRepairCenter(repairCenter);
    }

    @DeleteMapping("/deleteR/{code}")
    public String removeRepairCenter(@PathVariable("code") int coderepaircenter) {
        RepairCenterService.removeRepaircenter(coderepaircenter);
        return "delete success";
    }

    @GetMapping("/getAllRepaircenter")
    public List<RepairCenter> retrieveAllRepaircenters() {
        return RepairCenterService.retrieveAllRepairCenter();
    }

    @Autowired
    IMaterialUnderService materialUnderService;

    @GetMapping("/reteriveMaterialNotGet")

    public int reteriveMaterialNotGet() {
        return materialUnderService.reteriveMaterialNotGet();

    }
}