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
int x;
int colShift = 1; // Number of columns to shift
int colShift2 = 7; // Number of columns to shift

  // For a set of NeoPixels the first NeoPixel is 0, second is 1, all the way up to the count of pixels minus one.
  byte array_B[] = {1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 17, 20, 23, 25, 26, 28, 29};
  byte array_G[] = {2, 3, 4, 5, 6, 8, 14, 17, 20, 23, 25, 26, 27, 29};
  byte array_P[] = {0, 1, 2, 3, 4, 5, 6, 12, 15, 16, 19, 28, 31, 33, 34};
  byte array_R[] = {1, 2, 3, 4, 5, 6, 7, 11, 14, 17, 20, 24, 25, 26, 28, 29}; 
  byte array_W[] = {1, 2, 3, 4, 5, 6, 8, 21, 22, 24, 33, 34, 35, 36, 37, 38};
  byte array_Y[] = {1, 2, 3, 11, 20, 21, 22, 23, 27, 33, 34, 35};

  byte array_l[] = {3, 4, 5, 6, 7, 8, 23};
  byte array_u[] = {3, 4, 5, 6, 8, 23, 25, 26, 27, 28}; 
  byte array_e[] = {3, 4, 5, 6, 7, 8, 10, 12, 19, 21, 23};
  byte array_r[] = {3, 4, 5, 6, 7, 10, 12, 19, 21, 22, 23, 27};
  byte array_o[] = {3, 4, 5, 6, 7, 8, 12, 19, 23, 24, 25, 26, 27, 28};
  byte array_w[] = {3, 4, 5, 6, 8, 21, 22, 24, 35, 36, 37, 38};
  byte array_a[] = {4, 5, 6, 7, 10, 12, 19, 21, 24, 25, 26, 27};
  byte array_n[] = {3, 4, 5, 6, 7, 11, 21, 25, 35, 36, 37, 38, 39};
  byte array_g[] = {4, 5, 6, 8, 12, 19, 21, 23, 25, 26};
  byte array_d[] = {3, 4, 5, 6, 7, 8, 12, 19, 23, 25, 26, 27};
  byte array_h[] = {3, 4, 5, 6, 7, 10, 21, 24, 25, 26, 27, 28};
  byte array_i[] = {3, 7, 8, 9, 10, 11, 12, 19, 23};
  byte array_t[] = {3, 8, 9, 10, 11, 12, 19};
  byte array_k[] = {3, 4, 5, 6, 7, 10, 20, 22, 24, 28}; 

byte array_Letter[] = {3, 4, 5, 6, 7, 11, 21, 25, 35, 36, 37, 38, 39};
byte array_Letter2[] = {3, 4, 5, 6, 7, 10, 20, 22, 24, 28};

void setup() {
  // This is for Trinket 5V 16MHz, you can remove these three lines if you are not using a Trinket
  #if defined (__AVR_ATtiny85__)
    if (F_CPU == 16000000) clock_prescale_set(clock_div_1);
  #endif
  // End of trinket special code
    pixels.begin(); // Initializes the NeoPixel library.

    for(int i=0;i<NUMPIXELS;i++){                    // Blanks the NeoPixel display.
      pixels.setPixelColor(i, pixels.Color(0,0,0)); 
      pixels.show();
    } 
}

void loop() {
//  placeLetter(array_P);
// The code below allows an arbitrary number of columns of shift to be input.
// int placeLetter(byte array_P[]){                      // Use two arguments, the letter array and start column.  Return the next column.  Also color.

  for(int i = 0; i < sizeof(array_Letter); i++){         // Loops through all the pixels in one letter.
    int  startCol = colShift + array_Letter[i]/8;        // integer divison should only leave whole number (no remainder)
    int  startPos = array_Letter[i] % 16;                // position in 16
    bool secondHalf = startPos / 8;                      // second half of two pixel line
    int  startCell = startCol * 8;                       // starting cell
    bool oddShift = colShift % 2;                        // shift is odd.  This is really a bool
      if (oddShift)             
        {if (secondHalf) {z = 15;} else {z = 7;}}        // z is a temp variable
      else // Shift is even or zero columns
        {if (secondHalf) {z = 8;} else {z = 0;}}         // Could replace this nested IF with case or something fancy.
      array_Letter[i] = startCell - startPos + z;
        
      pixels.setPixelColor(array_Letter[i], pixels. Color(r, g, b)); 
    }


  for(int i = 0; i < sizeof(array_Letter2); i++){         // Loops through all the pixels in one letter.
    int  startCol2 = colShift2 + array_Letter2[i]/8;        // integer divison should only leave whole number (no remainder)
    int  startPos2 = array_Letter2[i] % 16;                // position in 16
    bool secondHalf2 = startPos2 / 8;                      // second half of two pixel line
    int  startCell2 = startCol2 * 8;                       // starting cell
    bool oddShift2 = colShift2 % 2;                        // shift is odd.  This is really a bool
      if (oddShift2)             
        {if (secondHalf2) {z = 15;} else {z = 7;}}   // z is a temp variable
      else // Shift is even or zero columns
        {if (secondHalf2) {z = 8;} else {z = 0;}}    // Could replace this nested IF with case or something fancy.
      array_Letter2[i] = startCell2 - startPos2 + z;
        
      pixels.setPixelColor(array_Letter2[i], pixels. Color(r, g, b)); 
    }

    
  pixels.show();
    delay(60000);
    for(int i=0;i<NUMPIXELS;i++){
      // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255
      pixels.setPixelColor(i, pixels.Color(0,0,0)); 
    } // This blanks display.
    pixels.show();

}

