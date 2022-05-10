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
public class ModRequest
{
    private String IPP;//
    private String NOM;//
    private String PRENOM;//
    private String nvnom;
    private String nvprenom;
    private String nvddn;
    private String INTIT;
    private String NOMMAR;//
    private String DDN;//
    private String SEXE;//
    private String ADR1;
    private String ADR2;
    private String CP;
    private String VILLE;
    private String PAYS;
    private String TEL;//
    private String PAYSN;

}
