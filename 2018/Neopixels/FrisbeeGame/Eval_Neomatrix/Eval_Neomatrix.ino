// NeoPixel Ring simple sketch (c) 2013 Shae Erisson
// released under the GPLv3 license to match the rest of the AdaFruit NeoPixel library

#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif

// Which pin on the Arduino is connected to the NeoPixels?
// On a Trinket or Gemma we suggest changing this to 1
#define PIN            8

// How many NeoPixels are attached to the Arduino?
#define NUMPIXELS      256

// When we setup the NeoPixel library, we tell it how many pixels, and which pin to use to send signals.
// Note that for older NeoPixel strips you might need to change the third parameter--see the strandtest
// example for more information on possible values.
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);

int delayval = 10; // delay for half a second
int r = 16;
int g = 0;
int b = 16;
int z = 0;
int colShift = 1; // Number of columns to shift

  // For a set of NeoPixels the first NeoPixel is 0, second is 1, all the way up to the count of pixels minus one.
  byte array_Y[] = {0, 1, 2, 12, 19, 20, 21, 22, 28, 32, 33, 34};
  byte array_P[] = {0, 1, 2, 3, 4, 5, 6, 12, 15, 16, 19, 28, 31, 33, 34};
  byte array_B[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 12, 15, 16, 19, 23, 25, 26, 27, 29, 30};
  byte array_l[] = {3, 4, 5, 6, 7, 8, 23};
  byte array_u[] = {3, 4, 5, 6, 8, 23, 25, 26, 27, 28}; 
  byte array_e[] = {3, 4, 5, 6, 7, 8, 10, 12, 19, 21, 23};
  byte array_r[] = {3, 4, 5, 6, 7, 10, 12, 19, 21, 22, 24, 27};
  byte array_o[] = {3, 4, 5, 6, 7, 8, 12, 19, 23, 24, 25, 26, 27, 28};
  byte array_w[] = {3, 4, 5, 6, 8, 21, 22, 24, 35, 36, 37, 38};
  byte array_a[] = {4, 5, 6, 7, 10, 12, 19, 21, 24, 25, 26, 27};
  byte array_n[] = {3, 4, 5, 6, 7, 11, 21, 25, 35, 36, 37, 38, 39};
  byte array_g[] = {4, 5, 6, 8, 12, 19, 21, 23, 25, 26};
  byte array_R[] = {0, 1, 2, 3, 4, 5, 6, 7, 12, 15, 16, 19, 24, 25, 26, 27, 29, 30}; 
  byte array_d[] = {3, 4, 5, 6, 7, 8, 12, 19, 23, 25, 26, 27};
  byte array_G[] = {1, 2, 3, 4, 5, 6, 8, 15, 16, 20, 23, 24, 27, 31, 33, 36, 37, 38};
  byte array_W[] = {0, 1, 2, 3, 4, 5, 6, 8, 21, 22, 24, 32, 33, 34, 35, 36, 37, 38};
  byte array_h[] = {3, 4, 5, 6, 7, 10, 21, 24, 25, 26, 27, 28};
  byte array_i[] = {3, 7, 8, 9, 10, 11, 12, 19, 23};
  byte array_t[] = {3, 8, 9, 10, 11, 12, 19};
  byte array_k[] = {3, 4, 5, 6, 7, 10, 20, 22, 24, 28}; 


void setup() {
  // This is for Trinket 5V 16MHz, you can remove these three lines if you are not using a Trinket
#if defined (__AVR_ATtiny85__)
  if (F_CPU == 16000000) clock_prescale_set(clock_div_1);
#endif
  // End of trinket special code

  pixels.begin(); // This initializes the NeoPixel library.
  for(int i=0;i<NUMPIXELS;i++){

    // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255
    pixels.setPixelColor(i, pixels.Color(0,0,0));  // Blanks display.

    pixels.show();
    }
}

void loop() {

for(int i = 0; i < sizeof(array_P); i++){  // Loops through all the pixels in one letter.

  int startCol = colShift + array_P[i]/8;     // integer divison should only leave whole number (no remainder)
  int startPos = array_P[i] % 16;             // position in 16
  bool secondHalf = startPos / 8;             // second half of two pixel line
  int startCell = startCol * 8;               // starting cell
  bool oddShift = colShift % 2;               // shift is odd.  This is really a bool
  if (oddShift)             
  {
    if (secondHalf) {z = 15;} else {z = 7;}   // z is a temp variable
    array_P[i] = startCell - startPos + z;
  } 
  else // Shift is even or zero columns
  {
    if (secondHalf) {z = -8;} else {z = 0;}
    array_P[i] = startCell + startPos + z;
  }
    
  pixels.setPixelColor(array_P[i], pixels. Color(r, g, b)); 
}
 
  pixels.show();
  
  delay(1000);
    for(int i=0;i<NUMPIXELS;i++){

    // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255
    pixels.setPixelColor(i, pixels.Color(0,0,0)); 

    } // This blanks display.
    pixels.show();

//}


  
/*
  for(int i=0;i<NUMPIXELS;i++){

    // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255
    pixels.setPixelColor(i, pixels.Color(0,0,0)); // Moderately bright green color.

    pixels.show(); // This sends the updated pixel color to the hardware.

    //delay(delayval); // Delay for a period of time (in milliseconds).

   for(int i=0; i<3; i++){ //works
    pixels.setPixelColor(i, pixels.Color(r, g, b));
   }
   pixels.setPixelColor(12, pixels.Color(r, g, b));
   pixels.setPixelColor(28, pixels.Color(r, g, b));
   for(int i=19; i<23; i++){
    pixels.setPixelColor(i, pixels.Color(r, g, b));
   }  
   for(int i=32; i<35; i++){
    pixels.setPixelColor(i, pixels.Color(r, g, b));
   }    
   pixels.show();
   
   pixels.setPixelColor(0, pixels.Color(0,15,0));
   pixels.setPixelColor(1, pixels.Color(0,15,0));
   */                                      
  }
