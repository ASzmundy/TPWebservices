package com.insa.fr.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    String NOM;
    String PRENOM;
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
}
