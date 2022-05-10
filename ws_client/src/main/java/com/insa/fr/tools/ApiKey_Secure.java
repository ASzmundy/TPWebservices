package com.insa.fr.tools;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/********************
 * @author tondeur-h
 ********************/

@Service
public class ApiKey_Secure {
    
/***********************************
 * Ctrl API Key version database
 * @param jdbctemplate
 * @param apikey
 * @return 
 ***********************************/    
    public boolean verif_apikeydb(JdbcTemplate jdbctemplate, String  apikey)
    {
        boolean reponse=true;
        //tester la cle a partir de la database sur la table clients
        //si existe et date ok => retourner true sinon false
        String sql="select cle from clients where cle='"+apikey+"' and current_timestamp between datedeb and datefin";  
        try
        {
            //si pas d'erreur c'est que trouvé et date OK
        jdbctemplate.queryForObject(sql, String.class);
        } catch (DataAccessException dae) {reponse=false;}
        return reponse;
    }


/***********************************
 * Ctrl API Key version fixe
 * @param jdbctemplate
 * @param apikey
 * @return 
 ***********************************/    
    public boolean verif_apikey(String  apikey)
    {
        boolean reponse;
        //fix key pour cette API
        reponse = apikey.compareToIgnoreCase("CAFEBABE")==0;
        //tester la cle a partir de la database sur la table clients
        //si existe et date ok => retourner true sinon false
        //String sql="select cle from clients where cle='"+apikey+"' and current_date between datedeb and datefin";  
        //try
        //{
            //si pas d'erreur c'est que trouvé et date OK
        //jdbctemplate.queryForObject(sql, String.class);
        //} catch (DataAccessException dae) {reponse=false;}
        return reponse;
    }
    
}
