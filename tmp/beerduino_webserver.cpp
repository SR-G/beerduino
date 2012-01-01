// Setup a webduino web server
WebServer webserver(PREFIX, WEBSERVER_PORT);

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





