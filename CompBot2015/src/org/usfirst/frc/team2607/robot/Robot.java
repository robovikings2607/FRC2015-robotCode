
package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.Compressor;
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
	
	Talon FrontL;
	Talon FrontR;
	Talon BackL;
	Talon BackR;
	
	Talon elevator1, elevator2;
	
	Gyro gyro;
	Solenoid solenoid;
	RobotDrive robotDrive;
	Compressor compressor;
	robovikingStick xboxSupremeController, xboxMinor;
	SmartDashboard smartDash;
	SmoothedEncoder encElevator;
	SmoothedEncoder encFL, encFR, encBL, encBR;
	
	boolean arms = false;
	double x, y, z;
	double lift = 0.75;
	double lower = -0.75;
	double[] driveValue = new double[3];
	double[] deadZones = new double[]{0.15, 0.15, 0.15};
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	xboxSupremeController = new robovikingStick(0);
    	xboxMinor = new robovikingStick(1);
    	compressor = new Compressor(1);
    	FrontL = new Talon(Constants.talonFrontLeft);
    	FrontR = new Talon(Constants.talonFrontRight);
    	BackL = new Talon(Constants.talonBackLeft);
    	BackR = new Talon(Constants.talonBackRight);
    	elevator1 = new Talon(Constants.talonElevator1);
    	elevator2 = new Talon(Constants.talonElevator2);
    	solenoid = new Solenoid(1, Constants.solenoidChannel);
    	encFR = new SmoothedEncoder(0, 1, false, Encoder.EncodingType.k1X);
    	encFL = new SmoothedEncoder(2, 3, false, Encoder.EncodingType.k1X);
    	encBR = new SmoothedEncoder(4, 5, false, Encoder.EncodingType.k1X);
    	encBL = new SmoothedEncoder(6, 7, false, Encoder.EncodingType.k1X);
    	encElevator = new SmoothedEncoder(8, 9, false, Encoder.EncodingType.k1X);
    	robotDrive = new RobotDrive(FrontL, BackL, FrontR, BackR);
    	robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);
    	robotDrive.setInvertedMotor(MotorType.kRearLeft, true);
    	gyro = new Gyro(Constants.gyroChannel);
    	smartDash = new SmartDashboard();
    	compressor.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	robotDrive.mecanumDrive_Cartesian(driveValue[0], driveValue[1], driveValue[2], 0);
    	
    	driveValue[0] = -xboxSupremeController.getX();
    	driveValue[1] = -xboxSupremeController.getY();
    	driveValue[2] = -(xboxSupremeController.getRawAxis(4));
    	
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
    		robotDrive.mecanumDrive_Cartesian(xboxSupremeController.getX(), xboxSupremeController.getY(), -xboxSupremeController.getRawAxis(4), 0);
	    	
	    	if(xboxSupremeController.getRawButton(1) || xboxMinor.getRawButton(1)){
	    		
	    		elevator1.set(lift);
	    		elevator2.set(lift);
	    		
	    	}else if(xboxSupremeController.getRawButton(4) || xboxMinor.getRawButton(4)){
	    		
	    		elevator1.set(lower);
	    		elevator2.set(lower);
	    	}
	    	
	    if(xboxSupremeController.getRawButton(2) || (xboxMinor.getRawButton(2))){
	    	solenoid.set(!arms);
	    	}
	    
	    	
	    	
	    	
	    	
	    	smartDash.putNumber("Front Right Rate ", encFR.getRate());
	    	smartDash.putNumber("Front Left Rate ", encFL.getRate());
	    	smartDash.putNumber("Back Right Rate ", encBR.getRate());
	    	smartDash.putNumber("Back Left Rate ", encBL.getRate());
	    	smartDash.getNumber("Vator Rate ", encElevator.getRate());
    	}
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	smartDash.putNumber("Front Right Rate ", encFR.getRate());
    	smartDash.putNumber("Front Left Rate ", encFL.getRate());
    	smartDash.putNumber("Back Right Rate ", encBR.getRate());
    	smartDash.putNumber("Back Left Rate ", encBL.getRate());
    	smartDash.getNumber("Vator Rate ", encElevator.getRate());
    	
        if (xboxSupremeController.getRawButton(4)) {
            y = .5;
            x = 0.0;
            z = 0.0;
        } else if (xboxSupremeController.getRawButton(1)) {
            y = -.5;
            x = 0.0;
            z = 0.0;
        } else if (xboxSupremeController.getRawButton(3)) {
            y = 0.0;
            x = .5;
            z = 0.0;
        } else if (xboxSupremeController.getRawButton(2)) {
            y = 0.0;
            x = -.5;
            z = 0.0;
        } else {
            y = 0.0;
            x = 0.0;
            z = 0.0;
        }
        robotDrive.mecanumDrive_Cartesian(x, y, z, 0);   
    }
    
    public void liftElevator(int height){
    	switch(height){
    	
    	case 0:
    		while (encElevator.getDistance() > 2){
    			
    		}
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
    	}
    	
    }
    
    
}
