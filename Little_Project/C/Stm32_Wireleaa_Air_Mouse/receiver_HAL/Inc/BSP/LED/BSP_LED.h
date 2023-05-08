#ifndef __BSP_LED_H__
#define __BSP_LED_H__

#include "main.h"

#define LED0 0
#define LED1 1

void LED_Init(void);
void LED_switch (uint8_t LEDx);

#endif
