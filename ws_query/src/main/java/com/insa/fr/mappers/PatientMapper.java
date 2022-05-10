package com.insa.fr.mappers;

import com.insa.fr.entity.Patient;
import com.insa.fr.entity.Students;
import java.util.Map;

/**********************
 *
 * @author alexis
 **********************/
public class PatientMapper

{

    /*idutilisateur, refad, nom, prenom, droit, codeuf, titre, login, motdepasse, loginad, actif, typeconnection*/
public Patient mapping(Patient tpat, Map<String,Object> row)
{
            tpat.setIPP(Integer.parseInt(row.get("patientid").toString(),10));
            tpat.setNOM(row.get("nom").toString());
            tpat.setPRENOM(row.get("prenom").toString());
            tpat.setINTIT(row.get("civilite").toString());
            tpat.setNOMMAR(row.get("nomusage").toString());
            tpat.setDDN(row.get("datedenaissance").toString());
            tpat.setSEXE(row.get("sexe").toString());
            tpat.setADR1(row.get("adresse1").toString());
            tpat.setADR2(row.get("adresse2").toString());
            tpat.setCP(row.get("codepostal").toString());
            tpat.setVILLE(row.get("ville").toString());
            tpat.setPAYS(row.get("pays").toString());
            tpat.setTEL(row.get("telephone").toString());
            tpat.setPAYSN(row.get("paysnaissance").toString());

    return tpat;
}
    
}
