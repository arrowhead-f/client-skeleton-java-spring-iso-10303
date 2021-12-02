# ISO 10303 Database Consumer Skeleton (Java Spring-Boot)
##### The project provides the skeleton project for the Consumer application development to communicate with the ISO 10303 Database Provider

ISO 10303 Database Consumer Skeleton contains all needed methods and the data objects to communicate with the ISO 10303 Database Provider.
The ISO 10303 Database Provider provides 4 services to read and write the sensor's data to and from the EDMtruePLM system. The EDMtruePLM is the PLM system based on the ISO 10303 Database (https://jotneit.no/products/edmtrueplm).  

There are 4 services, which can be used to communicate with the ISO 10303 Database Provider 
- The **getSensors** service provides info about sensors in the project
- The **getSensorBySN** service provides detailed info about the sensor with the specified serial number
- The **getSensorData** service provides sensor's data for the sensor with the specified serial number with the filtering by the key field
- The **addSensorData** service provides the possibility to add sensor's data for the sensor with the specified serial number 

Each service has to be initialized before usage.

### Example
    try
    {
        if( consumerBase.initGetSensorsService() )
            ArrayList<SensorDescriptionDTO> sensorsInfo = consumerBase.getSensors( "Project" );
    }
    catch( Exception ex )
    {
        logger.error( "Error while getting sensors info: " + ex.getMessage() );
    }

### Requirements

The Consumer Skeleton requires version 4.4 of the ArrowHead Framework.

The project has the following dependencies:
* JRE/JDK 11 [Download from here](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
* Maven 3.5+ [Download from here](http://maven.apache.org/download.cgi) | [Install guide](https://www.baeldung.com/install-maven-on-windows-linux-mac)
