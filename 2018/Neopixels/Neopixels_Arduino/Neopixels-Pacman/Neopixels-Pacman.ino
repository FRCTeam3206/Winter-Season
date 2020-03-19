By: Steve Olson 2/12/2018

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
Adafruit_NeoPixel strip = Adafruit_NeoPixel(60, 6);

int redcounter = 0;
int yellowcounter = 0;
int catchpoint=10;
int speedratio =2;
int speed=400;
int DIN2=2;
int digitalInput2;
bool pacgo=false;
 
void rlight(){
 strip.setPixelColor(redcounter,255,0,0);
 redcounter++;
 strip.show();
 delay(speed/2);
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

TimedAction redlighton = TimedAction(speed,rlight);
TimedAction yellowlighton = TimedAction(speed*speedratio,yellowlight);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  //pinMode(DIN0, INPUT_PULLUP);
  //pinMode(DIN1, INPUT_PULLUP);
  pinMode(DIN2 ,INPUT_PULLUP);
  strip.begin();
  
  for (int i=0; i <60;i++){
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
   for (int i=0; i <60;i++){
    strip.setPixelColor(i,255,0,0);
  }
  strip.show();
  delay(500);
  for (int i=0; i <60;i++){
    strip.setPixelColor(i,0,0,0);
  }
  strip.show();
  delay(500);
}

void pacman(){
  if(yellowcounter==0){
   yellowlighton.check();
   yellowcounter++;
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
      pacgo=false;
      delay(1000);
      for (int i=0; i <60;i++){
        strip.setPixelColor(i,0,0,255);
      }
      strip.show();
      delay(1000);
      redcounter=0;
      yellowcounter=0;
   
 }

}
//}
