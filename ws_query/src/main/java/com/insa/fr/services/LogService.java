package com.insa.fr.services;

import com.insa.fr.logging.Log;
import com.insa.fr.logging.LogLevel;
import com.insa.fr.logging.LogType;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Matthieu Bouchez
 */
@Service
public class LogService {
   
    @Value("${log.timeout.duration}")
    int duration;
    
    @Value("${application.logs_uri}")
    String logs_uri;
    
    private final RestTemplate restTemplate = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofMillis(duration))
            .setReadTimeout(Duration.ofMillis(duration))
            .build();
       
    /**
     * Envoi d'une requête Post vers l'API logs
     * @param service
     * @param level
     * @param type
     * @param trying
     * @param message
     * @return 
     */
    public boolean log(String service, LogLevel level, LogType type, String trying, String message) {
        String url = logs_uri + "/logs/log" + type.toString();
        
        Log log = new Log();
        log.setService(service);
        log.setLevel(level);
        log.setType(type);
        log.setTrying(trying);
        log.setMessage(message);
        
        ResponseEntity<String> result;
        
        try {
            result = restTemplate.postForEntity(url, log, String.class);
        } catch(RestClientException rce) {
            System.out.println("Erreur d'appel post :");
            System.out.println(rce.getMessage());
            return false;
        }
        
        System.out.println("Retour log OK!");
        return (result.getStatusCodeValue() == 201);
    }
    
}
