package com.jotne.iso10303_database_consumer_skeleton;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jotne.iso10303_database_consumer_skeleton.dto.JotneSensorDataDTO;
import com.jotne.iso10303_database_consumer_skeleton.dto.SensorDataDTO;
import com.jotne.iso10303_database_consumer_skeleton.dto.SensorDescriptionDTO;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.OrchestrationFlags;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.exception.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class ConsumerSkeleton
{

    @Autowired
    private ArrowheadService arrowheadService;

    @Autowired
    protected SSLProperties sslProperties;

    private final Logger logger = LogManager.getLogger( ConsumerSkeleton.class );

    private OrchestrationResultDTO getDataSrv, addDataSrv, getSensorsSrv, getSensorBySNSrv;

    /**
    * The getSensors service provides info about sensors in the project
     * @param projName - project name
     * @return ArrayList of the SensorDescriptionDTO
     * @throws java.lang.Exception
    */
    public ArrayList<SensorDescriptionDTO> getSensors( String projName ) throws Exception
    {
        if( getSensorsSrv == null )
        {
            throw new Exception( "Service not initialized" );
        }

        String serviceURI = "/" + projName;

        String token = getSensorsSrv.getAuthorizationTokens() == null ? null
                : getSensorsSrv.getAuthorizationTokens().get( getInterface() );

        String sensorsInfo = arrowheadService.consumeServiceHTTP( String.class,
                HttpMethod.valueOf( getSensorsSrv.getMetadata().get( Constants.HTTP_METHOD ) ),
                getSensorsSrv.getProvider().getAddress(), getSensorsSrv.getProvider().getPort(),
                getSensorsSrv.getServiceUri() + serviceURI, getInterface(), token, null );
        
        ArrayList<SensorDescriptionDTO> sensorList = null;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode objRez = mapper.readTree( sensorsInfo );
        if( objRez.isArray() )
        {
            sensorList = new ArrayList<>();
            Iterator<JsonNode> itr = objRez.iterator();
            while( itr.hasNext() )
            {
                JsonNode node = itr.next();
                SensorDescriptionDTO sDescr = mapper.readValue( node.traverse(), SensorDescriptionDTO.class );
                sensorList.add( sDescr );
            }
        }

        return sensorList;
    }

    /**
    * The getSensorBySN service provides detailed info about the sensor with the specified serial number
     * @param projName - project name
     * @param sensorSN - sensor serial number
     * @return SensorDescriptionDTO
     * @throws java.lang.Exception 
    */
    public SensorDescriptionDTO getSensorBySN( String projName, String sensorSN ) throws Exception
    {
        if( getSensorBySNSrv == null )
        {
            throw new Exception( "Service not initialized" );
        }

        String serviceURI = "/" + projName + "/" + sensorSN;

        String token = getSensorBySNSrv.getAuthorizationTokens() == null ? null
                : getSensorBySNSrv.getAuthorizationTokens().get( getInterface() );

        String sensorInfo = arrowheadService.consumeServiceHTTP( String.class,
                HttpMethod.valueOf( getSensorBySNSrv.getMetadata().get( Constants.HTTP_METHOD ) ),
                getSensorBySNSrv.getProvider().getAddress(), getSensorBySNSrv.getProvider().getPort(),
                getSensorBySNSrv.getServiceUri() + serviceURI, getInterface(), token, null );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree( sensorInfo );
        SensorDescriptionDTO sDescr = mapper.readValue( node.traverse(), SensorDescriptionDTO.class );
        
        return sDescr;
    }

    /**
    * The getSensorData service provides sensor's data for the sensor with the specified serial number 
    * with the filtering by the key field
     * @param projName - project name
     * @param sensorSN - sensor serial number
     * @param propURN - property URN
     * @param fromKey - start value of the key field for the filtering 
     * @param toKey - last value of the key field for the filtering 
     * @return SensorDataDTO
     * @throws java.lang.Exception
    */
    public JotneSensorDataDTO getSensorData( String projName, String sensorSN, String propURN, String fromKey, String toKey ) throws Exception
    {
        if( getDataSrv == null )
        {
            throw new Exception( "Service not initialized" );
        }

        String serviceURI = "/" + projName + "/" + sensorSN + "/" + propURN;

        String token = getDataSrv.getAuthorizationTokens() == null ? null
                : getDataSrv.getAuthorizationTokens().get( getInterface() );

        ArrayList<String> prmLst = new ArrayList<>();
        if( fromKey != null )
        {
            prmLst.add( "fromKey" );
            prmLst.add( fromKey );
        }
        if( toKey != null )
        {
            prmLst.add( "toKey" );
            prmLst.add( toKey );
        }

        String[] prms = null;
        if( !prmLst.isEmpty() )
        {
            prms = prmLst.toArray( new String[ prmLst.size() ] );
        }

        String sensorData = arrowheadService.consumeServiceHTTP( String.class,
                HttpMethod.valueOf( getDataSrv.getMetadata().get( Constants.HTTP_METHOD ) ),
                getDataSrv.getProvider().getAddress(), getDataSrv.getProvider().getPort(),
                getDataSrv.getServiceUri() + serviceURI, getInterface(), token, null, prms );
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree( sensorData );
        JotneSensorDataDTO sData = mapper.readValue( node.traverse(), JotneSensorDataDTO.class );
        
        return sData;
    }

    /**
    * The addSensorData service provides the possibility to add sensor's data for 
    * the sensor with the specified serial number 
     * @param projName - project name
     * @param sensorSN - sensor serial number
     * @param propURN - property URN
     * @param data - sensor data
     * @return OK or not
     * @throws java.lang.Exception
    */
    public String addSensorData( String projName, String sensorSN, String propURN, JotneSensorDataDTO data ) throws Exception
    {
        if( addDataSrv == null )
        {
            throw new Exception( "Service not initialized" );
        }

        String serviceURI = "/" + projName + "/" + sensorSN + "/" + propURN;

        String token = addDataSrv.getAuthorizationTokens() == null ? null
                : addDataSrv.getAuthorizationTokens().get( getInterface() );

        String result = arrowheadService.consumeServiceHTTP( String.class,
                HttpMethod.valueOf( addDataSrv.getMetadata().get( Constants.HTTP_METHOD ) ),
                addDataSrv.getProvider().getAddress(), addDataSrv.getProvider().getPort(),
                addDataSrv.getServiceUri() + serviceURI, getInterface(), token, data );

        return result;
    }

    /**
    * The initialization of the getData service
     * @return true in case of successful initialization
    */
    public boolean initGetDataService()
    {
        getDataSrv = getServiceInfo( Constants.TRUEPLM_GET_SENSOR_DATA_SERVICE );
        return ( getDataSrv != null );
    }

    /**
    * The initialization of the addData service
     * @return true in case of successful initialization
    */
    public boolean initAddDataService()
    {
        addDataSrv = getServiceInfo( Constants.TRUEPLM_ADD_SENSOR_DATA_SERVICE );
        return ( addDataSrv != null );
    }

    /**
    * The initialization of the getSensors service
     * @return true in case of successful initialization
    */
    public boolean initGetSensorsService()
    {
        getSensorsSrv = getServiceInfo( Constants.TRUEPLM_SENSORS_IN_PROJ_SERVICE );
        return ( getSensorsSrv != null );
    }

    /**
    * The initialization of the getSensorBySN service
     * @return true in case of successful initialization
    */
    public boolean initGetSensorBySNService()
    {
        getSensorBySNSrv = getServiceInfo( Constants.TRUEPLM_SENSOR_BY_SN_SERVICE );
        return ( getSensorBySNSrv != null );
    }

    private OrchestrationResultDTO getServiceInfo( String serviceName )
    {
        final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(
                serviceName ).interfaces( getInterface() ).build();

        final OrchestrationFormRequestDTO.Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
        final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder
                .requestedService( serviceQueryForm )
                .flag( OrchestrationFlags.Flag.TRIGGER_INTER_CLOUD, true )
                .flag( OrchestrationFlags.Flag.OVERRIDE_STORE, true )
                .flag( OrchestrationFlags.Flag.ENABLE_INTER_CLOUD, true )
                .build();

        logger.info( "Orchestration request for " + serviceName + " service:" );
        printOut( orchestrationFormRequest );

        try
        {
            final OrchestrationResponseDTO orchestrationResponse
                    = arrowheadService.proceedOrchestration( orchestrationFormRequest );

            logger.info( "Orchestration response:" );
            printOut( orchestrationResponse );

            if( orchestrationResponse == null )
            {
                logger.info( "No orchestration response received" );
            }
            else if( orchestrationResponse.getResponse().isEmpty() )
            {
                logger.info( "No provider found during the orchestration" );
            }
            else
            {
                final OrchestrationResultDTO orchRez = orchestrationResponse.getResponse().get( 0 );
                validateOrchestrationResult( orchRez, serviceName );

                return orchRez;
            }
        }
        catch( Exception ex )
        {
            logger.info( ex.getMessage() );
        }

        return null;
    }

    // =================================================================================================
    // assistant methods
    // -------------------------------------------------------------------------------------------------
    private String getInterface()
    {
        return sslProperties.isSslEnabled() ? Constants.INTERFACE_SECURE : Constants.INTERFACE_INSECURE;
    }

    // -------------------------------------------------------------------------------------------------
    private void validateOrchestrationResult( final OrchestrationResultDTO orchestrationResult,
            final String serviceDefinition )
    {
        if( !orchestrationResult.getService().getServiceDefinition().equalsIgnoreCase( serviceDefinition ) )
        {
            throw new InvalidParameterException( "Requested and orchestrated service definition do not match" );
        }

        boolean hasValidInterface = false;
        for( final ServiceInterfaceResponseDTO serviceInterface : orchestrationResult.getInterfaces() )
        {
            if( serviceInterface.getInterfaceName().equalsIgnoreCase( getInterface() ) )
            {
                hasValidInterface = true;
                break;
            }
        }
        if( !hasValidInterface )
        {
            throw new InvalidParameterException( "Requested and orchestrated interface do not match" );
        }
    }

    // -------------------------------------------------------------------------------------------------
    public void printOut( final Object object )
    {
        logger.info( Utilities.toPrettyJson( Utilities.toJson( object ) ) );
    }
}
