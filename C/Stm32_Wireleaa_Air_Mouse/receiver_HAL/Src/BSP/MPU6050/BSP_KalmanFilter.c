#include "MPU6050\BSP_MPU6050.h"
#include "MPU6050\BSP_KalmanFilter.h"
#include "stdio.h"
#include "math.h"

/* IMU Data */
static short accX, accY, accZ;
static float gyroX, gyroY, gyroZ;
static int16_t tempRaw;

static double gyroXangle, gyroYangle; // Angle calculate using the gyro only
static double compAngleX, compAngleY; // Calculated angle using a complementary filter
static double kalAngleX, kalAngleY; // Calculated angle using a Kalman filter

static float dt=10.0/1000;//10ms

void Kalman_Init(void)
{
	KalmanFilter_X.Q_angle=0.001;
	KalmanFilter_X.Q_bias=0.003;
	KalmanFilter_X.angle=0.0;
	KalmanFilter_X.bias=0.0;
	
	KalmanFilter_X.P[0][0] = 0.0; // Since we assume that the bias is 0 and we know the starting angle (use setAngle), the error covariance matrix is set like so - see: http://en.wikipedia.org/wiki/Kalman_filter#Example_application.2C_technical
  KalmanFilter_X.P[0][1] = 0.0;
  KalmanFilter_X.P[1][0] = 0.0;
  KalmanFilter_X.P[1][1] = 0.0;
	
	KalmanFilter_Y.Q_angle=0.001;
	KalmanFilter_Y.Q_bias=0.003;
	KalmanFilter_Y.angle=0.0;
	KalmanFilter_Y.bias=0.0;
	
	KalmanFilter_Y.P[0][0] = 0.0; // Since we assume that the bias is 0 and we know the starting angle (use setAngle), the error covariance matrix is set like so - see: http://en.wikipedia.org/wiki/Kalman_filter#Example_application.2C_technical
  KalmanFilter_Y.P[0][1] = 0.0;
  KalmanFilter_Y.P[1][0] = 0.0;
  KalmanFilter_Y.P[1][1] = 0.0;
	
	// get original value
	MPU_Get_Gyroscope(&gyroX,&gyroY,&gyroZ);
	MPU_Get_Accelerometer(&accX,&accY,&accZ);
	
  double roll  = atan2(accY, accZ) * RAD_TO_DEG;
  double pitch = atan(-accX / sqrt(accY * accY + accZ * accZ)) * RAD_TO_DEG;

  setAngle(roll,&KalmanFilter_X); // Set starting angle
  setAngle(pitch,&KalmanFilter_Y);
  gyroXangle = roll;
  gyroYangle = pitch;
  compAngleX = roll;
  compAngleY = pitch;
}

float getAngle(float newAngle, float newRate, float dt, KalmanFilter *KalmanFilter_O) {
    // KasBot V2  -  Kalman filter module - http://www.x-firm.com/?page_id=145
    // Modified by Kristian Lauszus
    // See my blog post for more information: http://blog.tkjelectronics.dk/2012/09/a-practical-approach-to-kalman-filter-and-how-to-implement-it

    // Discrete Kalman filter time update equations - Time Update ("Predict")
    // Update xhat - Project the state ahead
    /* Step 1 */
    KalmanFilter_O->rate = newRate - KalmanFilter_O->bias;
    KalmanFilter_O->angle += dt * KalmanFilter_O->rate;

    // Update estimation error covariance - Project the error covariance ahead
    /* Step 2 */
    KalmanFilter_O->P[0][0] += dt * (dt*KalmanFilter_O->P[1][1] - KalmanFilter_O->P[0][1] - KalmanFilter_O->P[1][0] + KalmanFilter_O->Q_angle);
    KalmanFilter_O->P[0][1] -= dt * KalmanFilter_O->P[1][1];
    KalmanFilter_O->P[1][0] -= dt * KalmanFilter_O->P[1][1];
    KalmanFilter_O->P[1][1] += KalmanFilter_O->Q_bias * dt;

    // Discrete Kalman filter measurement update equations - Measurement Update ("Correct")
    // Calculate Kalman gain - Compute the Kalman gain
    /* Step 4 */
    float S = KalmanFilter_O->P[0][0] + KalmanFilter_O->R_measure; // Estimate error
    /* Step 5 */
    float K[2]; // Kalman gain - This is a 2x1 vector
    K[0] = KalmanFilter_O->P[0][0] / S;
    K[1] = KalmanFilter_O->P[1][0] / S;

    // Calculate angle and bias - Update estimate with measurement zk (newAngle)
    /* Step 3 */
    float y = newAngle - KalmanFilter_O->angle; // Angle difference
    /* Step 6 */
    KalmanFilter_O->angle += K[0] * y;
    KalmanFilter_O->bias += K[1] * y;

    // Calculate estimation error covariance - Update the error covariance
    /* Step 7 */
    float P00_temp = KalmanFilter_O->P[0][0];
    float P01_temp = KalmanFilter_O->P[0][1];

    KalmanFilter_O->P[0][0] -= K[0] * P00_temp;
    KalmanFilter_O->P[0][1] -= K[0] * P01_temp;
    KalmanFilter_O->P[1][0] -= K[1] * P00_temp;
    KalmanFilter_O->P[1][1] -= K[1] * P01_temp;

    return KalmanFilter_O->angle;
};

void setAngle(float angle,KalmanFilter *KalmanFilter_O) { KalmanFilter_O->angle = angle; }; // Used to set angle, this should be set as the starting angle
float getRate(KalmanFilter *KalmanFilter_O) { return KalmanFilter_O->rate; }; // Return the unbiased rate

/* These are used to tune the Kalman filter */
void setQangle(float Q_angle,KalmanFilter *KalmanFilter_O) { KalmanFilter_O->Q_angle = Q_angle; };
void setQbias(float Q_bias,KalmanFilter *KalmanFilter_O) { KalmanFilter_O->Q_bias = Q_bias; };
void setRmeasure(float R_measure,KalmanFilter *KalmanFilter_O) { KalmanFilter_O->R_measure = R_measure; };

float getQangle(KalmanFilter *KalmanFilter_O) { return KalmanFilter_O->Q_angle; };
float getQbias(KalmanFilter *KalmanFilter_O) { return KalmanFilter_O->Q_bias; };
float getRmeasure(KalmanFilter *KalmanFilter_O) { return KalmanFilter_O->R_measure; };

//-------------------------------------------------------------------

void Kalman_get_Gyroscope(float *x,float *y,float *z)
{
	MPU_Get_Gyroscope(&gyroX,&gyroY,&gyroZ);
	MPU_Get_Accelerometer(&accX,&accY,&accZ);
	
	double roll  = atan2(accY, accZ) * RAD_TO_DEG;
  double pitch = atan(-accX / sqrt(accY * accY + accZ * accZ)) * RAD_TO_DEG;
	
	double gyroXrate = gyroX / 16.4; // Convert to deg/s, 32768/2000
  double gyroYrate = gyroY / 16.4; // Convert to deg/s
	
	if ((roll < -90 && kalAngleX > 90) || (roll > 90 && kalAngleX < -90)) {
    setAngle(roll,&KalmanFilter_X);
    compAngleX = roll;
    kalAngleX = roll;
    gyroXangle = roll;
  } else
    kalAngleX = getAngle(roll, gyroXrate, dt,&KalmanFilter_X); // Calculate the angle using a Kalman filter

  if (abs(kalAngleX) > 90)
    gyroYrate = -gyroYrate; // Invert rate, so it fits the restriced accelerometer reading
  kalAngleY = kalmanY.getAngle(pitch, gyroYrate, dt);
}