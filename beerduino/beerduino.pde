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
<<<<<<< HEAD
=======

  // start SPI and Ethernet
  Serial << "Initializing ethernet ...";
  delay(1000);
  SPI.begin();
  Ethernet.begin(mac, ip, gateway, subnet);
  delay(500);
  Serial << " DONE" << endl;

  // start the webserver
  Serial << "Initializing webserver on port " << WEBSERVER_PORT << " ...";  
  webserver.begin();
  webserver.setDefaultCommand( &valuesCmd );
  webserver.addCommand("values", &valuesCmd);
  webserver.addCommand("xml", &xmlCmd);
  // webserver.addCommand("configuration", &configurationCmd);
  // webserver.addCommand("save", &saveCmd);
  webserver.addCommand("about", &aboutCmd);
  delay(500);
  Serial << " DONE" << endl;
}

/**
 * Main loop.
 */
void loop() {
  // check for a reading no more than once a second.
  if (millis() - lastReadingTime > MEASURE_TEMPO){
    getDatas();
    // timestamp the last time you got a reading:
    lastReadingTime = millis();
  }

  // listen for incoming Ethernet connections:
  webserver.processConnection();
}

/**
 * Send the HTML for the "about" page
 */
void sendAboutPage(WebServer &server) {
  /*
  server.println("<li>Arduino Duemilanove AT328</li>");
   server.println("<li>Arduino Ethernet Shield</li>");
   server.println("<li>Arduino Proto Shield</li>");
   server.println("<li>Arduino library 022</li>");
   server.println("<li>Additionnal libraries : Time.h, OneWire.h, DallasTemperature.h, Wedbuino, Streaming</li>");
   server.println("<li>Thermal sensors : Dallas DS18B20</li>");
   */
>>>>>>> 4dea3178d3aae59194952624b292fdf6319c7cff
}

/**
 * Setup
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
 * Main loop.
 */
<<<<<<< HEAD
void loop() {
  // check for a reading no more than once a second.
  if (millis() - lastReadingTime > MEASURE_TEMPO){
    getDatas();
    // timestamp the last time you got a reading:
    lastReadingTime = millis();
=======
void sendMail() {
  for (int i = 0 ; i < foundSensors ; i++ ) {
    Serial << "Temp" << (i+1) << " [" << currentTemperature[i] << "], seuil [" << seuilTemperature[i] << "]" << endl;
  }
  Serial << "Envoi d'un mail ..." << endl;
  if (smtpClient.connect()) {
    smtpClient << "HELO beerduino" << endl;
    delay(SMTP_TEMPO);

    smtpClient << "MAIL FROM:destemail@free.fr" << endl; /* identify sender */
    delay(SMTP_TEMPO);

    smtpClient << "RCPT TO:destemail@free.fr" << endl; /* identify recipient */
    delay(SMTP_TEMPO);

    smtpClient << "AUTH LOGIN PLAIN CRAM-MD5" << endl; /* a tester si besoin d'authentification sur smtp.free.fr */
    delay(SMTP_TEMPO);

    // smtpClient << "8498e6ecf343c526c98388bbf310044a" << endl; /* destemail encodé en MD5 */
    // delay(SMTP_TEMPO);

    // smtpClient << "" << endl; /* password encodé en MD5 */
    // delay(SMTP_TEMPO);

    smtpClient << "DATA" << endl;
    delay(SMTP_TEMPO);

    smtpClient << "Subject: Beerduino temperature alert" << endl; /* insert subject */
    smtpClient << endl;

    // smtpClient << "Monitoring du : " << day() << "/" << month() << "/" << year() << " " << hour() << minute() << second() << endl;
    // smtpClient << endl;
    if (overheat == 1) {
      smtpClient << "Depassement des seuils de temperature autorises d'une ou plusieurs sondes !" << endl;
    } 
    else {
      smtpClient << "Retour a la normale des temperatures de toutes les sondes !" << endl;
    }
    smtpClient << endl;
    for (int i = 0 ; i < foundSensors ; i++ ) {
      smtpClient << "Temperature capteur (" << (i+1) << ") [" << currentTemperature[i] << "C] (seuil [" << seuilTemperature[i] << "C])." << endl;
    }
    smtpClient << endl;
    smtpClient << "--" << endl;
    smtpClient << "Sent from Arduino." << endl; /* insert body */
    smtpClient << "." << endl;

    smtpClient << "QUIT" << endl; /* terminate connection */
    delay(SMTP_TEMPO);

    smtpClient << endl;
    delay(SMTP_TEMPO);

    smtpClient.stop();
  } 
  else {
    Serial << "Can't send mail" << endl;
>>>>>>> 4dea3178d3aae59194952624b292fdf6319c7cff
  }
  
  // Process web connexions (outputs XML results)
  network_loop();
}

