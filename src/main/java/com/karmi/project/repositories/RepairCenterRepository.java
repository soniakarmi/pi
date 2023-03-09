package com.karmi.project.repositories;

import com.karmi.project.entitie.RepairCenter;
import com.karmi.project.entitie.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RepairCenterRepository  extends JpaRepository<RepairCenter,Integer> {

//@Query("Select r From RepairCenter r  where r.State=?Not_yet ")
//List<RepairCenter> retrieveRByState( );

  //  @Modifying
    //@Transactional
    //@Query("DELETE FROM RepairCenter R WHERE R.State =:state")
    //void deleteRepairCenterByState(@Param("state") State state);
}

