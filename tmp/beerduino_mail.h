
#define SMTP_PORT 25

// SMTP tempo between each operation (in ms)
#define SMTP_TEMPO 250

byte smtpServer[] = { 212, 27, 48, 4 }; // smtp.free.fr
// byte smtpServer[] = { 10, 68, 184, 66 }; // smtp.inetpsa.com

// Setup a mail client
Client smtpClient(smtpServer, SMTP_PORT);

int overheat = 0; // Are limits reached

