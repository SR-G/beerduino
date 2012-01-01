
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

