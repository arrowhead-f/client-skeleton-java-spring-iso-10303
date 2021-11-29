package com.jotne.iso10303_database_consumer_skeleton.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class JotneSensorDataDTO 
{	
    @JsonProperty("SensorType")
    private String SensorType;

    private String id;

    @JsonProperty("SensorData")
    private List<SensorDataDTO> SensorData = new ArrayList<SensorDataDTO>();

    public JotneSensorDataDTO() {
    }

    public JotneSensorDataDTO(String sensorType, String id, List<SensorDataDTO> sensorData) {
            this.SensorType = sensorType;
            this.id = id;
            this.SensorData = sensorData;
    }

    public String getSensorType() {
            return SensorType;
    }

    @JsonSetter("SensorType")
    public void setSensorType(String sensorType) {
            SensorType = sensorType;
    }

    public String getId() {
            return id;
    }
    public void setId(String id) {
            this.id = id;
    }
    public List<SensorDataDTO> getSensorData() {
            return SensorData;
    }

    @JsonSetter("SensorData")
    public void setSensorData(List<SensorDataDTO> sensorData) {
            this.SensorData = sensorData;
    }

    @Override
    public String toString() {
            return "JotneSensorDataDTO [SensorType=" + SensorType + ", id=" + id + ", SensorData=" + SensorData + "]";
    }
}
