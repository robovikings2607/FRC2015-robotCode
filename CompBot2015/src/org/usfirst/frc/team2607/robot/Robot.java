
package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
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
	
	Talon elevator1, elevator2;
	
	Gyro gyro;
	Solenoid solenoid;
	RobotDrive robotDrive;
	robovikingStick xboxSupremeController, xboxMinor;
	SmartDashboard smartDash;
	SmoothedEncoder encElevator;
	DigitalInput topSwitch, bottomSwitch;
	
	
	boolean arms = false;
	double x, y, z;
	double lift = 0.75;
	double lower = -0.75;
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
    	FrontL = new WheelRPMController("FrontLeft",0,false);
    	FrontR = new WheelRPMController("FrontRight",1,false);
    	BackL = new WheelRPMController("BackLeft", 2,false);
    	BackR = new WheelRPMController("BackRight", 3,false);
    	elevator1 = new Talon(Constants.talonElevator1);
    	elevator2 = new Talon(Constants.talonElevator2);
    	solenoid = new Solenoid(1, Constants.solenoidChannel);
    	encElevator = new SmoothedEncoder(Constants.encoderElevatorChannelA, 
    									  Constants.encoderElevatorChannelB, 
    									  Constants.encoderElevatorReversed, 
    									  Encoder.EncodingType.k1X);
    	robotDrive = new RobotDrive(FrontL, BackL, FrontR, BackR);
    	robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);
    	robotDrive.setInvertedMotor(MotorType.kRearLeft, true);
    	gyro = new Gyro(Constants.gyroChannel);
    	gyro.initGyro();
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
    	gyro.reset();
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
    		gyro.reset();
    	}
    	
    	double angler = gyro.getAngle();
    	
    	FrontL.setGearPID(xboxSupremeController.getToggleButton(8));
    	FrontR.setGearPID(xboxSupremeController.getToggleButton(8));
    	BackL.setGearPID(xboxSupremeController.getToggleButton(8));
    	BackR.setGearPID(xboxSupremeController.getToggleButton(8));
    	
    	driveValue[0] = -xboxSupremeController.getX();
    	driveValue[1] = -xboxSupremeController.getY();
    	driveValue[2] = -(xboxSupremeController.getRawAxis(4)/2);
    	
    	for (int i = 0; i <= 2; i++) {
    		if (Math.abs(driveValue[i]) <= deadZones[i]) {
    			driveValue[i] = 0;
    		}
    		if (driveValue[i] > deadZones[i] && driveValue[i] <= deadZones[i] * 2) {
    			driveValue[i] = (driveValue[i] - .10) * 2;
    		}
    		if (driveValue[i] < -deadZones[i] && driveValue[i] >= -2 * deadZones[i]) {
    			driveValue[i] = (driveValue[i] + .10) * 2;
    		}
	    	}
    	
    	if (driveValue[2] == 0){
    		driveValue[2] = angler * .004;
    	} else {
    		gyro.reset();
    	}
    	
	    	if((xboxSupremeController.getRawButton(1) || xboxMinor.getRawButton(1)) && topSwitch.get()){
	    		
	    		elevator1.set(lift);
	    		elevator2.set(lift);
	    		
	    	}else if(xboxSupremeController.getRawButton(4) || xboxMinor.getRawButton(4) && bottomSwitch.get()){
	    		
	    		elevator1.set(lower);
	    		elevator2.set(lower);
	    	} else {
	    		elevator1.set(0);
	    		elevator2.set(0);
	    	}
	    	
	    if(xboxSupremeController.getOneShotButton(2) || (xboxMinor.getOneShotButton(2))){
	    	solenoid.set(!arms);
	    	}
	    
	    	
    	
    	robotDrive.mecanumDrive_Cartesian(driveValue[0], driveValue[1], driveValue[2], 0);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	
    	double angler = gyro.getAngle();
    	
    	if (xboxSupremeController.getOneShotButton(7)){
    		gyro.reset();
    	}
    	
    	FrontL.setGearPID(xboxSupremeController.getToggleButton(8));
    	FrontR.setGearPID(xboxSupremeController.getToggleButton(8));
    	BackL.setGearPID(xboxSupremeController.getToggleButton(8));
    	BackR.setGearPID(xboxSupremeController.getToggleButton(8));
    	
        if (xboxSupremeController.getRawButton(4)) {
            y = .5;
            x = 0.0;
            z = angler * .004;
        } else if (xboxSupremeController.getRawButton(1)) {
            y = -.5;
            x = 0.0;
            z = angler * .004;
        } else if (xboxSupremeController.getRawButton(3)) {
            y = 0.0;
            x = .5;
            z = angler * .004;
        } else if (xboxSupremeController.getRawButton(2)) {
            y = 0.0;
            x = -.5;
            z = 0.0;
        } else {
            y = 0.0;
            x = 0.0;
            z = angler * .004;
        }
        robotDrive.mecanumDrive_Cartesian(x, y, z, 0);   
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
