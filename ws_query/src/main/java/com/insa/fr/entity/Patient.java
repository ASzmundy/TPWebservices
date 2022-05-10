package com.insa.fr.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 *
 * @author tondeur-h
 */
@Data
@NoArgsConstructor
@ToString
public
class Patient {
    private int IPP;//
    private String NOM;//
    private String PRENOM;//
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

    private List<Sejour> sejours;
}