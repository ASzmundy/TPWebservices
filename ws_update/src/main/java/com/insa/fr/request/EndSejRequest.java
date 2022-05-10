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
public class EndSejRequest
{
    String RequestType;
    String IPP;
    String NOM;
    String PRENOM;
    String DDN;

    public Optional<String> getIPP() {
        return Optional.ofNullable(IPP);
    }
    public Optional<String> getNOM() {
        return Optional.ofNullable(NOM);
    }
    public Optional<String> getPRENOM() {
        return Optional.ofNullable(PRENOM);
    }
    public Optional<String> getDDN() {
        return Optional.ofNullable(DDN);
    }

    String DFS;

}
