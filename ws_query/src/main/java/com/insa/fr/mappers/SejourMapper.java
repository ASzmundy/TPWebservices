package com.insa.fr.mappers;

import com.insa.fr.entity.Patient;
import com.insa.fr.entity.Sejour;

import java.util.Map;

public class SejourMapper {
    /**********************
     *
     * @author alexis
     **********************/

    /*idutilisateur, refad, nom, prenom, droit, codeuf, titre, login, motdepasse, loginad, actif, typeconnection*/
    public Sejour mapping(Sejour tsej, Map<String, Object> row) {
        tsej.setIEP(Integer.parseInt(row.get("sejourid").toString(), 10));
        tsej.setIPP(Integer.parseInt(row.get("patientid").toString(), 10));
        tsej.setDDS(row.get("datedesejour").toString());
        tsej.setUF(row.get("uf").toString());
        tsej.setCHAMBRE(Integer.parseInt(row.get("chambre").toString(), 10));
        tsej.setLIT(Integer.parseInt(row.get("lit").toString(), 10));
        tsej.setUFMED(row.get("ufmedecin").toString());

        return tsej;
    }
}
