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

  // For a set of NeoPixels the first NeoPixel is 0, second is 1, all the way up to the count of pixels minus one.
  byte array_B[] = {1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 17, 20, 23, 25, 26, 28, 29};
  byte array_BOdd[] = {8, 9, 10, 11, 12, 13, 14, 17, 20, 23, 24, 27, 30, 34, 35, 37, 38};
  byte array_G[] = {2, 3, 4, 5, 6, 8, 14, 17, 20, 23, 24, 27, 30, 34, 36, 37, 38};
  byte array_O[] = {1, 2, 3, 4, 5, 6, 7, 8, 14, 17, 23, 24, 30, 33, 34, 35, 36, 37, 38, 39};
  byte array_P[] = {1, 2, 3, 4, 5, 6, 7, 11, 14, 17, 20, 27, 30, 34, 35};
  byte array_R[] = {1, 2, 3, 4, 5, 6, 7, 11, 14, 17, 20, 24, 25, 26, 28, 29}; 
  byte array_ROdd[] = {8, 9, 10, 11, 12, 13, 14, 17, 20, 27, 30, 34, 35, 37, 38, 39}; 
  byte array_W[] = {1, 2, 3, 4, 5, 6, 8, 21, 22, 24, 33, 34, 35, 36, 37, 38};
  byte array_WOdd[] = {9, 10, 11, 12, 13, 14, 23, 25, 26, 39, 41, 42, 43, 44, 45, 46};
  byte array_Y[] = {1, 2, 3, 11, 20, 21, 22, 23, 27, 33, 34, 35};
  byte array_a[] = {3, 4, 5, 6, 11, 13, 18, 20, 25, 26, 27, 28};
  byte array_aOdd[] = {9, 10, 11, 12, 18, 20, 27, 29, 35, 36, 37, 38};
  byte array_c[] = {3, 4, 5, 9, 13, 18, 22, 25, 29};
  byte array_cOdd[] = {10, 11, 12, 18, 22, 25, 29, 34, 38};
  byte array_d[] = {2, 3, 4, 5, 6, 9, 13, 18, 22, 26, 27, 28};
  byte array_dOdd[] = {9, 10, 11, 12, 13, 18, 22, 25, 29, 35, 36, 37};
  byte array_e[] = {2, 3, 4, 5, 6, 9, 11, 13, 18, 22};
  byte array_eOdd[] = {9, 10, 11, 12, 13, 18, 20, 22, 25, 29};
  byte array_g[] = {3, 4, 5, 9, 13, 18, 20, 22, 26, 27};
  byte array_gOdd[] = {10, 11, 12, 18, 22, 25, 27, 29, 34, 36, 37, 38};
  byte array_h[] = {2, 3, 4, 5, 6, 11, 20, 25, 26, 27, 28, 29};
  byte array_hOdd[] = {9, 10, 11, 12, 13, 20, 27, 34, 35, 36, 37, 38};
  byte array_i[] = {2, 6, 9, 10, 11, 12, 13, 18, 22};
  byte array_iOdd[] = {9, 13, 18, 19, 20, 21, 22, 25, 29};
  byte array_l[] = {2, 3, 4, 5, 6, 9, 22};
  byte array_lOdd[] = {9, 10, 11, 12, 13, 22, 25};
  byte array_o[] = {2, 3, 4, 5, 6, 9, 13, 18, 22, 25, 26, 27, 28, 29};
  byte array_oOdd[] = {9, 10, 11, 12, 13, 18, 22, 25, 29, 34, 35, 36, 37, 38};
  byte array_n[] = {2, 3, 4, 5, 6, 12, 20, 25, 26, 27, 28, 29};
  byte array_nOdd[] = {9, 10, 11, 12, 13, 19, 27, 34, 35, 36, 37, 38};
  byte array_k[] = {2, 3, 4, 5, 6, 11, 19, 21, 25, 29}; 
  byte array_kOdd[] = {9, 10, 11, 12, 13, 20, 26, 28, 34, 38}; 
  byte array_p[] = {2, 3, 4, 5, 6, 11, 13, 18, 20, 28};
  byte array_pOdd[] = {9, 10, 11, 12, 13, 18, 20, 27, 29, 35};
  byte array_r[] = {2, 3, 4, 5, 6, 11, 13, 18, 20, 21, 25, 28};
  byte array_rOdd[] = {9, 10, 11, 12, 13, 18, 20, 26, 27, 29, 35, 38};
  byte array_t[] = {2, 9, 10, 11, 12, 13, 18};
  byte array_tOdd[] = {13, 18, 19, 20, 21, 22, 29};
  byte array_u[] = {2, 3, 4, 5, 9, 22, 26, 27, 28, 29}; 
  byte array_uOdd[] = {10, 11, 12, 13, 22, 25, 34, 35, 36, 37}; 
  byte array_w[] = {2, 3, 4, 5, 9, 20, 21, 25, 34, 35, 36, 37};
  byte array_wOdd[] = {10, 11, 12, 13, 22, 26, 27, 38, 42, 43, 44, 45};
  byte arrayMatrix[NUMPIXELS];

// Words to spell:
// Purple, Yellow, Orange, White, Green, Black, Blue, Pink, Red


void setup() {
  // This is for Trinket 5V 16MHz, you can remove these three lines if you are not using a Trinket
  #if defined (__AVR_ATtiny85__)
    if (F_CPU == 16000000) clock_prescale_set(clock_div_1);
  #endif
  // End of trinket special code
  pixels.begin(); // This initializes the NeoPixel library.
    backgroundDisplay (0,0,0); // rgb off
    pixels.show();
}

void loop() {
//    backgroundDisplay (16,1,0); // rgb off -- this is a good orange
    backgroundDisplay (16,1,0); // rgb off
    arrayRed(0,1,8);        // rgb on
    pixels.show();
    delay(500);

    backgroundDisplay (0,0,0); // rgb off
    arrayPink(0,2,2);        // rgb on
    pixels.show();
    delay(500);

    backgroundDisplay (0,0,0); // rgb off
    arrayBlue(8,1,0);        // rgb on
    pixels.show();
    delay(500);

    backgroundDisplay (0,0,0); // rgb off
    arrayBlack(3,2,0);        // rgb on
    pixels.show();
    delay(500);

    backgroundDisplay (0,0,0); // rgb off
    arrayGreen(2,0,2);        // rgb on
    pixels.show();
    delay(500);

    backgroundDisplay (0,0,0); // rgb off
    arrayWhite(0,0,4);        // rgb on
    pixels.show();
    delay(500);

    backgroundDisplay (0,0,0); // rgb off
    arrayOrange(0,2,0);        // rgb on
    pixels.show();
    delay(500);

    backgroundDisplay (0,0,0); // rgb off
    arrayYellow(8,0,2);        // rgb on
    pixels.show();
    delay(500);

    backgroundDisplay (0,0,0); // rgb off
    arrayPurple(4,0,0);        // rgb on
    pixels.show();
    delay(500);
 
} // End of main loop()

void backgroundDisplay(int rOff, int gOff, int bOff) {
    for(int i=0; i<NUMPIXELS; i++){
      pixels.setPixelColor(i, pixels.Color(rOff, gOff, bOff)); // Sets all pixels off.
    } 
    pixels.show();
}
/*  This fails because pointers don't work this way
void displayLetterP(byte *arrayLetter) {        // *arrayLetter is pointer to desired letter 
  for(int i = 0; i < 256; i++){     // Loops through all the pixels in one letter.
    int startCol = colShift + array_P[i]/8;     // integer divison should only leave whole number (no remainder)
    int startPos = array_P[i] % 16;             // position in 16
    bool secondHalf = startPos / 8;             // second half of two pixel line
    int startCell = startCol * 8;               // starting cell
    bool oddShift = colShift % 2;               // shift is odd.  This is really a bool
    if (oddShift)                               // The intermediate oddShift is never used again.  It could be consolidated.
    {
      if (secondHalf) {z = 15;} else {z = 7;}   // z is a temp variable
      array_P[i] = startCell - startPos + z;
    } 
    else // Shift is even or zero columns
    {
      if (secondHalf) {z = 8;} else {z = 0;}
      array_P[i] = startCell + startPos - z;
    }
    pixels.setPixelColor(array_P[i], pixels. Color(r, g, b)); 
  }
  
  pixels.show();

} */

void arrayRed(int rOn, int gOn, int bOn) {
  int j = 0;         // temp index for arrayMatrix

  byte colshift = 8;
  for(int i = 0; i < sizeof(array_ROdd); i++){                       
    arrayMatrix[j] = array_ROdd[i] + colshift*8;
    j++;
  }

  colshift = 14;
  for(int i = 0; i < sizeof(array_e); i++){                       
    arrayMatrix[j] = array_e[i] + colshift*8;
    j++;
  }

  colshift = 18;
  for(int i = 0; i < sizeof(array_d); i++){                       
    arrayMatrix[j] = array_d[i] + colshift*8;
    j++;
  }

  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rOn, gOn, bOn)); 
  }
}

void arrayPink(int rOn, int gOn, int bOn) {
  int j = 0;         // temp index for arrayMatrix

  byte colshift = 6;
  for(int i = 0; i < sizeof(array_P); i++){                       
    arrayMatrix[j] = array_P[i] + colshift*8;
    j++;
  }

  colshift = 12;
  for(int i = 0; i < sizeof(array_i); i++){                       
    arrayMatrix[j] = array_i[i] + colshift*8;
    j++;
  }

  colshift = 16;
  for(int i = 0; i < sizeof(array_n); i++){                       
    arrayMatrix[j] = array_n[i] + colshift*8;
    j++;
  }

  colshift = 20;
  for(int i = 0; i < sizeof(array_kOdd); i++){                       
    arrayMatrix[j] = array_kOdd[i] + colshift*8;
    j++;
  }

  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rOn, gOn, bOn)); 
  }
}

void arrayBlue(int rOn, int gOn, int bOn) {
  int j = 0;         // temp index for arrayMatrix

  byte colshift = 6;
  for(int i = 0; i < sizeof(array_BOdd); i++){                       
    arrayMatrix[j] = array_BOdd[i] + colshift*8;
    j++;
  }

  colshift = 12;
  for(int i = 0; i < sizeof(array_l); i++){                       
    arrayMatrix[j] = array_l[i] + colshift*8;
    j++;
  }

  colshift = 16;
  for(int i = 0; i < sizeof(array_u); i++){                       
    arrayMatrix[j] = array_u[i] + colshift*8;
    j++;
  }

  colshift = 20;
  for(int i = 0; i < sizeof(array_eOdd); i++){                       
    arrayMatrix[j] = array_eOdd[i] + colshift*8;
    j++;
  }

  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rOn, gOn, bOn)); 
  }
}

void arrayBlack(int rOn, int gOn, int bOn) {
  int j = 0;         // temp index for arrayMatrix

  byte colshift = 4;
  for(int i = 0; i < sizeof(array_B); i++){                       
    arrayMatrix[j] = array_B[i] + colshift*8;
    j++;
  }

  colshift = 8;
  for(int i = 0; i < sizeof(array_lOdd); i++){                       
    arrayMatrix[j] = array_lOdd[i] + colshift*8;
    j++;
  }

  colshift = 12;
  for(int i = 0; i < sizeof(array_aOdd); i++){                       
    arrayMatrix[j] = array_aOdd[i] + colshift*8;
    j++;
  }

  colshift = 18;
  for(int i = 0; i < sizeof(array_c); i++){                       
    arrayMatrix[j] = array_c[i] + colshift*8;
    j++;
  }

  colshift = 22;
  for(int i = 0; i < sizeof(array_kOdd); i++){                      
    arrayMatrix[j] = array_kOdd[i] + colshift*8;
    j++;
  }

  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rOn, gOn, bOn)); 
  }
}

void arrayGreen(int rOn, int gOn, int bOn) {
  int j = 0;         // temp index for arrayMatrix

  byte colshift = 4;
  for(int i = 0; i < sizeof(array_G); i++){                       
    arrayMatrix[j] = array_G[i] + colshift*8;
    j++;
  }

  colshift = 10;
  for(int i = 0; i < sizeof(array_r); i++){                       
    arrayMatrix[j] = array_r[i] + colshift*8;
    j++;
  }

  colshift = 14;
  for(int i = 0; i < sizeof(array_eOdd); i++){                       
    arrayMatrix[j] = array_eOdd[i] + colshift*8;
    j++;
  }

  colshift = 18;
  for(int i = 0; i < sizeof(array_eOdd); i++){                       
    arrayMatrix[j] = array_eOdd[i] + colshift*8;
    j++;
  }

  colshift = 22;
  for(int i = 0; i < sizeof(array_nOdd); i++){                      
    arrayMatrix[j] = array_nOdd[i] + colshift*8;
    j++;
  }

  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rOn, gOn, bOn)); 
  }
}

void arrayWhite(int rOn, int gOn, int bOn) {
  int j = 0;         // temp index for arrayMatrix

  byte colshift = 4;
  for(int i = 0; i < sizeof(array_WOdd); i++){                       
    arrayMatrix[j] = array_WOdd[i] + colshift*8;
    j++;
  }

  colshift = 10;
  for(int i = 0; i < sizeof(array_hOdd); i++){                       
    arrayMatrix[j] = array_hOdd[i] + colshift*8;
    j++;
  }

  colshift = 16;
  for(int i = 0; i < sizeof(array_i); i++){                       
    arrayMatrix[j] = array_i[i] + colshift*8;
    j++;
  }

  colshift = 20;
  for(int i = 0; i < sizeof(array_t); i++){                       
    arrayMatrix[j] = array_t[i] + colshift*8;
    j++;
  }

  colshift = 24;
  for(int i = 0; i < sizeof(array_e); i++){                      
    arrayMatrix[j] = array_e[i] + colshift*8;
    j++;
  }

  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rOn, gOn, bOn)); 
  }
}

void arrayOrange(int rOn, int gOn, int bOn) {
  int j = 0;         // temp index for arrayMatrix

  byte colshift = 2;
  for(int i = 0; i < sizeof(array_O); i++){                       
    arrayMatrix[j] = array_O[i] + colshift*8;
    j++;
  }

  colshift = 8;
  for(int i = 0; i < sizeof(array_r); i++){                       
    arrayMatrix[j] = array_r[i] + colshift*8;
    j++;
  }

  colshift = 12;
  for(int i = 0; i < sizeof(array_aOdd); i++){                       
    arrayMatrix[j] = array_aOdd[i] + colshift*8;
    j++;
  }

  colshift = 18;
  for(int i = 0; i < sizeof(array_n); i++){                       
    arrayMatrix[j] = array_n[i] + colshift*8;
    j++;
  }

  colshift = 22;
  for(int i = 0; i < sizeof(array_gOdd); i++){                      
    arrayMatrix[j] = array_gOdd[i] + colshift*8;
    j++;
  }

  colshift = 28;
  for(int i = 0; i < sizeof(array_e); i++){                      
    arrayMatrix[j] = array_e[i] + colshift*8;
    j++;
  }

  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rOn, gOn, bOn)); 
  }
}

void arrayYellow(int rOn, int gOn, int bOn) {
  int j = 0;         // temp index for arrayMatrix

  byte colshift = 2;
  for(int i = 0; i < sizeof(array_Y); i++){                       
    arrayMatrix[j] = array_Y[i] + colshift*8;
    j++;
  }

  colshift = 8;
  for(int i = 0; i < sizeof(array_e); i++){                       
    arrayMatrix[j] = array_e[i] + colshift*8;
    j++;
  }

  colshift = 12;
  for(int i = 0; i < sizeof(array_l); i++){                       
    arrayMatrix[j] = array_l[i] + colshift*8;
    j++;
  }

  colshift = 16;
  for(int i = 0; i < sizeof(array_l); i++){                       
    arrayMatrix[j] = array_l[i] + colshift*8;
    j++;
  }

  colshift = 20;
  for(int i = 0; i < sizeof(array_o); i++){                      
    arrayMatrix[j] = array_o[i] + colshift*8;
    j++;
  }

  colshift = 24;
  for(int i = 0; i < sizeof(array_wOdd); i++){                      
    arrayMatrix[j] = array_wOdd[i] + colshift*8;
    j++;
  }

  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rOn, gOn, bOn)); 
  }
}

void arrayPurple(int rOn, int gOn, int bOn) {
  int j = 0;         // temp index for arrayMatrix

  byte colshift = 2;
  for(int i = 0; i < sizeof(array_P); i++){                       
    arrayMatrix[j] = array_P[i] + colshift*8;
    j++;
  }

  colshift = 8;
  for(int i = 0; i < sizeof(array_u); i++){                       
    arrayMatrix[j] = array_u[i] + colshift*8;
    j++;
  }

  colshift = 12;
  for(int i = 0; i < sizeof(array_rOdd); i++){                       
    arrayMatrix[j] = array_rOdd[i] + colshift*8;
    j++;
  }

  colshift = 18;
  for(int i = 0; i < sizeof(array_p); i++){                     
    arrayMatrix[j] = array_p[i] + colshift*8;
    j++;
  }

  colshift = 22;
  for(int i = 0; i < sizeof(array_lOdd); i++){                       
    arrayMatrix[j] = array_lOdd[i] + colshift*8;
    j++;
  }

  colshift = 26;
  for(int i = 0; i < sizeof(array_eOdd); i++){                      
    arrayMatrix[j] = array_eOdd[i] + colshift*8;
    j++;
  }

  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rOn, gOn, bOn)); 
  }
}

