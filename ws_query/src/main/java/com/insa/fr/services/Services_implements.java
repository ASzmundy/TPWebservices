package com.insa.fr.services;

import com.insa.fr.entity.Logs;
import com.insa.fr.entity.Patient;
import com.insa.fr.entity.Sejour;
import com.insa.fr.logging.LogLevel;
import com.insa.fr.logging.LogType;
import com.insa.fr.mappers.PatientMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.insa.fr.mappers.SejourMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author alexis
 */
@Service
public class Services_implements implements Services_Interface {
    
    //autowired le service jdbc
    @Autowired
    JdbcTemplate jdbctemplate=new JdbcTemplate();
    
    @Autowired
    LogService logService;
    
    
    /****************************************
     * Un service
     * @param log
     * @return 
     *
     ****************************************/
    @Override
     public boolean setLogs(Logs log)
     {
         try
         {
             //TODO
         }
         catch (DataAccessException dae)
         {
           logService.log(this.getClass().getName(), LogLevel.ERROR, LogType.TRANSACTION,dae.getStackTrace()[0].getMethodName(),dae.getMessage());  
         }
         return false;
     }
     
     /****************************************
      * Un service
      * @return 
      ****************************************/
    @Override
    public List<Patient> getPatient(int id) {
           List<Patient> lpat=new ArrayList<>();
        try
        {
            String sql="SELECT * FROM patient WHERE patientid="+id+"";
            List<Map<String, Object>> mpat=jdbctemplate.queryForList(sql);
        if (!mpat.isEmpty())
       {
           mpat.forEach(row -> {
               List<Sejour> lsej = new ArrayList<>();
               //pour chaque patient
               Patient pat=new Patient();
               String sejsql = "SELECT * FROM sejour WHERE patientid="+id+"";
               List<Map<String, Object>> msej=jdbctemplate.queryForList(sejsql);
               if(!msej.isEmpty()){
                   msej.forEach(sejrow ->{
                       Sejour sej = new Sejour();
                       lsej.add(new SejourMapper().mapping(sej, sejrow));
                   });
               }
               pat.setSejours(lsej);
               lpat.add(new PatientMapper().mapping(pat, row));
            });
       }
        }
        catch (DataAccessException dae)
        {
           logService.log(this.getClass().getName(), LogLevel.ERROR, LogType.TRANSACTION,dae.getStackTrace()[0].getMethodName(),dae.getMessage());
        }
        return lpat;
    }
}
