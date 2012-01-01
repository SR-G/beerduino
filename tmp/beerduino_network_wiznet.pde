#include <Ethernet.h>
#include <SPI.h>

// Webserver for XML results handling
Server server(80);

// Standard ethernet parameters
static byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
static byte subnet[] = { 255, 255, 255, 0 };

// Specific ethernet parameters
static byte ip[] = { 192, 168, 1, 101 };
static byte gateway[] = { 192, 168, 1, 1 };
// byte ip[] = { 10, 90, 156, 160 }; // ip PSA
// byte gateway[] = { 10, 90, 156, 254 }; // gateway PSA

/**
 * Setup
 */
void setupNetwork() {
  // start SPI and Ethernet
  Serial << "Initializing ethernet ...";
  delay(1000);
  SPI.begin();
  // Ethernet.begin(mac, ip, gateway, subnet);
  Ethernet.begin(mac, ip);
  delay(500);
  Serial << " DONE" << endl;

  // start the webserver
  Serial << "Initializing webserver on port " << WEBSERVER_PORT << " ...";  
  server.begin();
  delay(100);
  Serial << " DONE" << endl;
}
