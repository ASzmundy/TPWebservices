package com.insa.fr.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Sejour {

    private int IEP;//
    private int IPP;
    private String DDS;
    private String UF;
    private int CHAMBRE;
    private int LIT;
    private String UFMED;
    private String DFS;

}
