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

/*
 *  Define structure for holding RGB then create colors.
 *  Colors are R, G, B.  Values of each can be 0 to 255.
 */
 
struct color {
  byte red;
  byte green;
  byte blue;
};

  color colorOff  = { 0, 0, 0};

  color dimRed        = {   4,   0,   0};
  color dimOrange     = {   6,   1,   0};
  color dimYellow     = {   3,   2,   0};
  color dimGreen      = {   0,   1,   0}; 
  color dimBlue       = {   0,   0,   1};
  color dimPurple     = {   2,   0,   2}; 
  color dimPink       = {   4,   1,   2}; 
  color dimWhite      = {   1,   1,   1};

  color medRed        = { 128,   0,   0};
  color medOrange     = { 128,  14,   0};
  color medYellow     = { 93,   64,   0};
  color medGreen      = { 0,   128,   0}; 
  color medGreenMint  = { 0,    128,  10}; 
  color medGreenSea   = { 0,    60,  25};
  color medBlue       = { 0,     0, 128};
  color medPurple     = { 48,    0, 128}; 
  color medPink       = { 96,   24,  48}; 
  color medWhite      = { 64,   64,  64}; 

  color briRed        = { 255,   0,   0};
  color briOrange     = { 255,  20,   0}; 
  color briYellow     = { 255, 160,   0}; 
  color briGreen      = { 0,   255,   0}; 
  color briBlue       = { 0,     0, 255};
  color briPurple     = { 128,   0, 255}; 
  color briPink       = { 255,  48,  96}; 
  color briWhite      = { 128, 128, 128}; 
 
//    backgroundDisplay (16,1,0); // rgb off -- this is a good orange

  // For a NeoPixels matrix, the first NeoPixel is 0, second is 1, all the way up to the count of pixels minus one.
  byte array_B[] = {1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 17, 20, 23, 25, 26, 28, 29};
  byte array_BOdd[] = {8, 9, 10, 11, 12, 13, 14, 17, 20, 23, 24, 27, 30, 34, 35, 37, 38};
  byte array_G[] = {2, 3, 4, 5, 6, 8, 14, 17, 20, 23, 24, 27, 30, 34, 36, 37, 38};
  byte array_O[] = {1, 2, 3, 4, 5, 6, 7, 8, 14, 17, 23, 24, 30, 33, 34, 35, 36, 37, 38, 39};
  byte array_J[] = {1, 6, 7, 8, 14, 17, 18, 19, 20, 21, 22, 23, 30, 33};
  byte array_P[] = {1, 2, 3, 4, 5, 6, 7, 11, 14, 17, 20, 27, 30, 34, 35};
  byte array_POdd[] = {8, 9, 10, 11, 12, 13, 14, 17, 20, 27, 30, 33, 36, 44, 45};
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
  byte array_k[] = {2, 3, 4, 5, 6, 11, 19, 21, 25, 29}; 
  byte array_kOdd[] = {9, 10, 11, 12, 13, 20, 26, 28, 34, 38}; 
  byte array_l[] = {2, 3, 4, 5, 6, 9, 22};
  byte array_lOdd[] = {9, 10, 11, 12, 13, 22, 25};
  byte array_n[] = {2, 3, 4, 5, 6, 12, 20, 25, 26, 27, 28, 29};
  byte array_nOdd[] = {9, 10, 11, 12, 13, 19, 27, 34, 35, 36, 37, 38};
  byte array_o[] = {2, 3, 4, 5, 6, 9, 13, 18, 22, 25, 26, 27, 28, 29};
  byte array_oOdd[] = {9, 10, 11, 12, 13, 18, 22, 25, 29, 34, 35, 36, 37, 38};
  byte array_p[] = {2, 3, 4, 5, 6, 11, 13, 18, 20, 28};
  byte array_pOdd[] = {9, 10, 11, 12, 13, 18, 20, 27, 29, 35};
  byte array_r[] = {2, 3, 4, 5, 6, 11, 13, 18, 20, 21, 25, 28};
  byte array_rOdd[] = {9, 10, 11, 12, 13, 18, 20, 26, 27, 29, 35, 38};
//  byte array_s[] = {};
  byte array_sOdd[] = {9, 12, 18, 20, 22, 25, 27, 29, 34, 37};
  byte array_t[] = {2, 9, 10, 11, 12, 13, 18};
  byte array_tOdd[] = {13, 18, 19, 20, 21, 22, 29};
  byte array_u[] = {2, 3, 4, 5, 9, 22, 26, 27, 28, 29}; 
  byte array_uOdd[] = {10, 11, 12, 13, 22, 25, 34, 35, 36, 37}; 
  byte array_w[] = {2, 3, 4, 5, 9, 20, 21, 25, 34, 35, 36, 37};
  byte array_wOdd[] = {10, 11, 12, 13, 22, 26, 27, 38, 42, 43, 44, 45};
  byte array_z[] = {2, 5, 6, 9, 11, 13, 18, 19, 22};
  byte array_zOdd[] = {9, 10, 13, 18, 20, 22, 25, 28, 29};
  byte arrayMatrix[NUMPIXELS];

  byte array_dieOne[] = {27, 28, 35, 36};
  byte array_dieTwo[] = {6, 7, 8, 9, 48, 49, 62, 63};
  byte array_dieThree[] = {6, 7, 8, 9, 27, 28, 35, 36, 48, 49, 62, 63};
  byte array_dieFour[] = {0, 1, 6, 7, 8, 9, 14, 15, 48, 49, 54, 55, 56, 57, 62, 63};
  byte array_dieFive[] = {0, 1, 6, 7, 8, 9, 14, 15, 27, 28, 35, 36, 48, 49, 54, 55, 56, 57, 62, 63};
  byte array_dieSix[] = {0, 1, 3, 4, 6, 7, 8, 9, 11, 12, 14, 15, 48, 49, 51, 52, 54, 55, 56, 57, 59, 60, 62, 63};
  byte array_dieEvent[]= {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 
  22, 23, 27, 28, 29, 34, 35, 36, 43, 44, 45, 49, 50, 51, 52, 53, 54 ,55, 56, 57, 58, 59, 60, 61, 62, 63, 
  65, 66 ,67, 68, 69, 70, 71};

  byte array_JustusR[] = {1, 2, 3, 4, 5, 6, 7, 11, 12, 14, 17, 19, 20, 21, 25, 28, 30, 34, 39};
  byte array_JustusA[] = {71, 73, 85, 91, 99, 109, 113, 125, 131, 139, 149, 153, 167, 
  100, 107, 116, 123, 132};
  byte array_JustusJ[] = {185, 184, 199, 200, 215, 216, 217, 218, 219, 20, 221, 222, 
  193, 206, 209, 225, 238, 241};

  byte array_1dogA[]= {1, 2, 3, 4, 5, 6, 7, 11, 14, 17, 20, 27, 30 ,33, 34, 35, 36, 37, 38, 39};
  byte array_2dogn[]= {};  
  byte array_3dogn[]= {};
  byte array_4dogA[]={};

/* 
 * 
 * Purple, Yellow, Orange, White, Green, Black, Blue, Pink, Red
 * 
 */

/*
 * To do: try creating a perimeter. Left, Top and Right are always open.  Bottom sometimes has the first letter there
 * To do: try using the perimeter to keep score.  It would be a pain to create byte arrays for numbers and then create 2-digit words to 99.
 * To do: after initial dice roll, wait for Frisbee input and then roll again.
 * To do: add validation to dice random.  Add some flourish to dice roll.
 */
 
void setup() {

  pixels.begin(); // This initializes the NeoPixel library.

    backgroundDisplay (colorOff);
    pixels.show();
}

void loop() {
  
/*    color ev; // declare a temp color varible

    backgroundDisplay (colorOff);
    pixels.show();
    delay(100);

    backgroundDisplay1 (dimRed);
    backgroundDisplay2 (dimGreen);
    backgroundDisplay3 (dimWhite);
    byte dieOne = (byte)random(1,7);
    byte dieTwo = (byte)random(1,7);
    byte e = (byte)random(1,7);
    if(e <= 3) { // barbarian for 1 and 2 and 3
      ev = colorOff;
    }
    else if (e == 4) {
      ev = dimBlue; 
    }
    else if (e == 5) {
      ev = dimGreen;
    }
    else if (e == 6) {
      ev = dimRed;      
    }
    dice( dieOne, dieTwo, ev); // Only the Event die gets the color.
    pixels.show();
    delay(2000);
*/

/*    
      
    backgroundDisplay (colorOff);
    color rgb = briWhite;
    pixels.setPixelColor(32, pixels. Color(rgb.red, rgb.green, rgb.blue));
    pixels.setPixelColor(93, pixels. Color(rgb.red, rgb.green, rgb.blue));
    pixels.show();
    delay(5000);
    
*/
    backgroundDisplay (dimBlue);
    arrayPizza(dimOrange);       
    pixels.show();
    delay(3000);
  
    backgroundDisplay (dimGreen);
    arrayJustus(dimOrange);        
    pixels.show();
    delay(3000);

    backgroundDisplay (dimGreen);
    arrayRaj(dimBlue);        
    pixels.show();
    delay(3000);

  /*  backgroundDisplay (colorOff);
    arrayGreen(dimRed);        
    pixels.show();
    delay(3000);

   backgroundDisplay (colorOff);
    arrayWhite(dimGreen);        
    pixels.show();
    delay(3000);

   backgroundDisplay (colorOff);
    arrayBlue(dimOrange);       
    pixels.show();
    delay(3000);

    backgroundDisplay (colorOff);
    arrayOrange(dimBlue);       
    pixels.show();
    delay(3000);

    backgroundDisplay (colorOff);
    arrayYellow(dimRed);        
    pixels.show();
    delay(3000);

    backgroundDisplay (colorOff);
    arrayPurple(dimYellow);     
    pixels.show();
    delay(3000);
*/  
} // End of main loop()

void backgroundDisplay(color rgb) {  // Set all background one color.
    for(int i=0; i<NUMPIXELS; i++){
      pixels.setPixelColor(i, pixels.Color(rgb.red, rgb.green, rgb.blue));
    } 
}

void backgroundDisplay1(color rgb) {  // Set first third background color
    for(int i=0; i<88; i++){
      pixels.setPixelColor(i, pixels.Color(rgb.red, rgb.green, rgb.blue));
    } 
}

void backgroundDisplay2(color rgb) { // Set second third a background color
    for(int i=88; i<168; i++){
      pixels.setPixelColor(i, pixels.Color(rgb.red, rgb.green, rgb.blue));
    } 
}

void backgroundDisplay3(color rgb) { // Set last third a background color
    for(int i=168; i<256; i++){
      pixels.setPixelColor(i, pixels.Color(rgb.red, rgb.green, rgb.blue));
    } 
}

/*************************************************************************
 * addLetter
 *  This is the main function to spell words with some code-reuse.
 *  It takes in a full matrix-sized array, size of the letter to add,
 *  location in the big array to start and the number of columns to shift
 *  the letter in the big array.
 *  
 *************************************************************************/
int addLetter (byte arrayLetter[255], int sizeLetter, int indexMatrix, int columnShift) {
  for(int i = 0; i < sizeLetter; i++){                       
    arrayMatrix[indexMatrix] = arrayLetter[i] + columnShift*8;
    indexMatrix++;
  }
  return indexMatrix;
}

/*
 * Distractions happened.
 * The matrix is about the right size to fit a few dice and we didn't
 * have the right ones for our game of Catan.  This function draws
 * the three dice based on inputs.
 * 
 */
void dice(byte die1, byte die2, color rgb) {
  int j = 0;
  int k = 0;
  if (die1 == 1) { // Select the first die to draw
    j = addLetter(array_dieOne, sizeof(array_dieOne), j,  2);
  }
  else if (die1 == 2) {
    j = addLetter(array_dieTwo, sizeof(array_dieTwo), j,  2);
  }
  else if (die1 == 3) {
    j = addLetter(array_dieThree, sizeof(array_dieThree), j,  2);
  }
  else if (die1 == 4) {
    j = addLetter(array_dieFour, sizeof(array_dieFour), j,  2);
  }
  else if (die1 == 5) {
    j = addLetter(array_dieFive, sizeof(array_dieFive), j, 2);
  }
  else if (die1 == 6) {
    j = addLetter(array_dieSix, sizeof(array_dieSix), j,  2);
  }
  else {
    j = addLetter(array_e,    sizeof(array_e),    j, 2);
  }

  for(k; k < j; k++){ // place the pixels for the first die
    pixels.setPixelColor(arrayMatrix[k], pixels. Color(9,   6,   0)); 
  }
  
  if (die2 == 1) {  // Select the second die to draw
    j = addLetter(array_dieOne, sizeof(array_dieOne), j,  12);
  }
  else if (die2 == 2) {
    j = addLetter(array_dieTwo, sizeof(array_dieTwo), j,  12);
  }
  else if (die2 == 3) {
    j = addLetter(array_dieThree, sizeof(array_dieThree), j,  12);
  }
  else if (die2 == 4) {
    j = addLetter(array_dieFour, sizeof(array_dieFour), j,  12);
  }
  else if (die2 == 5) {
    j = addLetter(array_dieFive, sizeof(array_dieFive), j, 12);
  }
  else if (die2 == 6) {
    j = addLetter(array_dieSix, sizeof(array_dieSix), j,  12);
  }
  else {
    j = addLetter(array_e,    sizeof(array_e),    j, 12);
  }

  for(k; k < j; k++){ // place the pixels for the second die
    pixels.setPixelColor(arrayMatrix[k], pixels. Color(6,   0,   0)); 
  }

  // The third die always has the same shape but has different color
  // depending on the roll.
  j = addLetter(array_dieEvent, sizeof(array_dieEvent), j, 22);
  for(k; k < j; k++){ // place the pixels for the third die
    pixels.setPixelColor(arrayMatrix[k], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
  
/*
//  for(int k = 0; k < j; k++){
//    pixels.setPixelColor(arrayMatrix[k], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
//  }
*/}

void arrayRaj(color rgb) {  // Spells the word Raj
  int j = 0;
  j = addLetter(array_JustusR,       sizeof(array_JustusR),      j,  0);
  j = addLetter(array_JustusA,       sizeof(array_JustusA),      j,  0);
  j = addLetter(array_JustusJ,       sizeof(array_JustusJ),      j,  0);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayRed(color rgb) {  // Spells the word Red
  int j = 0;
  j = addLetter(array_ROdd,    sizeof(array_ROdd),   j,   8);
  j = addLetter(array_e,       sizeof(array_e),      j,  14);
  j = addLetter(array_d,       sizeof(array_d),      j,  18);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayPink(color rgb) {  // Spells the word Pink
  int j = 0;         
  j = addLetter(array_P,       sizeof(array_P),      j,    6);
  j = addLetter(array_i,       sizeof(array_i),      j,   12);
  j = addLetter(array_n,       sizeof(array_n),      j,   16);
  j = addLetter(array_kOdd,    sizeof(array_kOdd),   j,   20);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayPizza(color rgb) { // Spells the word Pizza
  int j = 0;         
  j = addLetter(array_POdd,    sizeof(array_POdd),    j,   4);
  j = addLetter(array_iOdd,    sizeof(array_iOdd),    j,  10);
  j = addLetter(array_zOdd,    sizeof(array_zOdd),    j,  14);
  j = addLetter(array_zOdd,    sizeof(array_zOdd),    j,  18);
  j = addLetter(array_aOdd,    sizeof(array_aOdd),    j,  22);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayJustus(color rgb) { // Spells the word Justus
  int j = 0;         
  j = addLetter(array_J,       sizeof(array_J),       j,   2);
  j = addLetter(array_u,       sizeof(array_u),       j,   8);
  j = addLetter(array_sOdd,    sizeof(array_sOdd),    j,  12);
  j = addLetter(array_t,       sizeof(array_t),       j,  18);
  j = addLetter(array_u,       sizeof(array_u),       j,  22);
  j = addLetter(array_sOdd,    sizeof(array_sOdd),    j,  26);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayBlue(color rgb) { // Spells the word Blue
  int j = 0;
  j = addLetter(array_BOdd, sizeof(array_BOdd),       j,   6);
  j = addLetter(array_l,    sizeof(array_l),          j,  12);
  j = addLetter(array_u,    sizeof(array_u),          j,  16);
  j = addLetter(array_eOdd, sizeof(array_eOdd),       j,  20);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayBlack(color rgb) { // Spells the word Black
  int j = 0;         // temp index for arrayMatrix
  j = addLetter(array_B,    sizeof(array_B),          j,    4);
  j = addLetter(array_lOdd, sizeof(array_lOdd),       j,    8);
  j = addLetter(array_aOdd, sizeof(array_aOdd),       j,   12);
  j = addLetter(array_c,    sizeof(array_c),          j,   18);
  j = addLetter(array_kOdd, sizeof(array_kOdd),       j,   22);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayGreen(color rgb) { // Spells the word Green
  int j = 0;         // temp index for arrayMatrix
  j = addLetter(array_G,    sizeof(array_G),          j,    4);
  j = addLetter(array_r,    sizeof(array_r),          j,   10);
  j = addLetter(array_eOdd, sizeof(array_eOdd),       j,   14);
  j = addLetter(array_eOdd, sizeof(array_eOdd),       j,   18);
  j = addLetter(array_nOdd, sizeof(array_nOdd),       j,   22);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayWhite(color rgb) { // Spells the word White
  int j = 0;         // temp index for arrayMatrix
  j = addLetter(array_WOdd,    sizeof(array_WOdd),    j,   4);
  j = addLetter(array_hOdd,    sizeof(array_hOdd),    j,   10);
  j = addLetter(array_i,       sizeof(array_i),       j,   16);
  j = addLetter(array_t,       sizeof(array_t),       j,   20);
  j = addLetter(array_e,       sizeof(array_e),       j,   24);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayOrange(color rgb) { // Spells the word Orange
  int j = 0;         // temp index for arrayMatrix
  j = addLetter(array_O,       sizeof(array_O),       j,   2);
  j = addLetter(array_r,       sizeof(array_r),       j,   8);
  j = addLetter(array_aOdd,    sizeof(array_aOdd),    j,  12);
  j = addLetter(array_n,       sizeof(array_n),       j,  18);
  j = addLetter(array_gOdd,    sizeof(array_gOdd),    j,  22);
  j = addLetter(array_e,       sizeof(array_e),       j,  28);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayYellow(color rgb) { // Spells the word Yellow
  int j = 0;         // temp index for arrayMatrix
  j = addLetter(array_Y,       sizeof(array_Y),       j,   2);
  j = addLetter(array_e,       sizeof(array_e),       j,   8);
  j = addLetter(array_l,       sizeof(array_l),       j,  12);
  j = addLetter(array_l,       sizeof(array_l),       j,  16);
  j = addLetter(array_o,       sizeof(array_o),       j,  20);
  j = addLetter(array_wOdd,    sizeof(array_wOdd),    j,  24);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

void arrayPurple(color rgb) { // Spells the word Purple
  int j = 0;         // temp index for arrayMatrix
  j = addLetter(array_P,       sizeof(array_P),       j,   2);
  j = addLetter(array_u,       sizeof(array_u),       j,   8);
  j = addLetter(array_rOdd,    sizeof(array_rOdd),    j,  12);
  j = addLetter(array_p,       sizeof(array_p),       j,  18);
  j = addLetter(array_lOdd,    sizeof(array_lOdd),    j,  22);
  j = addLetter(array_eOdd,    sizeof(array_eOdd),    j,  26);
  for(int i = 0; i < j; i++){
    pixels.setPixelColor(arrayMatrix[i], pixels. Color(rgb.red, rgb.green, rgb.blue)); 
  }
}

