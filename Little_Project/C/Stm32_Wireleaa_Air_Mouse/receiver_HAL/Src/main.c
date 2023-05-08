/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.c
  * @brief          : Main program body
  ******************************************************************************
  * @attention
  *
  * <h2><center>&copy; Copyright (c) 2021 STMicroelectronics.
  * All rights reserved.</center></h2>
  *
  * This software component is licensed by ST under BSD 3-Clause license,
  * the "License"; You may not use this file except in compliance with the
  * License. You may obtain a copy of the License at:
  *                        opensource.org/licenses/BSD-3-Clause
  *
  ******************************************************************************
  */
/* USER CODE END Header */
/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "i2c.h"
#include "spi.h"
#include "tim.h"
#include "usart.h"
#include "usb_device.h"
#include "gpio.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include "LED\BSP_LED.h"
#include "OLED\oled.h"
//#include "MPU6050\BSP_MPU6050.h"
#include "NRF24L01\BSP_NRF24L01.h"
#include "usbd_hid.h"

#define dt 0.0 //10ms 10/1000
/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */
uint8_t MouseData01[4] = {0,0,0,0};

extern USBD_HandleTypeDef hUsbDeviceFS;
/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */
/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/

/* USER CODE BEGIN PV */
uint8_t startMessage[]="start communication \r\n";
uint8_t aRxBuffer[10];
int Pitch_Sum=0,Roll_Sum=0;
/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
/* USER CODE BEGIN PFP */

/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */

/* USER CODE END 0 */

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* USER CODE BEGIN 1 */
int pitch,roll;
  /* USER CODE END 1 */

  /* MCU Configuration--------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* USER CODE BEGIN Init */
  /* USER CODE END Init */

  /* Configure the system clock */
  SystemClock_Config();

  /* USER CODE BEGIN SysInit */
	LED_Init();
	OLED_Init();
  /* USER CODE END SysInit */

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_TIM2_Init();
  MX_USART1_UART_Init();
  MX_I2C1_Init();
  MX_USB_DEVICE_Init();
  MX_SPI1_Init();
  /* USER CODE BEGIN 2 */
	NRF24L01_Init();
	
	uint8_t tmp_buf[3];
	
  /* USER CODE END 2 */

  /* Infinite loop */
  /* USER CODE BEGIN WHILE */

	while(NRF24L01_Check())
	{
		printf("not found NRF24L01\r\n"); 
 		HAL_Delay(1000);
	}
	
	printf("NRF24L01 exist\r\n");
	
	NRF24L01_RX_Mode();
  printf("receive mode\r\n");

  while (1)
  {
    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */
		HAL_Delay(50);
		
		if(NRF24L01_RxPacket(tmp_buf)==0)
    {
			//printf("%d,%d,%d\r\n",tmp_buf[0],tmp_buf[1],tmp_buf[2]);
			//printf("%d\r\n",tmp_buf[2]%16);
			
			pitch=tmp_buf[0]%128;//x-axis remove one high bit
			roll=tmp_buf[1]%128;//y-axis
			
			//negative mark
			if (tmp_buf[2]&0x20) pitch=-pitch; 
			if (tmp_buf[2]&0x10) roll=-roll;
			Pitch_Sum+=pitch;
			Roll_Sum+=roll;
			
//			//get pitch origin
//			if (tmp_buf[0]&0x80) 
//			{
//				//printf("pitch");
//				if (abs(Pitch_Sum)<4) {pitch-=Pitch_Sum; Pitch_Sum=0;}
//				else {pitch-=4*Pitch_Sum/abs(Pitch_Sum); Pitch_Sum-=4*Pitch_Sum/abs(Pitch_Sum);}
//			}
//			//get roll origin
//			if (tmp_buf[1]&0x80) 
//			{
//				//printf("roll");
//				if (abs(Roll_Sum)<4) {roll-=Roll_Sum; Roll_Sum=0;}
//				else {roll-=4*Roll_Sum/abs(Roll_Sum); Roll_Sum-=4*Roll_Sum/abs(Roll_Sum);}
//			}
			
			//trasnfer
			if (pitch<0) MouseData01[1]=255+pitch;
			else MouseData01[1]=pitch;
			if (roll<0) MouseData01[2]=255+roll;
			else MouseData01[2]=roll;
			
			if(tmp_buf[2]&0x80) {MouseData01[0]=0x02;}//right key0000 0010
			if(tmp_buf[2]&0x40) {MouseData01[0]=0x01;}//left key0000 0001
			
			if(tmp_buf[2]&0x8) {MouseData01[3]=-(tmp_buf[2]%8);}//rolling up
			else {MouseData01[3]=tmp_buf[2]%8;}//rolling down
			//HAL_Delay(1000);
			
			//printf("%d,%d\r\n",Pitch_Sum,Roll_Sum);
			
			USBD_HID_SendReport(&hUsbDeviceFS,(uint8_t*)&MouseData01,sizeof(MouseData01));
			printf("%d,%d,%d,%d\r\n",MouseData01[0],MouseData01[1],MouseData01[2],MouseData01[3]);
    }
    else
    {
      printf("NRF24L01 failed\r\n");
    }

		//printf("%d\r\n",tmp_buf[2]);
		
		for(int i=0;i<4;i++) 
		{
			MouseData01[i]=0;
		}
		
	}	
  /* USER CODE END 3 */
}

/**
  * @brief System Clock Configuration
  * @retval None
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};
  RCC_PeriphCLKInitTypeDef PeriphClkInit = {0};

  /** Initializes the RCC Oscillators according to the specified parameters
  * in the RCC_OscInitTypeDef structure.
  */
  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSE;
  RCC_OscInitStruct.HSEState = RCC_HSE_ON;
  RCC_OscInitStruct.HSEPredivValue = RCC_HSE_PREDIV_DIV1;
  RCC_OscInitStruct.HSIState = RCC_HSI_ON;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSE;
  RCC_OscInitStruct.PLL.PLLMUL = RCC_PLL_MUL9;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }
  /** Initializes the CPU, AHB and APB buses clocks
  */
  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV2;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_2) != HAL_OK)
  {
    Error_Handler();
  }
  PeriphClkInit.PeriphClockSelection = RCC_PERIPHCLK_USB;
  PeriphClkInit.UsbClockSelection = RCC_USBCLKSOURCE_PLL_DIV1_5;
  if (HAL_RCCEx_PeriphCLKConfig(&PeriphClkInit) != HAL_OK)
  {
    Error_Handler();
  }
}

/* USER CODE BEGIN 4 */

/* USER CODE END 4 */

/**
  * @brief  Period elapsed callback in non blocking mode
  * @note   This function is called  when TIM1 interrupt took place, inside
  * HAL_TIM_IRQHandler(). It makes a direct call to HAL_IncTick() to increment
  * a global variable "uwTick" used as application time base.
  * @param  htim : TIM handle
  * @retval None
  */
void HAL_TIM_PeriodElapsedCallback(TIM_HandleTypeDef *htim)
{
  /* USER CODE BEGIN Callback 0 */

  /* USER CODE END Callback 0 */
  if (htim->Instance == TIM1) {
    HAL_IncTick();
  }
  /* USER CODE BEGIN Callback 1 */

  /* USER CODE END Callback 1 */
	
	if(htim==(&htim2))
    {		
    LED_switch(LED0);
    }
}

/**
  * @brief  This function is executed in case of error occurrence.
  * @retval None
  */
void Error_Handler(void)
{
  /* USER CODE BEGIN Error_Handler_Debug */
  /* User can add his own implementation to report the HAL error return state */
  __disable_irq();
  while (1)
  {
  }
  /* USER CODE END Error_Handler_Debug */
}

#ifdef  USE_FULL_ASSERT
/**
  * @brief  Reports the name of the source file and the source line number
  *         where the assert_param error has occurred.
  * @param  file: pointer to the source file name
  * @param  line: assert_param error line source number
  * @retval None
  */
void assert_failed(uint8_t *file, uint32_t line)
{
  /* USER CODE BEGIN 6 */
  /* User can add his own implementation to report the file name and line number,
     ex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */
}
#endif /* USE_FULL_ASSERT */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
