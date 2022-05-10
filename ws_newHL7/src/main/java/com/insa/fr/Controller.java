package com.insa.fr;

import com.insa.fr.entity.Alive;
import com.insa.fr.entity.Logs;
import com.insa.fr.entity.Students;
import com.insa.fr.exceptions.NotAllowedOperationException;
import com.insa.fr.hl7gen.PatientService;
import com.insa.fr.hl7gen.hl7gen;
import com.insa.fr.request.Request;
import com.insa.fr.services.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

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
     * Création du fichier HL7
     * @param request
     * @return
     ****************************************/
    @PostMapping(value = "/add", consumes = {"application/json"})
    @ApiOperation(value = "Exporte en HL7 la requête")
    public void creerFichier(@RequestBody Request request) throws Exception {
        if (request.getRequestType() != null) {
            //Connection BDD
            final String url = "jdbc:postgresql://localhost:5434/tpwebservices";
            final String user = "postgres";
            final String password = "admin";
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Connection réussie à la BDD");
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            //Récupère le dernier IPP et IEP
            String query = "SELECT COALESCE(MAX(patient.patientid),0) AS maxipp, COALESCE(MAX(sejour.sejourid),0) AS maxiep FROM public.patient, public.sejour";
            int lastIPP = 0;
            int lastIEP = 0;
            try {
                assert conn != null;
                try (Statement stmt = conn.createStatement()) {
                    System.out.println(stmt);
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        lastIPP = rs.getInt("maxipp");
                        lastIEP = rs.getInt("maxiep");
                    }
                }
            } catch (SQLException e2) {
                e2.printStackTrace();
            }

            //Récupération du numéro du dernier fichier hl7
            String outputfolderpath = "Output/";
            Files.createDirectories(Paths.get(outputfolderpath));
            String[] filesinoutput;
            File f = new File(outputfolderpath);
            filesinoutput = f.list();
            assert filesinoutput != null;
            int maxfilenumber = 0;
            if (!(filesinoutput.length == 0)) {
                for (String filenameinoutput :
                        filesinoutput) {
                    String filenumberstring = filenameinoutput.split("\\.")[0].replaceAll("[^0-9]", "");
                    int filenumber = Integer.parseInt(filenumberstring);
                    if (filenumber > maxfilenumber) {
                        maxfilenumber = filenumber;
                    }
                }
            }


            //Request -> hl7gen
            hl7gen hl7gen = new hl7gen(Integer.toString(lastIPP + maxfilenumber + 1), request.getNOM(), request.getPRENOM(), request.getINTIT(), request.getNOMMAR(), request.getDDN(), request.getSEXE(), Integer.toString(lastIEP + maxfilenumber + 1), request.getADR1(), request.getADR2(), request.getCP(), request.getVILLE(), request.getPAYS(), request.getTEL(), request.getPAYSN(), request.getDDS(), request.getUF(), request.getCHAMBRE(), request.getLIT(), request.getUFMED());


            hl7gen.create_adt(outputfolderpath + "output" + (maxfilenumber + 1) + ".hl7");
            System.out.println("Fichier output"+ (maxfilenumber + 1) + ".hl7 créé");
        }
    }

    
}