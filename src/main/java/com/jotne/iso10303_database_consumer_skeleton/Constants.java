package com.jotne.iso10303_database_consumer_skeleton;

public class Constants
{

    //=================================================================================================
    // members
    public static final String INTERFACE_SECURE = "HTTPS-SECURE-JSON";
    public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
    public static final String HTTP_METHOD = "http-method";

    public static final String TRUEPLM_SENSORS_IN_PROJ_SERVICE = "trueplm-sensors-in-project-service";
    public static final String TRUEPLM_SENSOR_BY_SN_SERVICE = "trueplm-sensor-by-sn-service";
    public static final String TRUEPLM_GET_SENSOR_DATA_SERVICE = "trueplm-get-sensor-data-service";
    public static final String TRUEPLM_ADD_SENSOR_DATA_SERVICE = "trueplm-add-sensor-data-service";

    //=================================================================================================
    // assistant methods
    //-------------------------------------------------------------------------------------------------
    private Constants()
    {
        throw new UnsupportedOperationException();
    }
}
