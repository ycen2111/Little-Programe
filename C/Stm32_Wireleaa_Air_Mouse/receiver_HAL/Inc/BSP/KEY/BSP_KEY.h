#ifndef __BSP_KEY_H__
#define __BSP_KEY_H__

#include "main.h"

#define KEY0        HAL_GPIO_ReadPin(GPIOC,GPIO_PIN_5)  //KEY0°´¼üPC5
#define KEY1        HAL_GPIO_ReadPin(GPIOA,GPIO_PIN_15) //KEY1°´¼üPA15
#define WK_UP       HAL_GPIO_ReadPin(GPIOA,GPIO_PIN_0)  //WKUP°´¼üPA0

#define KEY0_PRES 	1
#define KEY1_PRES		2
#define WKUP_PRES   3

void KEY_Init(void);
uint8_t KEY_Scan(uint8_t mode);

#endif
