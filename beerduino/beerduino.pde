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
#include <Ethernet.h>
#include <SPI.h>
#include <Streaming.h>
#include <WebServer.h>
// #include <Time.h>
// #include <EthernetDHCP.h>
// #include <EthernetDNS.h>
// #include <Udp.h>
// #include <EEPROM.h>
// #include <PString.h>

// Data wire is plugged into pin 10 on the Arduino
#define ONE_WIRE_BUS 9

// Max number of sensors on the board
#define MAX_SENSORS 10

// Level of webduino debut
#define WEBDUINO_SERIAL_DEBUGGING 9

// This creates an instance of the webserver.  By specifying a prefix
// of "/", all pages will be at the root of the server. 
#define PREFIX ""

// Ports, webserver + destination smtp
#define WEBSERVER_PORT 80
#define SMTP_PORT 25

// SMTP tempo between each operation (in ms)
#define SMTP_TEMPO 250

// Measure tempo between each Dallas measure (in ms)
#define MEASURE_TEMPO 1000

// Standard ethernet parameters
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
byte subnet[] = { 255, 255, 255, 0 };

// Specific ethernet parameters
byte ip[] = { 192, 168, 0, 101 };
byte gateway[] = { 192, 168, 0, 254 };
byte smtpServer[] = { 212, 27, 48, 4 }; // smtp.free.fr
// byte ip[] = { 10, 90, 156, 160 }; // ip PSA
// byte gateway[] = { 10, 90, 156, 254 }; // gateway PSA
// byte smtpServer[] = { 10, 68, 184, 66 }; // smtp.inetpsa.com

// Arrays that will hold found temperaturs and values under or over which mails will be sent
float currentTemperature[MAX_SENSORS];
float seuilTemperature[MAX_SENSORS] = {26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0};

// Internal variables
int foundSensors = 0; // Number of 1wire sensors found on the 1wire bus
long lastReadingTime = 0; // Last time the previous reading was done (no more than 1 reading per seconds)
int overheat = 0; // Are limits reached

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature.
DallasTemperature sensors(&oneWire);

// Setup a webduino web server
WebServer webserver(PREFIX, WEBSERVER_PORT);

// Setup a mail client
Client smtpClient(smtpServer, SMTP_PORT);

// Function prototypes to trick the Arduino pre-processor into allowing call-by-reference
void sendHeaderPage(WebServer &server);
void sendFormButtons(WebServer &server);
void sendTemperatureValues(WebServer &server);
void sendXMLValues(WebServer &server);
void sendAboutPage(WebServer &server);
// void sendConfigurationPage(WebServer &server);
// void sendSavePage(WebServer &server);

/**
 * Default page to return to browser
 */
void valuesCmd(WebServer &server, WebServer::ConnectionType type, char *url_tail, bool tail_complete) {
  server.httpSuccess();
  sendHeaderPage(server);
  sendTemperatureValues(server);
  sendFormButtons(server);
}

/**
 * Send temp values as XML
 */
void xmlCmd(WebServer &server, WebServer::ConnectionType type, char *url_tail, bool tail_complete) {
  server.httpSuccess("text/xml");
  sendXMLValues(server);
}

/**
 * Default page to return to browser
 */
void aboutCmd(WebServer &server, WebServer::ConnectionType type, char *url_tail, bool tail_complete) {
  server.httpSuccess();
  sendHeaderPage(server);
  sendAboutPage(server);
  sendFormButtons(server);
}

/**
 * Configuration page
 */
/*
void configurationCmd(WebServer &server, WebServer::ConnectionType type, char *url_tail, bool tail_complete) {
 server.httpSuccess();
 sendHeaderPage(server);
 sendConfigurationPage(server);
 sendFormButtons(server);
 }
 */

/*
void saveCmd(WebServer &server, WebServer::ConnectionType type, char *url_tail, bool tail_complete) {
 server.httpSuccess();
 sendHeaderPage(server);
 sendSavePage(server);
 sendFormButtons(server);
 }
 */

/**
 * Setup
 */
void setup() {

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
   server.println("Contact: <a href=\"mailto:serge.simon@gmail.com\">serge.simon@gmail.com</a>.");
   */
}


/*
void sendSavePage(WebServer &server) {
 server.println("Not yet done !");;
 }
 */

/**
 * Header
 */
void sendHeaderPage(WebServer &server) {
  server.println("<h1>Beerduino v0.1</h1>");
}

/**
 * Send the HTML for the "configuration" page
 */
/*
void sendConfigurationPage( WebServer &server ) {
 server.println("<h2>Configuration</h2>");
 
 // server.println("IP serveur SMTP : <input type=text name=smtp value=\"\"><br />");
/*
 server.print("Seuil temperature1 : <input type=text name=seuil value=\"");
 server.print(seuilTemperature1);
 server.print("\"><br />"); 
 
 // Display a form button to update the display
/*
 server.println("<p><form METHOD='POST' action='" PREFIX "/configuration'>");
 server.println("Mails destinataires :<br>");
 server.println("IP Serveur SMTP : <br>");
 server.println("Seuil temperature 1 (Celsius) : <br>");
 server.println("Seuil temperature 2 (Celsius) : <br>");
 server << "<input type=submit value=\"Save Configuration\">" << endl;
 server.println("</form></p>");
 */
// server.println("<input type=text name=emails>");
//}

/**
 * Send the HTML for the navigation buttons
 */
void sendFormButtons( WebServer &server ) {
  server.println("<br /><hr />[&nbsp;<a href=\"" PREFIX "/values\">Datas</a>&nbsp;] | [&nbsp;<a href=\"" PREFIX "/configuration\">Configuration</a>&nbsp;] | [&nbsp;<a href=\"" PREFIX "/about\">About</a>&nbsp;] | [&nbsp;<a href=\"" PREFIX "/xml\">XML</a>&nbsp;]<br />");
}

/**
 * Generates the HTML with the previously read temperaturs
 */
void sendTemperatureValues( WebServer &server ) {
  for ( int i = 0 ; i < foundSensors ; i++ ) {
    server << "Temperature " << (i+1) << " : <b>" << currentTemperature[i] << "C</b> (seuil [" << seuilTemperature[i] << "C])<br />" << endl;
  }
}

/**
 * Get temperature values from connected sensors and generate the XML
 */
void sendXMLValues(WebServer &server) {
  server << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
    << "<sensors time=\"" << millis() << "\">";
  for ( int i = 0 ; i < foundSensors ; i++ ) {
    server << "    <sensor id=\"" << (i+1) << "\" value=\"" << currentTemperature[i] << "\" type=\"C\" seuil=\"" << seuilTemperature[i] << "\"/>\n";
  }    
  server << "</sensors>" << endl;
}

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
}

/**
 * Send mail.
 */
void sendMail() {
  for (int i = 0 ; i < foundSensors ; i++ ) {
    Serial << "Temp" << (i+1) << " [" << currentTemperature[i] << "], seuil [" << seuilTemperature[i] << "]" << endl;
  }
  Serial << "Envoi d'un mail ..." << endl;
  if (smtpClient.connect()) {
    smtpClient << "HELO beerduino" << endl;
    delay(SMTP_TEMPO);

    smtpClient << "MAIL FROM:serge.simon@free.fr" << endl; /* identify sender */
    delay(SMTP_TEMPO);

    smtpClient << "RCPT TO:serge.simon@free.fr" << endl; /* identify recipient */
    delay(SMTP_TEMPO);

    smtpClient << "AUTH LOGIN PLAIN CRAM-MD5" << endl; /* a tester si besoin d'authentification sur smtp.free.fr */
    delay(SMTP_TEMPO);

    // smtpClient << "8498e6ecf343c526c98388bbf310044a" << endl; /* serge.simon encodé en MD5 */
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
  }
}


