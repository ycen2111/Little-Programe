/* mbed Microcontroller Library
 * Copyright (c) 2019 ARM Limited
 * SPDX-License-Identifier: Apache-2.0
 */

#include "Ticker.h"
#include "mbed.h"
#include "SLCD.h"
#include <string>

// Definitons in milliseconds
#define SLOE_PERIOD     50ms
#define FAST_PERIOD     5ms
#define ONE_SECOND      1000ms
#define MAX_DIGITS 20


//initialise the object
Ticker column_check;
Ticker warning;
Ticker changing;
SLCD my_display;



//---------------Initialise the digital pin as output and input----------------
DigitalOut led_g(LED1);//green led
DigitalOut led_r(LED2);//red led
DigitalOut d0(D0);//connect D (on the keyboard, same below)
DigitalOut d1(D1);//connect E
DigitalOut d2(D2);//connect F

DigitalIn d3(D3);//connect G
DigitalIn d4(D4);//connect H
DigitalIn d5(D5);//connect J
DigitalIn d6(D6);//connect K



//--------------------global variables--------------------------
//----------------(not the global variants)-----------------------
bool released=true;
int c1,c2,c3;
int password_digits=0;
int input_memory;
int input_word[MAX_DIGITS];
int last_four_inputs[4]={10,10,10,10};

//---------------------function principle----------------------------
bool initialization_check();//finding if user have entered the password before
void enter_initialize_password(int [MAX_DIGITS],int [MAX_DIGITS]);//enter the low level and high level password at first time

//read module
int read_and_diaplay_input();//read when button is pressed and return digit
void column_switch(void);//Rotating power supply for c1,c2,c3
int keypad(int,int,int,int,int,int,int);//get exactly entered number and pass it to read_input function
void button_release_checking(int, int, int, int, int);//mark whether used have released before
void display_rolling_process(int);//output as a rolling display

//caption module
void caption_rolling(string);//rolling some words on display

//clear module
void reset_inputs();//reset input array, led, last_four_inputs and SLCD
void clean_screen();//reset last_four_inputs and SLCD

//password analysis module
void input_password(int,int [MAX_DIGITS]);//input all digits into array
int check_password(int [MAX_DIGITS],int [MAX_DIGITS]);//find whether user have inputed correct loe level or high level password
void change_password(int [MAX_DIGITS],int [MAX_DIGITS]);//change these two password

//LED and signal module
void red_led(int);
void green_led(int);
void ticker_red_blinking();
void ticker_green_blinking();
void open_door();


//-------------------main function------------
int main()
{
    int input;
    int low_level[MAX_DIGITS], high_level[MAX_DIGITS];
    int entered_results;//recording user's password results
    int discorrect_times=0;//warning after 5 times incorredt enter

    if(!initialization_check()) {//ture if there is no password entered and set earlier
        enter_initialize_password(low_level,high_level);//set low pass and high pass password
    }
    caption_rolling("start");

    while (true)
    {
        input_password(password_digits,input_word);
        entered_results=check_password(low_level,high_level);//0=wrong password, 1=low level, 2=high level

        if (entered_results==0){//wrong password
            red_led(6);
            discorrect_times++;
            if (discorrect_times>4) warning.attach(ticker_red_blinking,ONE_SECOND);//warning after 5 times incorredt entering
            continue;
        }
        else if (entered_results==1){//low level password
            warning.detach();
            open_door();
            continue;
        }
        else if (entered_results==2){//high level password
            warning.detach();
            clean_screen();
            change_password(low_level,high_level);
        }
    }
}

//----------------------initialization module--------------------------
//finding if user have entered the password before
bool initialization_check()
{
    int tens,ones;
    reset_inputs();

    if (password_digits) return true;

    caption_rolling("set password digits");
    do {
    tens=read_and_diaplay_input();
    ones=read_and_diaplay_input();
    } while(ones==11||tens==11);

    password_digits=tens*10+ones;
    clean_screen();
    return false;
}

//enter the low level and high level password at first time
void enter_initialize_password(int low_level[MAX_DIGITS],int high_level[MAX_DIGITS])
{
    caption_rolling("low");
    input_password(password_digits,low_level);
    clean_screen();

    caption_rolling("high");
    input_password(password_digits,high_level);
    clean_screen();
    caption_rolling("congratulation");
}

//----------------------read and display module------------------------------
//----------------------------------------------------------------
//read when button is pressed and return digit
int read_and_diaplay_input()
{
    int r1,r2,r3,r4;
    int input;

    column_check.attach(column_switch,FAST_PERIOD);//runs every 5ms 

    RESTART:
    do{
    d0.write(c1);//write c1 into d0 and output it to the keyboard
    d1.write(c2);
    d2.write(c3);
    r1=d3.read();//read values inside the d3 returnning from the keyboard
    r2=d4.read();
    r3=d5.read();
    r4=d6.read();

    input=keypad(c1, c2, c3, r1, r2, r3, r4);
    button_release_checking(input_memory, c1, c2, c3, r1*r2*r3*r4);

    ThisThread::sleep_for(SLOE_PERIOD);//delay function
    } while(input==404);

    if (released){
        display_rolling_process(input);
        released=false;
        input_memory=input;
        //if (input==11||input==12) goto RESTART;

        return input;
    }
    else goto RESTART;
}

//Rotating power supply for c1,c2,c3
void column_switch()
{   
    if (c3==0) {c1=0;c2=1;c3=1;}//only c1 has the current
    else if (c1==0) {c1=1;c2=0;c3=1;}//same, but c2
    else if (c2==0) {c1=1;c2=1;c3=0;}//c3
    else {c3=0;}//initialise this function
}

//get exactly entered number and pass it to read_input function
int keypad(int c1,int c2,int c3,int r1,int r2,int r3,int r4)
{
        int i;

    //recognize single signal input by the crossover point
        if (c3==0 && r4==0) i=1;//1
        else if (c2==0 && r4==0) i=2;//2
        else if (c1==0 && r4==0) i=3;//3
        else if (c3==0 && r3==0) i=4;//4
        else if (c2==0 && r3==0) i=5;//5
        else if (c1==0 && r3==0) i=6;//6
        else if (c3==0 && r2==0) i=7;//7
        else if (c2==0 && r2==0) i=8;//8
        else if (c1==0 && r2==0) i=9;//9
        else if (c3==0 && r1==0) i=11;//"*", meaning clear inputs
        else if (c2==0 && r1==0) i=0;//0
        else if (c1==0 && r1==0) i=12;//#, meaning change inputs
        else i=404;

    return i;
}

//mark whether used have released before
void button_release_checking(int input_memory, int c1, int c2, int c3, int row_results)
{
    if (!released){
        if ((input_memory==1||input_memory==4||input_memory==7||input_memory==11) && c3==0) {if (row_results==1) released=true;}
        else if ((input_memory==2||input_memory==5||input_memory==8||input_memory==0) && c2==0) {if (row_results==1) released=true;}
        else if ((input_memory==3||input_memory==6||input_memory==9||input_memory==12) && c1==0) {if (row_results==1) released=true;}
    }
}

//output as a rolling display
void display_rolling_process(int input)
{
    if (input==11) {
        reset_inputs();
        return;
        }
    if (input==12) {
        reset_inputs();
        return;
        }

    for (int i=0;i<4;i++){
        if (last_four_inputs[i]==10){
            my_display.printf("%d", input); // prints i
            last_four_inputs[i]=input;
            return;
        }
    }

    last_four_inputs[0]=last_four_inputs[1];
    last_four_inputs[1]=last_four_inputs[2];
    last_four_inputs[2]=last_four_inputs[3];
    last_four_inputs[3]=input;
    //clean_screen();
    for (int i=0;i<4;i++){
        my_display.printf("%d", last_four_inputs[i]); // prints i
    }
}

//----------------------caption module--------------------
//--------------------------------------------------------
//rolling some words on display
void caption_rolling(string str)
{
    string caption("   ");
    caption.append(str).append("   ");
    int length=caption.length();

    for(int i=0;i<length-3;i++){//rolling congratulations
        my_display.printf("%c",caption[i]);
        my_display.printf("%c",caption[i+1]);
        my_display.printf("%c",caption[i+2]);
        my_display.printf("%c",caption[i+3]);
        ThisThread::sleep_for(300ms);
    }

    clean_screen();
}


//------------------------clear module-------------------
//-------------------------------------------------------
//reset input array, led, last_four_inputs and SLCD
void reset_inputs()
{
    my_display.clear(); // All segments off
    my_display.Home(); // moves the cursor back to the beginning of the display
    for (int i=0;i<password_digits;i++) input_word[i]=10;//init input password memory
    for (int i=0;i<4;i++) last_four_inputs[i]=10;
    led_g=1;
    led_r=1;
}

//reset last_four_inputs and SLCD
void clean_screen()
{
    my_display.clear(); // All segments off
    my_display.Home(); // moves the cursor back to the beginning of the display
    for (int i=0;i<4;i++) last_four_inputs[i]=10;
}


//----------------------password analysis module----------------
//---------------------------------------------------------------------
//input all digits into array
void input_password(int digits,int password[MAX_DIGITS])
{
    for (int i=0;i<digits;i++){
        password[i]=read_and_diaplay_input();
        if (password[i]==11) {i=-1; continue;}
    }
}

//find whether user have inputed correct loe level or high level password
int check_password(int low_level[MAX_DIGITS],int high_level[MAX_DIGITS])
{
    bool low=true,high=true;

    for (int i=0;i<password_digits;i++){
        if (low_level[i]!=input_word[i]) low=false;
    }

    for (int i=0;i<password_digits;i++){
        if (high_level[i]!=input_word[i]) high=false;
    }

    if (low) return 1;
    else if (high) return 2;
    else return 0;
}

//change these two password
void change_password(int low_level[MAX_DIGITS],int high_level[MAX_DIGITS])
{
    int input;

    changing.attach(ticker_green_blinking,ONE_SECOND);
    do {
    input=read_and_diaplay_input();
    clean_screen();
    } while(input!=11&&input!=12);

    if (input==11){
        caption_rolling("low");
        input_password(password_digits,low_level);
    }
    else if (input==12){
        caption_rolling("high");
        input_password(password_digits,high_level);
    }

    changing.detach();
    caption_rolling("congratulations");
}

//----------------LED and signal module---------------
//------------------------------------------------
void red_led(int repeat_times)
{
    for (int i=0;i<repeat_times;i++){
        led_r=!led_r;
        ThisThread::sleep_for(100ms);
    }
    reset_inputs();
}

void green_led(int state)
{
    led_g=state;
}

void ticker_red_blinking()
{
    led_r=!led_r;
}

void ticker_green_blinking()
{
    led_g=!led_g;
}

void open_door()
{
    green_led(0);
    caption_rolling("door is opening");
    green_led(1);
}