package org.usfirst.frc.team2607.robot;


public class Constants {

	
	public static int talonFrontLeft = 1;
	public static int talonFrontRight = 2;
	public static int talonBackRight = 3;
	public static int talonBackLeft = 4;
	public static int[] talonCANAddresses = {talonFrontLeft, talonFrontRight, talonBackLeft, talonBackRight};
	
	public static int talonElevator1 = 5;
	public static int talonElevator2 = 6;
	
	public static int solenoidChannel = 0;
	public static int gyroChannel = 0;
	public static int bottomSwitchPort = 10;
	public static int topSwitchPort = 11;
	
	public static int encoderFrontRightChannelA = 2;
	public static int encoderFrontRightChannelB = 3;
	public static boolean encoderFrontRightReversed = true;
	
	public static int encoderFrontLeftChannelA = 0;
	public static int encoderFrontLeftChannelB = 1;
	public static boolean encoderFrontLeftReversed = false;
	
	public static int encoderBackRightChannelA = 6;
	public static int encoderBackRightChannelB = 7;
	public static boolean encoderBackRightReversed = false;
	
	public static int encoderBackLeftChannelA = 4;
	public static int encoderBackLeftChannelB = 5;
	public static boolean encoderBackLeftReversed = false;

	public static int encoderElevatorChannelA = 8;
	public static int encoderElevatorChannelB = 9;
	public static boolean encoderElevatorReversed = false;
	
	public static int[][] encoders = {
		{encoderFrontLeftChannelA, encoderFrontLeftChannelB},
		{encoderFrontRightChannelA, encoderFrontRightChannelB},
		{encoderBackLeftChannelA, encoderBackLeftChannelB},
		{encoderBackRightChannelA, encoderBackRightChannelB}
		};
	
	
	  final static double[][] talonHighGearPIDGains = {
		  
              {.000026, .000015, 0.0},  // leftFrontPID Gains
              {.000026, .000015, 0.0},  // rightFrontPID Gains
              {.000026, .000015, 0.0},  // leftRearPID Gains
              {.000026, .000015, 0.0}   // rightRearPID Gains
};

	  final static double[][] talonLowGearPIDGains = {
              {.000058, .000036, 0.0},  // leftFrontPID Gains
              {.000058, .000036, 0.0},  // rightFrontPID Gains
              {.000058, .000036, 0.0},  // leftRearPID Gains 
              {.000058, .000036, 0.0}  // rightRearPID Gains                                      
};
	  
	  final static double  talonHighGearMaxSpeed = 18100;
		  	  

	  final static double talonLowGearMaxSpeed =  8100;
	  
}
	
