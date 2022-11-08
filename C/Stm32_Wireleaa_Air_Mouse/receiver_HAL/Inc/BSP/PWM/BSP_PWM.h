#ifndef __BSP_PWM_H__
#define __BSP_PWM_H__

#ifdef __cplusplus
extern "C" {
#endif

/* Includes ------------------------------------------------------------------*/
#include "main.h"

/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

extern TIM_HandleTypeDef htim1;

/* USER CODE BEGIN Private defines */

/* USER CODE END Private defines */

void MX_TIM1_Init(void);

void HAL_TIM_MspPostInit(TIM_HandleTypeDef *htim);

/* USER CODE BEGIN Prototypes */
	
void TIM_SetTIM1Compare(long int compare);

void Compare_Value_Setting(void);

/* USER CODE END Prototypes */

#ifdef __cplusplus
}
#endif

#endif
