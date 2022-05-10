package com.insa.fr;

import com.insa.fr.entity.Alive;
import com.insa.fr.entity.Logs;
import com.insa.fr.request.Request;
import com.insa.fr.exceptions.NotAllowedOperationException;
import com.insa.fr.request.RequestService;
import com.insa.fr.services.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.insa.fr.services.Services_Interface;
import com.insa.fr.tools.ApiKey_Secure;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/***************
 *
 * @author herve
 *************/

@RestController
@Api(value = "Application Template")
public class Controller {
 
    String VERSION="0.1";
    
    String WHOAMI="ws_services_template";
    
    @Autowired
    Services_Interface execservice;
    
    @Autowired
    JdbcTemplate jdbctemplate= new JdbcTemplate();
    
  //Construire les tools
    //====================
    //construire instance pour appel des logs...
       @Autowired
    LogService logService;
    
    //construire instance pour appel secureapi...
      @Autowired
       ApiKey_Secure securapi;
    
    /*************************************
     * Service de vie des service logs
     * @param xapikey
     * @return 
     **************************************/    
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Tester la vie du service")
    public Alive IAmAlive(@RequestHeader("x-api-key") String xapikey)
    {
       //if (securapi.verif_apikey(xapikey)==true)
       if (securapi.verif_apikeydb(jdbctemplate,xapikey)==true)
        {
        return new Alive("42", VERSION, WHOAMI);
        } else {throw new NotAllowedOperationException("NotAllowedOperationException : Contactez l'administrateur pour obtenir un acces...");}
    }

    /*************************************
     * Service de vie des service logs
     * @param xapikey
     * @return 
     **************************************/        
    @GetMapping("/service")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Tester la vie du service")
    public Alive IAmAlive2(@RequestHeader("x-api-key") String xapikey)
    {
       //if (securapi.verif_apikey(xapikey)==true)
       if (securapi.verif_apikeydb(jdbctemplate,xapikey)==true)
        {
        return new Alive("42", VERSION, WHOAMI);
        } else {throw new NotAllowedOperationException("NotAllowedOperationException : Contactez l'administrateur pour obtenir un acces...");}
    }

    /*************************************
     * Service de vie des service logs
     * @param xapikey
     * @return 
     **************************************/    
    @GetMapping("/service/isalive")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Tester la vie du service")
    public Alive IAmAlive3(@RequestHeader("x-api-key") String xapikey)
    {
       //if (securapi.verif_apikey(xapikey)==true)
       if (securapi.verif_apikeydb(jdbctemplate,xapikey)==true)
        {
        return new Alive("42", VERSION, WHOAMI);
        } else {throw new NotAllowedOperationException("NotAllowedOperationException : Contactez l'administrateur pour obtenir un acces...");}
    }

    
    /****************************************
     * Envoyer des données vers les logs
     * @param log
     * @param xapikey
     * @return 
     ****************************************/
    @PostMapping(value="/service/logs", consumes={"application/json"}, produces ={"application/json"})
    @ApiOperation(value = "Inserer des logs dans la base des logs MongoDB")
    public ResponseEntity<String> insertLogs(@RequestBody Logs log,@RequestHeader("x-api-key") String xapikey) {
       //if (securapi.verif_apikey(xapikey)==true)
       if (securapi.verif_apikeydb(jdbctemplate,xapikey)==true)
        {        
            //insert data
            execservice.setLogs(log);
    
          return new ResponseEntity<>("{\"reponse\":\"Log ACTION inserré correctement\"}", HttpStatus.CREATED);
        } else {throw new NotAllowedOperationException("NotAllowedOperationException : Contactez l'administrateur pour obtenir un acces...");}
   }

    /****************************************
     * Envoi de la requête au bon WS
     * @param request
     * @return
     ****************************************/
    @PostMapping(value = "/service/request", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Envoyer la requête au bon WS")
    public String sendRequest(@RequestBody Request request) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println(request.toString());

        URI uri;
        switch (request.getRequestType()){
            case "add": uri = new URI("http://localhost:8071/add");
                break;
            case "see": uri = new URI("http://localhost:8069/see");
                break;
            case "addsej": uri = new URI("http://localhost:8068/addsej");
                break;
            case "endsej": uri = new URI("http://localhost:8068/endsej");
                break;
            case "mod": uri = new URI("http://localhost:8068/update");
                break;
            case "del": uri = new URI("http://localhost:8068/delete");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + request.getRequestType());
        }

        HttpEntity<Request> httpEntity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForObject(uri, httpEntity, String.class);
    }

}