
package org.usfirst.frc.team2607.robot;

import com.kauailabs.nav6.frc.IMUAdvanced;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	WheelRPMController FrontL;
	WheelRPMController FrontR;
	WheelRPMController BackL;
	WheelRPMController BackR;
	
	CANTalon elevator1, elevator2;
	
	IMUAdvanced navx;
	SerialPort comPort;
	Solenoid solenoid;
	robovikingMecanumDrive robotDrive;
	robovikingStick xboxSupremeController, xboxMinor;
	SmartDashboard smartDash;
	SmoothedEncoder encElevator;
	//DigitalInput topSwitch, bottomSwitch;
	
	
	boolean arms = false;
	double x, y, z;
	double lift = 0.75;
	double lower = -0.5;
	double[] driveValue = new double[3];
	double[] deadZones = {0.15, 0.15, 0.15};
	int currentHeight = 0;
	double tempAngle = 0;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	xboxSupremeController = new robovikingStick(0);
    	xboxMinor = new robovikingStick(1);
    	FrontL = new WheelRPMController("FrontLeft",0,true);
    	FrontR = new WheelRPMController("FrontRight",1,true);
    	BackL = new WheelRPMController("BackLeft", 2,true);
    	BackR = new WheelRPMController("BackRight", 3,true);
    	elevator1 = new CANTalon(Constants.talonElevator1);
    	elevator2 = new CANTalon(Constants.talonElevator2);
    	solenoid = new Solenoid(1, Constants.solenoidChannel);
    	encElevator = new SmoothedEncoder(Constants.encoderElevatorChannelA, 
    									  Constants.encoderElevatorChannelB, 
    									  Constants.encoderElevatorReversed, 
    									  Encoder.EncodingType.k1X);
    	robotDrive = new robovikingMecanumDrive(FrontL, BackL, FrontR, BackR);
    	robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);
    	robotDrive.setInvertedMotor(MotorType.kRearLeft, true);
    	FrontL.enable();
    	BackL.enable();
    	FrontR.enable();
    	BackR.enable();
    	try {
            comPort = new SerialPort(57600, SerialPort.Port.kMXP);
                    
                    // You can add a second parameter to modify the 
                    // update rate (in hz) from 4 to 100.  The default is 100.
                    // If you need to minimize CPU load, you can set it to a
                    // lower value, as shown here, depending upon your needs.
                    
                    // You can also use the IMUAdvanced class for advanced
                    // features.
                    
                    byte update_rate_hz = 50;
                    //imu = new IMU(serial_port,update_rate_hz);
                    navx = new IMUAdvanced(comPort, update_rate_hz);
                	while (navx.isCalibrating()) {
                		Thread.sleep(5);
                	}
            } catch( Exception ex ) {
                    
            }
    	
    	smartDash = new SmartDashboard();
    	/* topSwitch = new DigitalInput(Constants.topSwitchPort);
    	bottomSwitch = new DigitalInput(Constants.bottomSwitchPort);
    	*/
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }
    
    public void testInit(){
    	navx.zeroYaw();
    	testTick = 0;
    }
    
    /*public void teleopInit(){
    	gyro.reset();
    }
    */

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	if (xboxSupremeController.getOneShotButton(7)){
    		navx.zeroYaw();
    	}
    	
    	float angler = navx.getYaw();
    	
    	FrontL.setGearPID(xboxSupremeController.getToggleButton(8));
    	FrontR.setGearPID(xboxSupremeController.getToggleButton(8));
    	BackL.setGearPID(xboxSupremeController.getToggleButton(8));
    	BackR.setGearPID(xboxSupremeController.getToggleButton(8));
    	
    	driveValue[0] = xboxSupremeController.getX() * .65;
    	driveValue[1] = xboxSupremeController.getY() * .65;
    	driveValue[2] = xboxSupremeController.getRawAxis(4)/2;
    	
    	for (int i = 0; i <= 2; i++) {
    		if (Math.abs(driveValue[i]) <= deadZones[i]) {
    			driveValue[i] = 0;
    		}
    		if (driveValue[i] > deadZones[i] && driveValue[i] <= deadZones[i] * 2) {
    			driveValue[i] = (driveValue[i] - .15) * 1.5;
    		}
    		if (driveValue[i] < -deadZones[i] && driveValue[i] >= -2 * deadZones[i]) {
    			driveValue[i] = (driveValue[i] + .15) * 1.5;
    		}
	    	}
    	
    	if (driveValue[2] == 0){
    		driveValue[2] = angler * -.005;
    	} else {
    		navx.zeroYaw();
    	}
    	
	    	if((xboxSupremeController.getRawButton(1) || xboxMinor.getRawButton(1))){ //&& topSwitch.get()){
	    		
	    		elevator1.set(lift);
	    		elevator2.set(lift);
	    		
	    	}else if(xboxSupremeController.getRawButton(4) || xboxMinor.getRawButton(4)){ //&& bottomSwitch.get()){
	    		
	    		elevator1.set(lower);
	    		elevator2.set(lower);
	    	} else {
	    		elevator1.set(0);
	    		elevator2.set(0);
	    	}
	    	
	    if(xboxSupremeController.getOneShotButton(2) || (xboxMinor.getOneShotButton(2))){
	    	arms = !arms;
	    	solenoid.set(arms);
	    	}
	    
	    	
    	
    	robotDrive.mecanumDrive_Cartesian(driveValue[0], driveValue[1], driveValue[2], 0);
    }
     
    /**
     * This function is called periodically during test mode
     */
    private int testTick;
    public void testPeriodic() {
       	
    	double angler = 0.0; //xboxSupremeController.getToggleButton(8) ? navx.getYaw() : 0.0;
    	
    	if (xboxSupremeController.getOneShotButton(7)){
    		navx.zeroYaw();
    	}
    	
    	solenoid.set(false);
    	FrontL.setGearPID(false);
    	FrontR.setGearPID(false);
    	BackL.setGearPID(false);
    	BackR.setGearPID(false);
    	FrontL.enableLogging(xboxSupremeController.getToggleButton(8));
    	FrontR.enableLogging(xboxSupremeController.getToggleButton(8));
    	BackL.enableLogging(xboxSupremeController.getToggleButton(8));
    	BackR.enableLogging(xboxSupremeController.getToggleButton(8));

    	if (xboxSupremeController.getRawButton(4)) {
            y = .4;
            x = 0.0;
            z = angler * -.008;
        } else if (xboxSupremeController.getRawButton(1)) {
            y = -.4;
            x = 0.0;
            z = angler * -.008;
        } else if (xboxSupremeController.getRawButton(3)) {
            y = 0.0;
            x = .4;
            z = angler * -.008;
        } else if (xboxSupremeController.getRawButton(2)) {
            y = 0.0;
            x = -.4;
            z = angler * -.008;
        } else {
            y = 0.0;
            x = 0.0;
            z = 0.0;
        }

    	robotDrive.mecanumDrive_Cartesian(x, y, z, 0);
        //robotDrive.correctedMecanumDrive(x, y, z, 0.0, -.15);
    }
	    
    
    /* public void liftElevator(int height){
    	switch(height){
    	
    	
    	
    	case 0:
    		
    		break;
    	case 1:
    		
    		break;
    	case 2:
    		
    		break;
    	case 3:
    		
    		break;
    	case 4:
    		
    		break;
    	case 5: 
    		
    		break;
    	}
    	
    	currentHeight = height;
    	
    } */
    
   /* public void driveForwardAuton(){
    	
    }
    
    public void strafeLeftAuton(int stage){
    	
    }
    
    
    public void strafeRightAuton(){
    	
    }
    
    public void stackTotesAuton(){
    	
    }
    
    public void pickUpTotesAuton(){
    	
    }
    */
    
    
    /* Auton Steps:
     * 1. pick up tote                 we have one tote
     * 2. strafeRightAuton()
     * 3. driveForwardAuton()
     * 4. strafeLeftAuton(1)
     * 5. stackTotesAuton()
     * 6. pickUpTotesAtuon()           we have two totes
     * 7. strafeRightAuton()
     * 8. driveForwardAuton()
     * 9. strafeLeftAuton(1)
     * 10. stackTotesAuton()
     * 11. pickUpTotesAtuon() 		   we have three totes
     * 12. strafeLeftAuton(2) 
     * 13. drop totes
     */
    
    
}
