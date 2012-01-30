/*
 The circuit:
 * LED attached from pin 13 to ground 
 * pushbutton attached to pin 2 from +5V
 * 10K resistor attached to pin 2 from ground
 
 * Note: on most Arduinos there is already an LED on the board
 attached to pin 13.
 
 http://www.arduino.cc/en/Tutorial/Button

  Interrupts tutorial : http://www.arduino.cc/en/Reference/AttachInterrupt
  Notes about frequency : http://www.arduino.cc/cgi-bin/yabb2/YaBB.pl?num=1208715493/11
  Event library : http://protolab.pbworks.com/w/page/19403605/ArduinoProcessingLibraryReference
  
*/

// constants won't change. They're used here to 
// set pin numbers:
const int buttonPin = 2;     // the number of the pushbutton pin
const int ledPin =  13;      // the number of the LED pin

// variables will change:
volatile int buttonState = 0;         // variable for reading the pushbutton status
volatile int lastButtonState = 0;     // previous state of the button
volatile long buttonPushCount = 0;
volatile long pinReadingCount = 0;

volatile int state = HIGH;   // which state should generate the trigger

void setup() {
  // initialize the pushbutton pin as an input:
  pinMode(buttonPin, INPUT);     
  // initialize the LED pin as an output:
  pinMode(ledPin, OUTPUT);      

  attachInterrupt(0, triggered, CHANGE); // 0 => trigger on pin 2
  
  Serial.begin(9600);
  Serial.println("Starting Arduino counter ...");

  // turn LED on:    
  digitalWrite(ledPin, HIGH);  
}

void triggered() {
  buttonState = digitalRead(buttonPin);
  pinReadingCount++;
  if (buttonState != lastButtonState) {  
    // check if the pushbutton is pressed.
    // if it is, the buttonState is HIGH:
    if (buttonState == HIGH) {     
      Serial.print("COUNTER ACTIVATED for the [");
      Serial.print(buttonPushCount++);
      Serial.print("]th times / total on or off acquisitions count [");
      Serial.print(pinReadingCount);
      Serial.println("] ");
    }
  }
  lastButtonState = buttonState;
}

void loop() {
  if ( buttonState == LOW ) {
    // turn LED off:
    digitalWrite(ledPin, LOW); 
  } else { 
    // turn LED on:    
    digitalWrite(ledPin, HIGH);
  }  
}

/*
void loop(){
  // read the state of the pushbutton value:
  buttonState = digitalRead(buttonPin);
  pinReadingCount++;

 // compare the buttonState to its previous state
  if (buttonState != lastButtonState) {  
    // check if the pushbutton is pressed.
    // if it is, the buttonState is HIGH:
    if (buttonState == HIGH) {     
      // turn LED on:    
      digitalWrite(ledPin, HIGH);  

      Serial.print("COUNTER ACTIVATED for the [");
      Serial.print(buttonPushCount++);
      Serial.print("]th times on [");
      Serial.print(pinReadingCount);
      Serial.println("] acquisitions");

      // turn LED off:
      digitalWrite(ledPin, LOW); 
    } 
    else {
    }
  }

  // save the current state as the last state, 
  //for next time through the loop
  lastButtonState = buttonState;
}
*/

