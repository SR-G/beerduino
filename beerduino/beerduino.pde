/**
 * OK   rendre dynamique le nombre de capteurs (tableau) (2011-09-26) ;
 * TODO ajout quartz (matériel) + librairie Time.h + NTP ;
 * TODO configuration par EEPROM (mails + seuils) ;
 * TODO ajout DHCP + résolution DNS ;
 * TODO ajout afficheur LED ;
 * TODO ajout LED de couleur ;
 * TODO écriture des données lues sur carte SD si disponible ;
 *
 * Nécessite Arduino 022
 * WebDuino http://code.google.com/p/webduino/
 */
#include <OneWire.h>
#include <DallasTemperature.h>
#include <Streaming.h>

// #include <beerduino_network_enc28j60.h>
// #include "beerduino_network_wiznet.cpp" // Official arduino network card

// #include <WebServer.h>
// #include <Time.h>
// #include <EthernetDHCP.h>
// #include <EthernetDNS.h>
// #include <Udp.h>
// #include <EEPROM.h>
// #include <PString.h>

// Data wire is plugged into pin 9 on the Arduino (beware, pins 10,11,12 are used by the ethernet shield so don't use them !)
#define ONE_WIRE_BUS 9

// Max number of sensors on the board (read carefully about parasitic and normal power mode here : http://www.arduino.cc/playground/Learning/OneWire )
#define MAX_SENSORS 10

// Level of webduino debug information
#define WEBDUINO_SERIAL_DEBUGGING 9

// This creates an instance of the webserver.  By specifying a prefix
// of "/", all pages will be at the root of the server. 
#define PREFIX ""

// Ports, webserver + destination smtp
#define WEBSERVER_PORT 80

// Measure tempo between each Dallas measure (in ms)
#define MEASURE_TEMPO 1000

// Arrays that will hold found temperaturs and values under or over which mails will be sent
float currentTemperature[MAX_SENSORS];
float seuilTemperature[MAX_SENSORS] = {26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0};

// Internal variables
int foundSensors = 0; // Number of 1wire sensors found on the 1wire bus
long lastReadingTime = 0; // Last time the previous reading was done (no more than 1 reading per seconds)

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature.
DallasTemperature sensors(&oneWire);

void setupSensors() {
  // Unused ports reinitialisation
  /*
  for (int i = 2 ; i < 8; i++) {
   pinMode(i, INPUT);
   }
   */

  // Start standard serial port
  Serial.begin(9600);
  Serial << "Dallas Temperature IC Control Library Demo" << endl;

  // Start up the 1wire Dallas library
  sensors.begin();

  // Find 1wire devices on the 1wire bus
  foundSensors = sensors.getDeviceCount();

  if ( foundSensors > MAX_SENSORS ) {
    Serial << "ERROR " << foundSensors << " devices on 1wire bus whereas max allowed is " << MAX_SENSORS;
  } 
  else {
    Serial << "Found " << foundSensors << " devices on 1wire bus";
  }
}

/**
 * ARDUINO Setup
 */
void setup() {
  setupSensors();
  setupNetwork();
}

/**
 * Get temperature values from connected sensors and generate the XML
 */
 /*
void sendXMLValues(WebServer &server) {
  server << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
    << "<sensors time=\"" << millis() << "\">";
  for ( int i = 0 ; i < foundSensors ; i++ ) {
    server << "    <sensor id=\"" << (i+1) << "\" value=\"" << currentTemperature[i] << "\" type=\"C\" seuil=\"" << seuilTemperature[i] << "\"/>\n";
  }    
  server << "</sensors>" << endl;
}
*/

/** 
 * Get temperateurs from the sensors.
 */
void getDatas() {
  // call sensors.requestTemperatures() to issue a global temperature
  // request to all devices on the bus
  Serial << "Requesting temperatures ...";
  sensors.requestTemperatures(); // Send the command to get temperatures
  Serial << " DONE" << endl;

  Serial << "[" << millis() << "] Temperatures : ";
  for (int i = 0 ; i < foundSensors ; i++ ) {
    currentTemperature[i] = sensors.getTempCByIndex(i);
    Serial << currentTemperature[i] << " | ";
  }
  Serial << endl;

  /*
  // Code for mail notification
  if ( overheat == 0 ) {  
    for (int i = 0 ; i < foundSensors ; i++ ) {
      if ( currentTemperature[i] >= seuilTemperature[i] ) {
        overheat = 1;
      }
      if ( overheat == 1 ) {
        sendMail();
      }
    }
  } 
  else {
    int temperaturesReleased = 0;
    for (int i = 0 ; i < foundSensors ; i++ ) {
      if ( currentTemperature[i] < seuilTemperature[i] ) {
        temperaturesReleased++;
      }
      if ( temperaturesReleased == foundSensors ) {
        // All sensors are now under limits
        overheat = 0;
        sendMail();
      }
    }    
  }
  */
}

/**
 * ARDUINO Main loop.
 */
void loop() {
  // check for a reading no more than once a second.
  if (millis() - lastReadingTime > MEASURE_TEMPO){
    getDatas();
    // timestamp the last time you got a reading:
    lastReadingTime = millis();
  }
  
  // Process web connexions (outputs XML results)
  network_loop();
}

