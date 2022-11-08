#ifndef __BSP_KALMANFILTER_H__
#define __BSP_KALMANFILTER_H__

#include "main.h"

#define RAD_TO_DEG (360/3.1415926/2)
#define DEG_TO_RAD (2*3.1415926/360)

typedef struct
{
    /* We will set the variables like so, these can also be tuned by the user */
    float Q_angle;//0.001
    float Q_bias;//0.003
    float R_measure;//0.03

    float angle;  // Reset the angle 0.0
    float bias;  // Reset bias 0.0

    float P[2][2];//0.0
	
		float rate;
	
} KalmanFilter;

static KalmanFilter KalmanFilter_X;
static KalmanFilter KalmanFilter_Y;

void Kalman_Init(void);
float getAngle(float newAngle, float newRate, float dt, KalmanFilter *KalmanFilter_O);

void setAngle(float angle,KalmanFilter *KalmanFilter_O); 
float getRate(KalmanFilter *KalmanFilter_O);

void setQangle(float Q_angle,KalmanFilter *KalmanFilter_O);
void setQbias(float Q_bias,KalmanFilter *KalmanFilter_O);
void setRmeasure(float R_measure,KalmanFilter *KalmanFilter_O);

float getQangle(KalmanFilter *KalmanFilter_O);
float getQbias(KalmanFilter *KalmanFilter_O);
float getRmeasure(KalmanFilter *KalmanFilter_O);

#endif