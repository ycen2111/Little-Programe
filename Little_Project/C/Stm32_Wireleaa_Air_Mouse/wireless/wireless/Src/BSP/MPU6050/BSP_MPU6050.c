#include "MPU6050\BSP_MPU6050.h"
#include "stdio.h"
#include "stdlib.h"
#include "math.h"

#define RESTRICT_PITCH

uint8_t MPU_Init(void)
{ 
  uint8_t res;
  extern I2C_HandleTypeDef hi2c1;
  HAL_I2C_Init(&hi2c1);
  MPU_Write_Byte(MPU_PWR_MGMT1_REG,0X80);	//resetMPU6050
	res=MPU_Read_Byte(MPU_DEVICE_ID_REG);
  MPU_Write_Byte(MPU_PWR_MGMT1_REG,0X00);	//wake MPU6050 up ????
  MPU_Set_Gyro_Fsr(3);					//gyro sensor ,+-2000dps
  MPU_Set_Accel_Fsr(0);					//accelerate sensot,+-2g
  MPU_Set_Rate(50);						//50Hz
  //MPU_Write_Byte(MPU_INT_EN_REG,0X00);	//close all interreptions
  //MPU_Write_Byte(MPU_USER_CTRL_REG,0X00);	//I2C close
  //MPU_Write_Byte(MPU_FIFO_EN_REG,0X00);	//FIFO close
  MPU_Write_Byte(MPU_INTBP_CFG_REG,0X80);	//INT pin with low voltage
  //res=MPU_Read_Byte(MPU_DEVICE_ID_REG);
	printf("\r\nMPU6050:0x%2x\r\n",res);
  if(res==MPU_ADDR)//device ID correct
  {
    MPU_Write_Byte(MPU_PWR_MGMT1_REG,0X01);	
    MPU_Write_Byte(MPU_PWR_MGMT2_REG,0X00);	
    //MPU_Set_Rate(50);						//set 50Hz
  }else 
		return 1;
  return 0;
}
//set gyro range
//fsr:0,+-250dps;1,500dps;2,1000dps;3,2000dps
uint8_t MPU_Set_Gyro_Fsr(uint8_t fsr)
{
	return MPU_Write_Byte(MPU_GYRO_CFG_REG,fsr<<3);
}
//set acceleration range
//fsr:0,+-2g;1,4g;2,8g;3,16g
uint8_t MPU_Set_Accel_Fsr(uint8_t fsr)
{
	return MPU_Write_Byte(MPU_ACCEL_CFG_REG,fsr<<3);
}
//set digital low pass filter
//lpf:low pass frequency(Hz)
uint8_t MPU_Set_LPF(uint16_t lpf)
{
	uint8_t data=0;
	if(lpf>=188)data=1;
	else if(lpf>=98)data=2;
	else if(lpf>=42)data=3;
	else if(lpf>=20)data=4;
	else if(lpf>=10)data=5;
	else data=6; 
	return MPU_Write_Byte(MPU_CFG_REG,data); 
}
//??MPU6050????(assume Fs=1KHz)
//rate:4~1000(Hz)
uint8_t MPU_Set_Rate(uint16_t rate)
{
	uint8_t data;
	if(rate>1000)rate=1000;
	if(rate<4)rate=4;
	data=1000/rate-1;
	data=MPU_Write_Byte(MPU_SAMPLE_RATE_REG,data);	//?????????
 	return MPU_Set_LPF(rate/2);	//????LPF???????
}

//get temperature
//return:value of temerature(100 times)
float MPU_Get_Temperature(void)
{
  unsigned char  buf[2]; 
  short raw;
  float temp;
  
  MPU_Read_Len(MPU_TEMP_OUTH_REG,2,buf); 
  raw=(buf[0]<<8)| buf[1];  
  temp=(36.53+((double)raw)/340)*100;  
//  temp = (long)((35 + (raw / 340)) * 65536L);
  return temp/100.0f;
}
//get gyro original value
//gx,gy,gz:axis values of of x,y,z (with signal)
//???:0,??
//    ??,????
uint8_t MPU_Get_Gyroscope(float *gx,float *gy,float *gz)
{
    uint8_t buf[6],res;  
		//short x,y,z;
	res=MPU_Read_Len(MPU_GYRO_XOUTH_REG,6,buf);
	if(res==0)
	{
		*gx=((uint16_t)buf[0]<<8)|buf[1];  
		*gy=((uint16_t)buf[2]<<8)|buf[3];  
		*gz=((uint16_t)buf[4]<<8)|buf[5];
		
		//*gx=2000*(float)x/32768;
		//*gy=2000*(float)y/32768;
		//*gz=2000*(float)z/32768;
	} 	
    return res;
}
//get acceleration original value
//gx,gy,gz:axis values of of x,y,z (with signal)
//???:0,??
//    ??,????
uint8_t MPU_Get_Accelerometer(short *ax,short *ay,short *az)
{
    uint8_t buf[6],res;  
	res=MPU_Read_Len(MPU_ACCEL_XOUTH_REG,6,buf);
	if(res==0)
	{
		*ax=((uint16_t)buf[0]<<8)|buf[1];  
		*ay=((uint16_t)buf[2]<<8)|buf[3];  
		*az=((uint16_t)buf[4]<<8)|buf[5];
	} 	
    return res;;
}


//IIC multy write
uint8_t MPU_Write_Len(uint8_t reg,uint8_t len,uint8_t *buf)
{
  extern I2C_HandleTypeDef hi2c1;
  HAL_I2C_Mem_Write(&hi2c1, MPU_WRITE, reg, I2C_MEMADD_SIZE_8BIT, buf, len, 0xfff);
  HAL_Delay(100);
  
  return 0;
}
//IIC multy read
//addr:device add
//reg:registion add
//len:read length
//buf:data saving matrix
//???:0,??
//    ??,????
uint8_t MPU_Read_Len(uint8_t reg,uint8_t len,uint8_t *buf)
{ 
  extern I2C_HandleTypeDef hi2c1;
  HAL_I2C_Mem_Read(&hi2c1, MPU_READ, reg, I2C_MEMADD_SIZE_8BIT, buf, len, 0xfff);
  HAL_Delay(10);
  
  return 0;	
}
//IIC byte write
//reg:?????
//data:??
//???:0,??
//    ??,????
uint8_t MPU_Write_Byte(uint8_t reg,uint8_t data) 				 
{ 
  extern I2C_HandleTypeDef hi2c1;
  unsigned char W_Data=0;

  W_Data = data;
  HAL_I2C_Mem_Write(&hi2c1, MPU_WRITE, reg, I2C_MEMADD_SIZE_8BIT, &W_Data, 1, 0xfff);
  HAL_Delay(100);
  
  return 0;
}
//IIC byte read
//reg:????? 
//???:?????
uint8_t MPU_Read_Byte(uint8_t reg)
{
  extern I2C_HandleTypeDef hi2c1;
  unsigned char R_Data=0;
  
  HAL_I2C_Mem_Read(&hi2c1, MPU_READ, reg, I2C_MEMADD_SIZE_8BIT, &R_Data, 1, 0xfff);
  HAL_Delay(100);
  
  return R_Data;		
}

//-----------------------------------------------------------------------------------------------
//-----------------------------------------------------------------------------------------------

// Need set starting angle
KalmanFilterSys_t Get_Kalman_Filter(float roll, float pitch)
{
	KalmanFilterSys_t *pSys = (KalmanFilterSys_t *)calloc(1, sizeof(KalmanFilterSys_t));
	pSys->pKalmanX = (KalmanFilter_t *)calloc(1, sizeof(KalmanFilter_t));
	pSys->pKalmanY = (KalmanFilter_t *)calloc(1, sizeof(KalmanFilter_t));
 
	/* We will set the variables like so, these can also be tuned by the user */
	pSys->pKalmanX->Q_angle = pSys->pKalmanY->Q_angle = 0.001f;
	pSys->pKalmanX->Q_bias = pSys->pKalmanY->Q_bias = 0.003f;
	pSys->pKalmanX->R_measure = pSys->pKalmanY->R_measure = 0.03f;
 
	pSys->pKalmanX->angle = roll; // Reset the angle
	pSys->pKalmanY->angle = pitch; // Reset bias
 
	// Since we assume that the bias is 0 and we know the starting angle (use setAngle),
	// the error covariance matrix is set like so -
	// see: http://en.wikipedia.org/wiki/Kalman_filter#Example_application.2C_technical
	pSys->pKalmanX->P[0][0] = 0.0f;
	pSys->pKalmanX->P[0][1] = 0.0f;
	pSys->pKalmanX->P[1][0] = 0.0f;
	pSys->pKalmanX->P[1][1] = 0.0f;
 
	pSys->pKalmanY->P[0][0] = 0.0f;
	pSys->pKalmanY->P[0][1] = 0.0f;
	pSys->pKalmanY->P[1][0] = 0.0f;
	pSys->pKalmanY->P[1][1] = 0.0f;
 
	pSys->gyroXangle = roll;
	pSys->gyroYangle = pitch;
 
	pSys->compAngleX = roll;
	pSys->compAngleY = pitch;
 
	return *pSys;
}
 
// Source: http://www.freescale.com/files/sensors/doc/app_note/AN3461.pdf
//eq. 25 and eq. 26
// atan2 outputs the value of -p to p (radians) - see http://en.wikipedia.org/wiki/Atan2
// It is then converted from radians to degrees
void Accel_To_Angle(float *p_roll, float *p_pitch, float accX, float accY, float accZ)
{
#ifdef RESTRICT_PITCH // Eq. 25 and 26
	*p_pitch = atan(-accX / sqrt(accY * accY + accZ * accZ)) * RAD_TO_DEG;
	*p_roll = atan(accY / sqrt(accX * accX + accZ * accZ)) * RAD_TO_DEG;
	//*p_roll = atan2(accY, accZ) * RAD_TO_DEG;
#else     // Eq. 28 and 29
	*p_pitch = atan2(-accX, accZ) * RAD_TO_DEG;
	*p_roll = atan(accY / sqrt(accX * accX + accZ * accZ)) * RAD_TO_DEG;
#endif
};
 
// The angle should be in degrees and the rate should be in degrees per second and the delta time in seconds
float Kalman_Filter_GetAngle(KalmanFilter_t *pSys, float newAngle, float newRate, float dt)
{
	// KasBot V2  -  Kalman filter module - http://www.x-firm.com/?page_id=145
	// Modified by Kristian Lauszus
	// See my blog post for more information:  http://blog.tkjelectronics.dk/2012/09/a-practical-approach-to-kalman-filter-and-how-to-implement-it
	// Discrete Kalman filter time update equations - Time Update ("Predict")
	// Update xhat - Project the state ahead
	/* Step 1 */
	pSys->rate = newRate - pSys->bias;
	pSys->angle += dt * pSys->rate;
 
	// Update estimation error covariance - Project the error covariance ahead
	/* Step 2 */
	pSys->P[0][0] += dt * (dt*pSys->P[1][1] - pSys->P[0][1] - pSys->P[1][0] + pSys->Q_angle);
	pSys->P[0][1] -= dt * pSys->P[1][1];
	pSys->P[1][0] -= dt * pSys->P[1][1];
	pSys->P[1][1] += pSys->Q_bias * dt;
 
	// Discrete Kalman filter measurement update equations - Measurement Update ("Correct")
	// Calculate Kalman gain - Compute the Kalman gain
	/* Step 4 */
	float S = pSys->P[0][0] + pSys->R_measure; // Estimate error
 
	/* Step 5 */
	float K[2]; // Kalman gain - This is a 2x1 vector
	K[0] = pSys->P[0][0] / S;
	K[1] = pSys->P[1][0] / S;
 
	// Calculate angle and bias - Update estimate with measurement zk (newAngle)
	/* Step 3 */
	float y = newAngle - pSys->angle; // Angle difference
 
	/* Step 6 */
	pSys->angle += K[0] * y;
	pSys->bias += K[1] * y;
 
	// Calculate estimation error covariance - Update the error covariance
	/* Step 7 */
	float P00_temp = pSys->P[0][0];
	float P01_temp = pSys->P[0][1];
 
	pSys->P[0][0] -= K[0] * P00_temp;
	pSys->P[0][1] -= K[0] * P01_temp;
	pSys->P[1][0] -= K[1] * P00_temp;
	pSys->P[1][1] -= K[1] * P01_temp;
 
	return pSys->angle;
};
 
void Kalman_Fileter_SetAngle(KalmanFilterSys_t *pSys, float roll, float pitch,
	float gyroXrate, float gyroYrate, float dt)
{
#ifdef RESTRICT_PITCH
	// This fixes the transition problem when the accelerometer angle jumps between -180 and 180 degrees
	if ((roll < -90 && pSys->kalAngleX > 90) || (roll > 90 && pSys->kalAngleX < -90)) {
		pSys->pKalmanX->angle = roll;
		pSys->compAngleX = roll;
		pSys->kalAngleX = roll;
		pSys->gyroXangle = roll;
	}
	else
		pSys->kalAngleX = Kalman_Filter_GetAngle(pSys->pKalmanX, roll, gyroXrate, dt); // Calculate the angle using a Kalman filter
 
	if (pSys->kalAngleX> 90||pSys->kalAngleX<-90)
		gyroYrate = -gyroYrate; // Invert rate, so it fits the restriced accelerometer reading
	pSys->kalAngleY = Kalman_Filter_GetAngle(pSys->pKalmanY, pitch, gyroYrate, dt);
#else
	// This fixes the transition problem when the accelerometer angle jumps between -180 and 180 degrees
	if ((pitch < -90 && pSys->kalAngleY > 90) || (pitch > 90 && pSys->kalAngleY < -90)) {
		pSys->pKalmanY->angle = pitch;
		pSys->compAngleY = pitch;
		pSys->kalAngleY = pitch;
		pSys->gyroYangle = pitch;
	}
	else
		pSys->kalAngleY = Kalman_Filter_GetAngle(pSys->pKalmanY, pitch, gyroYrate, dt); // Calculate the angle using a Kalman filter
 
	if (abs(pSys->kalAngleY) > 90)
		gyroXrate = -(gyroXrate); // Invert rate, so it fits the restriced accelerometer reading
	pSys->kalAngleX = Kalman_Filter_GetAngle(pSys->pKalmanX, roll, gyroXrate, dt); // Calculate the angle using a Kalman filter
#endif
 
	pSys->gyroXangle += gyroXrate * dt; // Calculate gyro angle without any filter
	pSys->gyroYangle += gyroYrate * dt;
 
	//pSys->gyroXangle += kalmanX.getRate() * dt; // Calculate gyro angle using the unbiased rate
	//pSys->gyroYangle += kalmanY.getRate() * dt;
 
	// Calculate the angle using a Complimentary filter
	pSys->compAngleX = 0.93 * (pSys->compAngleX + gyroXrate * dt) + 0.07 * roll;
	pSys->compAngleY = 0.93 * (pSys->compAngleY + gyroYrate * dt) + 0.07 * pitch;
 
	// Reset the gyro angle when it has drifted too much
	if (pSys->gyroXangle < -180 || pSys->gyroXangle > 180)
		pSys->gyroXangle = pSys->kalAngleX;
	if (pSys->gyroYangle < -180 || pSys->gyroYangle > 180)
		pSys->gyroYangle = pSys->kalAngleY;
};
 
void Delete_Kalman_Filter(KalmanFilterSys_t **ppSys)
{
	free((*ppSys)->pKalmanX);
	free((*ppSys)->pKalmanX);
	free((*ppSys));
	*ppSys = 0;
};