/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jotne.iso10303_database_consumer_skeleton.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;

/**
 *
 * @author FRA
 */
public class SensorDescriptionDTO 
{
    private String name = null, description = null, serialNumber = null, type = null;
    private ArrayList<String> aggrProps = null;
    private ArrayList<String> aggrPropTypes = null;
    
    private long instanceID = -1;

    public long getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(long instanceID) {
        this.instanceID = instanceID;
    }
     
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getAggrProps() {
        return aggrProps;
    }

    public void addAggrProp( String prop ) {
        if( aggrProps == null )
            aggrProps = new ArrayList<>();
        
        aggrProps.add( prop );
    }
    
    public void setAggrProps(ArrayList<String> aggrProps) {
        this.aggrProps = aggrProps;
    }

    public ArrayList<String> getAggrPropTypes() {
        return aggrPropTypes;
    }

    public void addAggrPropType( String type ) {
        if( aggrPropTypes == null )
            aggrPropTypes = new ArrayList<>();
        
        aggrPropTypes.add( type );
    }
    
    public void setAggrPropTypes(ArrayList<String> aggrPropTypes) {
        this.aggrPropTypes = aggrPropTypes;
    }
}
