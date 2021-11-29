package com.jotne.iso10303_database_consumer_skeleton;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import com.jotne.iso10303_database_consumer_skeleton.dto.JotneSensorDataDTO;
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
    */
    public String getSensors( String projName ) throws Exception
    {
        if( getSensorsSrv == null )
        {
            throw new Exception( "Service not initialized" );
        }

        String serviceURI = "/" + projName;

        String token = getSensorsSrv.getAuthorizationTokens() == null ? null
                : getSensorsSrv.getAuthorizationTokens().get( getInterface() );

        String result = arrowheadService.consumeServiceHTTP( String.class,
                HttpMethod.valueOf( getSensorsSrv.getMetadata().get( Constants.HTTP_METHOD ) ),
                getSensorsSrv.getProvider().getAddress(), getSensorsSrv.getProvider().getPort(),
                getSensorsSrv.getServiceUri() + serviceURI, getInterface(), token, null );

        return result;
    }

    /**
    * The getSensorBySN service provides detailed info about the sensor with the specified serial number
    */
    public String getSensorBySN( String projName, String sensorSN ) throws Exception
    {
        if( getSensorBySNSrv == null )
        {
            throw new Exception( "Service not initialized" );
        }

        String serviceURI = "/" + projName + "/" + sensorSN;

        String token = getSensorBySNSrv.getAuthorizationTokens() == null ? null
                : getSensorBySNSrv.getAuthorizationTokens().get( getInterface() );

        String result = arrowheadService.consumeServiceHTTP( String.class,
                HttpMethod.valueOf( getSensorBySNSrv.getMetadata().get( Constants.HTTP_METHOD ) ),
                getSensorBySNSrv.getProvider().getAddress(), getSensorBySNSrv.getProvider().getPort(),
                getSensorBySNSrv.getServiceUri() + serviceURI, getInterface(), token, null );

        return result;
    }

    /**
    * The getSensorData service provides sensor's data for the sensor with the specified serial number 
    * with the filtering by the key field
    */
    public String getSensorData( String projName, String sensorSN, String propURN, String fromKey, String toKey ) throws Exception
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

        String result = arrowheadService.consumeServiceHTTP( String.class,
                HttpMethod.valueOf( getDataSrv.getMetadata().get( Constants.HTTP_METHOD ) ),
                getDataSrv.getProvider().getAddress(), getDataSrv.getProvider().getPort(),
                getDataSrv.getServiceUri() + serviceURI, getInterface(), token, null, prms );

        return result;
    }

    /**
    * The addSensorData service provides the possibility to add sensor's data for 
    * the sensor with the specified serial number 
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
    * The initialisation of the getData service
    */
    public boolean initGetDataService()
    {
        getDataSrv = getServiceInfo( Constants.TRUEPLM_GET_SENSOR_DATA_SERVICE );
        return ( getDataSrv != null );
    }

    /**
    * The initialisation of the addData service
    */
    public boolean initAddDataService()
    {
        addDataSrv = getServiceInfo( Constants.TRUEPLM_ADD_SENSOR_DATA_SERVICE );
        return ( addDataSrv != null );
    }

    /**
    * The initialisation of the getSensors service
    */
    public boolean initGetSensorsService()
    {
        getSensorsSrv = getServiceInfo( Constants.TRUEPLM_SENSORS_IN_PROJ_SERVICE );
        return ( getSensorsSrv != null );
    }

    /**
    * The initialisation of the getSensorBySN service
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
