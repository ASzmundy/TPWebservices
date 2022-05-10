package com.insa.fr;

import com.insa.fr.entity.Alive;
import com.insa.fr.entity.Logs;
import com.insa.fr.request.AddSejRequest;
import com.insa.fr.exceptions.NotAllowedOperationException;
import com.insa.fr.request.DelRequest;
import com.insa.fr.request.EndSejRequest;
import com.insa.fr.request.ModRequest;
import com.insa.fr.services.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestHeader;

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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

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
     * Mettre à jour un patient dans la BDD
     * @param modRequest
     * @return
     ****************************************/
    @PostMapping(value = "/update", consumes = {"application/json"})
    @ApiOperation(value = "Modifie un patient")
    public ModRequest modifierPatient(@RequestBody ModRequest modRequest) throws URISyntaxException, ParseException {
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

        String query;

        //récupération patientid
        int patientid = 0;
        if(!modRequest.getIPP().equals("")){
            patientid = Integer.parseInt(modRequest.getIPP());
        }
        if(modRequest.getIPP().equals("") && !modRequest.getNOM().equals("") && !modRequest.getPRENOM().equals("") && !modRequest.getDDN().equals("")) {
            DateFormat originaldate = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat targetdate = new SimpleDateFormat("MM/dd/yyyy");
            Date date = originaldate.parse(modRequest.getDDN());

            query = "SELECT patientid FROM patient WHERE nom = '" + modRequest.getNOM() + "' AND prenom = '" + modRequest.getPRENOM() + "' AND datedenaissance = '" + targetdate.format(date) + "' LIMIT 1;";
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

        //Remplissage attributs vides
        String selectquery = "SELECT * FROM patient WHERE patientid = "+ patientid + ";";
        List<Map<String, Object>> mpat=jdbctemplate.queryForList(selectquery);
        if(modRequest.getNvnom().toString().equals("")){
            modRequest.setNvnom(mpat.get(0).get("nom").toString());
        }
        if(modRequest.getNvprenom().toString().equals("")){
            modRequest.setNvprenom(mpat.get(0).get("prenom").toString());
        }
        if(modRequest.getINTIT().toString().equals("")){
            modRequest.setINTIT(mpat.get(0).get("civilite").toString());
        }
        if(modRequest.getNOMMAR().toString().equals("")){
            modRequest.setNOMMAR(mpat.get(0).get("nomusage").toString());
        }
        if(modRequest.getNvddn().toString().equals("")){
            modRequest.setNvddn(mpat.get(0).get("datedenaissance").toString());
        }
        if(modRequest.getSEXE().toString().equals("")){
            modRequest.setSEXE(mpat.get(0).get("sexe").toString());
        }
        if(modRequest.getADR1().toString().equals("")){
            modRequest.setADR1(mpat.get(0).get("adresse1").toString());
        }
        if(modRequest.getADR2().toString().equals("")){
            modRequest.setADR2(mpat.get(0).get("adresse2").toString());
        }
        if(modRequest.getCP().toString().equals("")){
            modRequest.setCP(mpat.get(0).get("codepostal").toString());
        }
        if(modRequest.getVILLE().toString().equals("")){
            modRequest.setVILLE(mpat.get(0).get("ville").toString());
        }
        if(modRequest.getPAYS().toString().equals("")){
            modRequest.setPAYS(mpat.get(0).get("pays").toString());
        }
        if(modRequest.getTEL().toString().equals("")){
            modRequest.setTEL(mpat.get(0).get("telephone").toString());
        }
        if(modRequest.getPAYSN().toString().equals("")){
            modRequest.setPAYSN(mpat.get(0).get("paysnaissance").toString());
        }

        DateFormat originaldate = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat originaldate2 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat targetdate = new SimpleDateFormat("MM/dd/yyyy");
        Date date;
        try {
            date = originaldate.parse(modRequest.getNvddn());
        }catch (ParseException pe){
            date = originaldate2.parse(modRequest.getNvddn());
        }



        //Requête de mise à jour
        String finalquery = "UPDATE patient " +
                "SET nom = '" +
                modRequest.getNvnom() +
                "', prenom = '" +
                modRequest.getNvprenom() +
                "', civilite = '" +
                modRequest.getINTIT() +
                "', nomusage = '" +
                modRequest.getNOMMAR() +
                "', datedenaissance = CAST('" +
                targetdate.format(date) +
                "' AS DATE), sexe = '" +
                modRequest.getSEXE() +
                "', adresse1 = '" +
                modRequest.getADR1() +
                "', adresse2 = '" +
                modRequest.getADR2() +
                "', codepostal = '" +
                modRequest.getCP() +
                "', ville = '" +
                modRequest.getVILLE() +
                "', pays = '" +
                modRequest.getPAYS() +
                "', telephone = '" +
                modRequest.getTEL() +
                "', paysnaissance = '" +
                modRequest.getPAYSN() +
                "' WHERE patientid = " +
                patientid +
                ";"
                ;

        try {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                System.out.println(finalquery);
                stmt.executeUpdate(finalquery);
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }



        return modRequest;
    }

    /****************************************
     * Supprime un patient de la bdd
     * @param delRequest
     * @return
     ****************************************/
    @PostMapping(value = "/delete", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Supprime un patient de la bdd")
    public DelRequest supprimerPatient(@RequestBody DelRequest delRequest) throws URISyntaxException, ParseException {
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

        String query;

        //récupération patientid
        int patientid = 0;
        if(!delRequest.getIPP().get().equals("")){
            patientid = Integer.parseInt(delRequest.getIPP().get());
        }
        if(delRequest.getIPP().get().equals("") && !delRequest.getNOM().get().equals("") && !delRequest.getPRENOM().get().equals("") && !delRequest.getDDN().get().equals("")) {
            DateFormat originaldate = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat targetdate = new SimpleDateFormat("MM/dd/yyyy");
            Date date = originaldate.parse(delRequest.getDDN().get());

            query = "SELECT patientid FROM patient WHERE nom = '" + delRequest.getNOM().get() + "' AND prenom = '" + delRequest.getPRENOM().get() + "' AND datedenaissance = '" + targetdate.format(date) + "' LIMIT 1;";
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

        query = "DELETE FROM sejour WHERE patientid = " + patientid + "; DELETE FROM patient WHERE patientid = " + patientid + ";";
        try {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                System.out.println(query);
                stmt.executeUpdate(query);
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        


        return delRequest;

    }

    /****************************************
     * Ajouter un séjour à la BDD
     * @param addSejRequest
     * @return
     ****************************************/
    @PostMapping(value = "/addsej", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Ajouter un séjour à la BDD")
    public AddSejRequest ajouterSejour(@RequestBody AddSejRequest addSejRequest) throws URISyntaxException, ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println(addSejRequest.toString());

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

        //Récupère le dernier IEP
        String query = "SELECT COALESCE(MAX(sejour.sejourid),0) AS maxiep FROM public.sejour";
        int lastIPP = 0;
        int lastIEP = 0;
        try {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                System.out.println(stmt);
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lastIEP = rs.getInt("maxiep");
                }
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        //récupération patientid
        int patientid = 0;
        if(!addSejRequest.getIPP().get().equals("")){
            patientid = Integer.parseInt(addSejRequest.getIPP().get());
        }
        if(addSejRequest.getIPP().get().equals("") && !addSejRequest.getNOM().get().equals("") && !addSejRequest.getPRENOM().get().equals("") && !addSejRequest.getDDN().get().equals("")) {
            DateFormat originaldate = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat targetdate = new SimpleDateFormat("MM/dd/yyyy");
            Date date = originaldate.parse(addSejRequest.getDDN().get());

            query = "SELECT patientid FROM patient WHERE nom = '" + addSejRequest.getNOM().get() + "' AND prenom = '" + addSejRequest.getPRENOM().get() + "' AND datedenaissance = '" + targetdate.format(date) + "' LIMIT 1;";
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
        DateFormat originaldatetime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat targetdatetime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date datetime = originaldatetime.parse(addSejRequest.getDDS());


        String finalquery = "UPDATE sejour " +
                "SET datefinsejour = '" +
                targetdatetime.format(datetime) +
                "' WHERE patientid = " +
                patientid +
                " AND datefinsejour IS NULL" +
                ";" +
                "INSERT INTO sejour(sejourid, patientid, datedesejour, uf, chambre, lit, ufmedecin) " +
                "VALUES (" +
                (lastIEP+1) +
                "," +
                patientid +
                ",'" +
                targetdatetime.format(datetime) +
                "','" +
                addSejRequest.getUF().get() +
                "'," +
                addSejRequest.getCHAMBRE() +
                "," +
                addSejRequest.getLIT() +
                ",'" +
                addSejRequest.getUFMED() +
                "')";

        try {
            try (Statement stmt = conn.createStatement()) {
                System.out.println(finalquery);
                stmt.executeUpdate(finalquery);
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }




        return addSejRequest;
    }

    /****************************************
     * Mettre fin à un séjour de la BDD
     * @param endSejRequest
     * @return
     ****************************************/
    @PostMapping(value = "/endsej", consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Mettre fin à un séjour de la BDD")
    public EndSejRequest finSejour(@RequestBody EndSejRequest endSejRequest) throws URISyntaxException, ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        System.out.println(endSejRequest.toString());

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

        //Récupère le dernier IEP
        String query = "SELECT COALESCE(MAX(sejour.sejourid),0) AS maxiep FROM public.sejour";
        int lastIPP = 0;
        int lastIEP = 0;
        try {
            assert conn != null;
            try (Statement stmt = conn.createStatement()) {
                System.out.println(stmt);
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    lastIEP = rs.getInt("maxiep");
                }
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        //récupération patientid
        int patientid = 0;
        if(!endSejRequest.getIPP().get().equals("")){
            patientid = Integer.parseInt(endSejRequest.getIPP().get());
        }
        if(endSejRequest.getIPP().get().equals("") && !endSejRequest.getNOM().get().equals("") && !endSejRequest.getPRENOM().get().equals("") && !endSejRequest.getDDN().get().equals("")) {
            DateFormat originaldate = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat targetdate = new SimpleDateFormat("MM/dd/yyyy");
            Date date = originaldate.parse(endSejRequest.getDDN().get());

            query = "SELECT patientid FROM patient WHERE nom = '" + endSejRequest.getNOM().get() + "' AND prenom = '" + endSejRequest.getPRENOM().get() + "' AND datedenaissance = '" + targetdate.format(date) + "' LIMIT 1;";
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
        DateFormat originaldatetime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat targetdatetime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date datetime;
        try {
            datetime = originaldatetime.parse(endSejRequest.getDFS());
        }catch (ParseException pe){
            Date currentdate = new Date();
            datetime = originaldatetime.parse(originaldatetime.format(currentdate));
        }


        String finalquery = "UPDATE sejour " +
                "SET datefinsejour = '" +
                 targetdatetime.format(datetime)+
                "' WHERE patientid = " +
                patientid +
                "AND datefinsejour IS NULL;"
                ;

        try {
            try (Statement stmt = conn.createStatement()) {
                System.out.println(finalquery);
                stmt.executeUpdate(finalquery);
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }




        return endSejRequest;
    }
}