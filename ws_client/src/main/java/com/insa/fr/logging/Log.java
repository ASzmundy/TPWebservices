package com.insa.fr.logging;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author TONDEUR Hervé
 */
public class Log {
    
    @JsonIgnore
    private String datelog;
    private String service;
    private String trying;
    private String message;
    private LogLevel level;
    private LogType type;

    /**
     * @return the datelog
     */
    public String getDatelog() {
        return datelog;
    }

    /**
     * @param datelog the datelog to set
     */
    public void setDatelog(String datelog) {
        this.datelog = datelog;
    }
    
    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @return the trying
     */
    public String getTrying() {
        return trying;
    }

    /**
     * @param trying the trying to set
     */
    public void setTrying(String trying) {
        this.trying = trying;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the level
     */
    public LogLevel getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(LogLevel level) {
        this.level = level;
    }

    /**
     * @return the Type
     */
    public LogType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(LogType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Log{" + "service=" + service + ", trying=" + trying + ", message=" + message + ", level=" + level + ", type=" + type + '}';
    }

}
