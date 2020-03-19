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
int r = 15;
int g = 0;
int b = 15;
int z = 0;
int colShift = 2; // Number of columns to shift

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
  for(int i=0; i<NUMPIXELS; i++){

    // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255
    pixels.setPixelColor(i, pixels.Color(0,0,0)); 

    pixels.show();
    } // This blanks display.
}

void loop() {

for(int i = 0; i < sizeof(array_Y); i++){  // Loops through all the pixels in one letter.
//  // Shifts the characters to the right "col".  Jessica's code. Works.         
    int j = (2*(8-(array_Y[i] % 8)) - 1);
    array_Y[i] = j + array_Y[i];}
    pixels.setPixelColor(array_Y[i], pixels.Color(r, g, b));
  }

// The downside to the above code is that, in order to shift multiple columns, the code needs to be looped.

  pixels.show();
  
  delay(1000);
    for(int i=0;i<NUMPIXELS;i++){

    // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255
    pixels.setPixelColor(i, pixels.Color(0,0,0)); 

    } // This blanks display.
    pixels.show();

}
