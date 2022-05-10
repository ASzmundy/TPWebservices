package com.insa.fr.services;

import com.insa.fr.entity.Logs;
import com.insa.fr.entity.Students;
import com.insa.fr.logging.LogLevel;
import com.insa.fr.logging.LogType;
import com.insa.fr.mappers.StudentMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author herve
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
    
}
