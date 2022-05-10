
package com.insa.fr.services;

import com.insa.fr.entity.Logs;
import com.insa.fr.entity.Students;
import java.util.List;

/**
 *
 * @author herve
 */
public interface Services_Interface {
    
   public boolean setLogs(Logs log);
   public List<Students> getStudent(String id);
   public List<Students> getStudents(); 
   public boolean deleteStudent(String id);
   public int createStudent(Students stud);
   public boolean updateStudent(Students stud, String id);
}
