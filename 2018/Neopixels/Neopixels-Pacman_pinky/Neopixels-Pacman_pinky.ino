

//By: Steve Olson 2/12/2018

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
Adafruit_NeoPixel strip = Adafruit_NeoPixel(128, 6);
int greencounter = 0;
int redcounter = 0;
int yellowcounter = 0;
int pinkcounter = 0;
int catchpoint=64; //catchpoint*speedratio should be < total pixels on strip
double  speedratio = 2;
int speed=200;
int DIN2=2;
int digitalInput2;
bool pacgo=true;
int pinkstart = 10;
int greenstart = 1; 
void rlight(){
 strip.setPixelColor(redcounter,112,72,12);
 redcounter-+;
 strip.show();
 delay(speed/4);
 strip.setPixelColor(redcounter-1, 0,0,0);
 Serial.println(redcounter);
}

void yellowlight(){
 strip.setPixelColor(yellowcounter, 255,255,0);
 yellowcounter++;
 strip.show();
 if (yellowcounter<catchpoint){
 delay(speed/2);
 strip.setPixelColor(yellowcounter-1, 0, 0, 0);
 }
 strip.setPixelColor(yellowcounter-2,0,0,0);
 Serial.println(yellowcounter);
}
void pinklight(){
  strip.setPixelColor(pinkcounter, 234,48,101);   // pixel color
  pinkcounter++;
  strip.show();   
  delay(speed/2);
  strip.setPixelColor(pinkcounter-1, 0,0,0);
  Serial.println(pinkcounter);
}

TimedAction redlighton = TimedAction(speed,rlight);  // First Value was speed*speedraito
TimedAction yellowlighton = TimedAction(speed*speedratio,yellowlight); // First Value was speed*speedraito
TimedAction pinklighton = TimedAction(speed*speedratio,pinklight);
//TimedAction bluelighton = TimedAction(speed/speedratio, bluelight);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  //pinMode(DIN0, INPUT_PULLUP);
  //pinMode(DIN1, INPUT_PULLUP);
  pinMode(DIN2 ,INPUT_PULLUP);
  strip.begin();
  
  for (int i=0; i <128;i++){
    strip.setPixelColor(i,0,0,255);
  }
  strip.show();
  delay(2000);
}
 void loop() {
// Read Digital Inputs
//digitalInput0 = digitalRead(DIN0);
//digitalInput1 = digitalRead(DIN1);
digitalInput2 = digitalRead(DIN2);
//choice=digitalInput0+2*digitalInput1+4*digitalInput2;
//Take action on Input

//strip.setPixelColor(12, 255, 255, 0);
//strip.show();
  if (digitalInput2==0){
    pacgo=true;
  }
  if (pacgo==true){
    pacman();
  }
  
}

void gameover(){
   for (int i=0; i <128;i++){
    strip.setPixelColor(i,255,0,0);
  }
  strip.show();
  delay(500);
  for (int i=0; i <128;i++){
    strip.setPixelColor(i,0,0,0);
  }
  strip.show();
  delay(500);
}

/*void newgame(){
  for (int i=0; i <128;i++){
    bluelighton.check();
    bluecounter++;
  }
}*/
  

void pacman(){
  if(yellowcounter==0){
   yellowlighton.check();
   //yellowcounter++;
  }
  if(yellowcounter>pinkstart){
    pinklighton.check();
    //pinkcounter++;
  }
  
  if(redcounter<yellowcounter){
    yellowlighton.check();
    if (yellowcounter>catchpoint){
      redlighton.check();
    }
  }
  if(redcounter==yellowcounter){
      gameover();
      gameover();
      gameover();
//      newgame();
      //pacgo=false;
     // delay(1000);
      for (int i=0; i <128;i++){
        strip.setPixelColor(i,0,0,255);
      }
      strip.show();
      delay(1000);
      redcounter=0;
      yellowcounter=0;
      pinkcounter=0;
   
 }


}
