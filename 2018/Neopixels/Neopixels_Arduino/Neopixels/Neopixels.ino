<<<<<<< HEAD
#include <TimedAction.h>
#include <Adafruit_NeoPixel.h>
/*
int DIN0 = 2;
int DIN1 = 3; //Connection to the Roborio
int DIN2 = 4;

int digitalInput0 = 0;
int digitalInput1 = 0;
int digitalInput2 = 0;
int choice =0;
char Color = 'r';
*/

//TimedAction redlight = TimedAction(200,redlight);
//TimedAction yellowlight = TimedAction(400,yellowlight);

//public int redcounter = 0;
//public int yellowcounter = 0;

Adafruit_NeoPixel strip = Adafruit_NeoPixel(60, 6);
 
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  //pinMode(DIN0, INPUT_PULLUP);
  //pinMode(DIN1, INPUT_PULLUP);
  //pinMode(DIN2 ,INPUT_PULLUP);
  strip.begin();
  
  for (int i=0; i <60;i++){
    strip.setPixelColor(i,0,0,255);
  }
=======
int DIN0 = 2;
int DIN1 = 3; //Connection to the Roborio
int digitalInput = 0;
int choice =0;
char Color = 'r';

void setup() {
  // put your setup code here, to run once:
Serial.begin(9600);
pinMode(DIN0, INPUT_PULLUP);
pinMode(DIN1, INPUT_PULLUP);
>>>>>>> 0d3040d49709e954ba2ab599d49394b0065e51d6
}

void loop() {
// Read Digital Inputs
<<<<<<< HEAD
//digitalInput0 = digitalRead(DIN0);
//digitalInput1 = digitalRead(DIN1);
//digitalInput2 = digitalRead(DIN2);
//choice=digitalInput0+2*digitalInput1+4*digitalInput2;
//Take action on Input


//yellowlight.check();
//if (yellowcounter>10){
//redlight.check();
//}

}


//void redlight(){
//strip.setPixelColor(0, 255,0,0);
//redcounter++;
//}

//void yellowlight(){
//strip.setPixelColor(0, 255,255,0);
//yellowcounter++;
//}
//}
//}
=======
digitalInput = digitalRead(DIN0);
//Take action on Input

switch(digitalInput){
digitalInput = digitalRead(DIN0);
  case 0:
Color ='r';
  break;
  case 1:
Color='b';
  break;
}

Serial.println(Color);
delay (1000); //delays for 1000ms
}
>>>>>>> 0d3040d49709e954ba2ab599d49394b0065e51d6
