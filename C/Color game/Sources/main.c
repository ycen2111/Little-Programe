/* ###################################################################
**     Filename    : main.c
**     Project     : assignment 1
**     Processor   : MKL25Z128VLK4
**     Version     : Driver 01.01
**     Compiler    : GNU C Compiler
**     Date/Time   : 2021-02-09, 15:25, # CodeGen: 0
**     Abstract    :
**         Main module.
**         This module contains user's application code.
**     Settings    :
**     Contents    :
**         No public methods
**
** ###################################################################*/
/*!
** @file main.c
** @version 01.01
** @brief
**         Main module.
**         This module contains user's application code.
*/         
/*!
**  @addtogroup main_module main module documentation
**  @{
*/         
/* MODULE main */


/* Including needed modules to compile this module/procedure */
#include "Cpu.h"
#include "Events.h"
#include "RED_LED.h"
#include "TU1.h"
#include "GREEN_LED.h"
#include "BLUE_LED.h"
#include "TU2.h"
#include "TSS1.h"
/* Including shared modules, which are used for whole project */
#include "PE_Types.h"
#include "PE_Error.h"
#include "PE_Const.h"
#include "IO_Map.h"
/* User includes (#include below this line is not maintained by Processor Expert) */

/*
 * ========================================================================================
 * 	Event:waitms
 * 	Description:A sort of delay function, in unit of ms. The program will wait for seconds until reaching its
 * 				set time value.
 * 	Input:wait_ms(int)
 * 	Output: none
 * =========================================================================================
 */
void waitms(int wait_ms) {
	unsigned int i,j;
	for(i=0;i<wait_ms;i++){
		for(j=0;j<785;j++) {
			__asm("nop");
		}
	}
}

/*
 * ========================================================================================
 * 	Event:LED_initialise
 * 	Description:Turn off all three PMW LED terminals. Set them into initialise case.
 * 	Input:none
 * 	Output: none
 * =========================================================================================
 */
void LED_initialise(){
	RED_LED_SetRatio16(RED_LED_DeviceData,0x0);
	BLUE_LED_SetRatio16(BLUE_LED_DeviceData,0x0);
    GREEN_LED_SetRatio16(GREEN_LED_DeviceData,0x0);
}

/*
 * ========================================================================================
 * 	Event:output_board_signal
 * 	Description:Set LED's output color. Every number in position is pointed to an unique color.
 * 	Input:position(int)
 * 	Output: none
 * =========================================================================================
 */
void output_board_signal(int Position){
		  if (Position ==0) {
			         RED_LED_SetRatio16(RED_LED_DeviceData,0x09FF);
			     	 BLUE_LED_SetRatio16(BLUE_LED_DeviceData,0x0);//red
			     	 GREEN_LED_SetRatio16(GREEN_LED_DeviceData,0x0);
		   	   	    }
		  if (Position ==1) {
			         GREEN_LED_SetRatio16(GREEN_LED_DeviceData,0x09FF);
			      	 BLUE_LED_SetRatio16(BLUE_LED_DeviceData,0x0);//green
			      	 RED_LED_SetRatio16(RED_LED_DeviceData,0x0);
		   	   	    }
		  if (Position ==2) {
			         RED_LED_SetRatio16(RED_LED_DeviceData,0x0);
			    	 GREEN_LED_SetRatio16(GREEN_LED_DeviceData,0x0);//blue
			    	 BLUE_LED_SetRatio16(BLUE_LED_DeviceData,0x09FF);
		  	   		}
}

int ispressed=0;//double clicking check



/*lint -save  -e970 Disable MISRA rule (6.3) checking. */
int main(void)
/*lint -restore Enable MISRA rule (6.3) checking. */
{
  /* Write your local variable definition here */
//--------------------------------variable definition---------------------------------
RESTART://where to start the new round
TSS1_Configure();
int random_seed;//seed of srand()
int random_number;//random number from rand()
int i,temp_input;//counting variable
int repeat_times;//repeat time count
int list[100];//past data memory
int pass;//bool, check whether user's input is correct

  /*** Processor Expert internal initialization. DON'T REMOVE THIS CODE!!! ***/
  PE_low_level_init();
  /*** End of Processor Expert internal initialization.                    ***/

  /* Write your code here */
  /* For example: for(;;) { } */
  //------------------------------input random seed----------------------------------
  RED_LED_SetRatio16(RED_LED_DeviceData,0x09FF);
  BLUE_LED_SetRatio16(BLUE_LED_DeviceData,0x09FF);//start color(Bright white)
  GREEN_LED_SetRatio16(GREEN_LED_DeviceData,0x09FF);
  pass=1;//initialise user's input is true

  //initialise array
  for (i=0;i<100;i++){
      		list[i]=0;
      	}

  //kick to start(white) and user's first TSS input value will be the random seed
//  for(;;){
  do{
	  TSS_Task();
	  random_seed=TSS1_cKey0.Position;//get seed
  } while (TSS1_cKey0.Position==0);//jump out of loop if seed!=0
  TSS1_Configure();
//  }
  srand(random_seed);//set random seed
  LED_initialise();//cut off LED
  waitms(1500);

  //--------------------------------start repeating----------------------------------
    for(repeat_times=1;repeat_times<100;repeat_times++){//core loop

    	//set random array and output it
    	for (i=0;i<repeat_times;i++){
    		if (i==repeat_times-1){
    			random_number=rand()%3;//choose a random number from 0-2
    			list[i]=random_number;//write into array, add new number once a time
    			list[i+1]=404;//mark the end position, as a sign of 404
    		}
    	    output_board_signal(list[i]);//output its LED color for user to memory
    	    waitms(1500);
    	    LED_initialise();
    	    waitms(500);
    	}

    	//sign(yellow) of finish the array, telling user to input sth.
        BLUE_LED_SetRatio16(BLUE_LED_DeviceData,0x0);//waiting color
        RED_LED_SetRatio16(RED_LED_DeviceData,0x09FF);
        GREEN_LED_SetRatio16(GREEN_LED_DeviceData,0x09FF);
    	waitms(1000);
    	LED_initialise();

    	//user's input, and check it one by one
    	for (i=0;list[i]!=404;i++){//404 is the sign of array's end
    		int color_id;
    		TSS1_Configure();
    		//do{
    			ispressed = 0;
    			while(ispressed <2){//double check avoiding
    				TSS_Task();
    				}
    			temp_input=TSS1_cKey0.Position;//get user's input
    		//} while (temp_input==0);//jump out of loop if input !=0

    		if (temp_input<10||temp_input==31){//turn position into 0-2
    			color_id=0;//red
    		}
    		else if (temp_input>=10&&temp_input<=54){
    			color_id=1;//green
    		}
    		else if (temp_input>54){
    			color_id=2;//blue
    		}

    		output_board_signal(color_id);//output signal LED
    		waitms(500);
    		LED_initialise();
    		waitms(100);

    		//check if inputs is correct
    		if (color_id!=list[i]){
    			pass=0;//mark the wrong sign
    		}
    	}
    	//final check
    	if (pass==1){//correct enter
    		waitms(500);
    		RED_LED_SetRatio16(RED_LED_DeviceData,0x0);//correct color
    		BLUE_LED_SetRatio16(BLUE_LED_DeviceData,0x09FF);
    		GREEN_LED_SetRatio16(GREEN_LED_DeviceData,0x09FF);
    		waitms(750);
    		LED_initialise();
    		waitms(3000);
    	}
    	else{//wrong enter
    		waitms(500);
    		GREEN_LED_SetRatio16(GREEN_LED_DeviceData,0x0);//wrong color
    		BLUE_LED_SetRatio16(BLUE_LED_DeviceData,0x09FF);
    		RED_LED_SetRatio16(RED_LED_DeviceData,0x09FF);
    		waitms(2500);
    		LED_initialise();
    		waitms(500);
    		for (i=0;i<2;i++){
				output_board_signal(0);//some interesting consist colors
				waitms(500);
				output_board_signal(1);
				waitms(500);
				output_board_signal(2);
				waitms(500);
				output_board_signal(1);
				waitms(500);
    		}
    		LED_initialise();
    		waitms(1500);
    		goto RESTART;//fail the game, and restart the game
    	}
    }

    //----------------------finish the game after entering 100 random colors successfully-------------------
    //---------------------------------nobody will pass it, right?----------------------------
    for (i=0;i<10;i++){
    				output_board_signal(0);//some interesting colors
    				waitms(500);
    				output_board_signal(1);
    				waitms(500);
    				output_board_signal(2);
    				waitms(500);
    				output_board_signal(1);
    				waitms(500);
        		}
    goto RESTART;

  /*** Don't write any code pass this line, or it will be deleted during code generation. ***/
  /*** RTOS startup code. Macro PEX_RTOS_START is defined by the RTOS component. DON'T MODIFY THIS CODE!!! ***/
  #ifdef PEX_RTOS_START
    PEX_RTOS_START();                  /* Startup of the selected RTOS. Macro is defined by the RTOS component. */
  #endif
  /*** End of RTOS startup code.  ***/
  /*** Processor Expert end of main routine. DON'T MODIFY THIS CODE!!! ***/
  for(;;){}
  /*** Processor Expert end of main routine. DON'T WRITE CODE BELOW!!! ***/
} /*** End of main routine. DO NOT MODIFY THIS TEXT!!! ***/

/* END main */
/*!
** @}
*/
/*
** ###################################################################
**
**     This file was created by Processor Expert 10.5 [05.21]
**     for the Freescale Kinetis series of microcontrollers.
**
** ###################################################################
*/
