package com.insa.fr.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;


/**
 *
 * @author Alexis
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request
{
    String RequestType;
    String IPP;
    String NOM;
    String PRENOM;
    String nvnom;
    String nvprenom;
    String nvddn;
    String INTIT;
    String NOMMAR;
    String DDN;
    String SEXE;
    String ADR1;
    String ADR2;
    String CP;
    String VILLE;
    String PAYS;
    String TEL;
    String PAYSN;
    String DDS;
    String UF;
    String CHAMBRE;
    String LIT;
    String UFMED;
    String NUMPAS;
    String DFS;

    public Optional<String> getIPP() {
        return Optional.ofNullable(IPP);
    }

}
