package com.insa.fr;

import com.insa.fr.entity.Alive;
import com.insa.fr.entity.Logs;
import com.insa.fr.entity.Patient;
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
import org.springframework.web.bind.annotation.RequestParam;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * Traitement de la requête
     * @param request
     * @return
     ****************************************/
    @PostMapping(value = "/see", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Traitement de la requête")
    public Patient[] generateQuery(@RequestBody Request request) throws URISyntaxException, ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println(request.toString());

        //Connection BDD
        final String dburl = "jdbc:postgresql://localhost:5434/tpwebservices";
        final String user = "postgres";
        final String password = "admin";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dburl, user, password);
            System.out.println("Connection réussie à la BDD");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        //Récupère l'id patient correspondant si l'ip est absent
        String query;
        int patientid=0;
        if(!request.getIPP().get().equals("")){
            patientid = Integer.parseInt(request.getIPP().get());
        }
        if(request.getIPP().get().equals("") && !request.getNOM().get().equals("") && !request.getPRENOM().get().equals("") && !request.getDDN().get().equals("")) {
            DateFormat originaldate = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat targetdate = new SimpleDateFormat("MM/dd/yyyy");
            Date date = originaldate.parse(request.getDDN().get());

            query = "SELECT patientid FROM patient WHERE nom = '" + request.getNOM().get() + "' AND prenom = '" + request.getPRENOM().get() + "' AND datedenaissance = '" + targetdate.format(date) + "' LIMIT 1;";
            try {
                assert conn != null;
                try (Statement stmt = conn.createStatement()) {
                    System.out.println(query);
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        patientid = rs.getInt("patientid");
                    }
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }



        URI uri = new URI("http://localhost:8069/see/"+ patientid);

        HttpEntity<Request> httpEntity = new HttpEntity<>(request, headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(uri, Patient[].class);
    }

    /****************************************
     * Récupération du patient
     * @param id
     * @return
     ****************************************/
    @GetMapping(value = "/see/{id}")
    @ApiOperation(value = "Récupération du patient")
    public ResponseEntity<Object> getPatient(@PathVariable int id) throws URISyntaxException {
        //get data
        return new ResponseEntity<>(execservice.getPatient(id), HttpStatus.OK);
    }

}
//TODO Gérer la récupération du patient et de ses séjours