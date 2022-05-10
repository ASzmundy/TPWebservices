
package com.insa.fr.services;

import com.insa.fr.entity.Logs;
import com.insa.fr.entity.Patient;
import com.insa.fr.entity.Sejour;
import com.insa.fr.entity.Students;
import java.util.List;
import java.util.Map;

/**
 *
 * @author herve
 */
public interface Services_Interface {
    
   public boolean setLogs(Logs log);
   public List<Patient> getPatient(int id);
}
