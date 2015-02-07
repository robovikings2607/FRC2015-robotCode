
package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	robovikingStick sticktoriaJustice; 
	Compressor comPreston; 
	Talon FrontL;
	Talon FrontR;
	Talon BackL;
	Talon BackR;
	Talon Hellovator1, Hellovator2;
	Solenoid Saulenoid;
	RobotDrive DriveRobot;
	SmoothedEncoder encFL, encFR, encBL, encBR;
	SmoothedEncoder encVator;
	Gyro gyroPyro;
	SmartDashboard iDash5s;
	double x, y, z;
	double lift = -1;
	double lower = .50;
	double[] driveValerie = new double[3];
	double[] DeadZones = new double[]{0.25, 0.25, 0.25};
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	sticktoriaJustice = new robovikingStick(0);
    	comPreston = new Compressor(1);
    	FrontL = new Talon(Constants.talonFrontLeft);
    	FrontR = new Talon(Constants.talonFrontRight);
    	BackL = new Talon(Constants.talonBackLeft);
    	BackR = new Talon(Constants.talonBackRight);
    	Hellovator1 = new Talon(Constants.talonElevator1);
    	Hellovator2 = new Talon(Constants.talonElevator2);
    	Saulenoid = new Solenoid(1,Constants.solenoidChannel);
    	encFR = new SmoothedEncoder(0, 1, false, Encoder.EncodingType.k1X);
    	encFL = new SmoothedEncoder(2, 3, false, Encoder.EncodingType.k1X);
    	encBR = new SmoothedEncoder(4, 5, false, Encoder.EncodingType.k1X);
    	encBL = new SmoothedEncoder(6, 7, false, Encoder.EncodingType.k1X);
    	encVator = new SmoothedEncoder(8, 9, false, Encoder.EncodingType.k1X);
    	DriveRobot = new RobotDrive(FrontL, BackL, FrontR, BackR);
    	DriveRobot.setInvertedMotor(MotorType.kFrontLeft, true);
    	DriveRobot.setInvertedMotor(MotorType.kRearLeft, true);
    	gyroPyro = new Gyro(Constants.gyroChannel);
    	iDash5s = new SmartDashboard();
    	comPreston.start();
    	gyroPyro.initGyro();
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
    	DriveRobot.mecanumDrive_Cartesian(driveValerie[0], driveValerie[1], driveValerie[2], 0);
    	
    	driveValerie[0] = -sticktoriaJustice.getX();
    	driveValerie[1] = -sticktoriaJustice.getY();
    	driveValerie[2] = -(sticktoriaJustice.getRawAxis(4)/2);
    	
    	for (int i = 0; i < 3; i++) {
    		if (Math.abs(driveValerie[i]) <= DeadZones[i]) {
    			driveValerie[i] = 0;
    		}
    		if (driveValerie[i] > DeadZones[i] && driveValerie[i] <= DeadZones[i] * 2) {
    			driveValerie[i] = (driveValerie[i] - .10) * 2;
    		}
    		if (driveValerie[i] < -DeadZones[i] && driveValerie[i] >= -2 * DeadZones[i]) {
    			driveValerie[i] = (driveValerie[i] + .10) * 2;
    		}
    	}
    	//	DriveRobot.mecanumDrive_Cartesian(sticktoriaJustice.getX(), sticktoriaJustice.getY(), -sticktoriaJustice.getRawAxis(4), 0);
    	
    	if(sticktoriaJustice.getRawButton(1)){
    		
    		Hellovator1.set(lower);
    		Hellovator2.set(lower);
    		
    	}else{ if(sticktoriaJustice.getRawButton(4)){
    		
    		Hellovator1.set(lift);
    		Hellovator2.set(lift);
    	} else {
    		Hellovator1.set(0);
    		Hellovator2.set(0);
    	}
    	}
    	
    	Saulenoid.set(sticktoriaJustice.getToggleButton(2));

    	iDash5s.putNumber("Front Right Rate ", encFR.getRate());
    	iDash5s.putNumber("Front Left Rate ", encFL.getRate());
    	iDash5s.putNumber("Back Right Rate ", encBR.getRate());
    	iDash5s.putNumber("Back Left Rate ", encBL.getRate());
    	iDash5s.getNumber("Vator Rate ", encVator.getRate());
    
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testInit(){
    	gyroPyro.reset();
    }
    
    public void testPeriodic() {
    	
    	if(sticktoriaJustice.getOneShotButton(7)){
    		gyroPyro.reset();
    	}
    	double angler = gyroPyro.getAngle();
    	iDash5s.putNumber("Front Right Rate ", encFR.getRate());
    	iDash5s.putNumber("Front Left Rate ", encFL.getRate());
    	iDash5s.putNumber("Back Right Rate ", encBR.getRate());
    	iDash5s.putNumber("Back Left Rate ", encBL.getRate());
    	iDash5s.getNumber("Vator Rate ", encVator.getRate());
    	iDash5s.getNumber("Angle of the Bot", gyroPyro.getAngle());
    	
       if (sticktoriaJustice.getRawButton(4)) {
            y = .5;
            x = 0.0;
            z = angler*.004;
        } else if (sticktoriaJustice.getRawButton(1)) {
            y = -.5;
            x = 0.0;
            z = angler*.004;
        } else if (sticktoriaJustice.getRawButton(3)) {
            y = 0.0;
            x = .5;
            z = angler*.004;
        } else if (sticktoriaJustice.getRawButton(2)) {
            y = 0.0;
            x = -.5;
            z = angler*.004;
        } else {
            y = 0.0;
            x = 0.0;
            z = 0.0;
            
            
        } 
/*if(sticktoriaJustice.getRawButton(1)){
    		
    		Hellovator1.set(lift);
    		Hellovator2.set(lift);
    		
    	}else{ if(sticktoriaJustice.getRawButton(4)){
    		
    		Hellovator1.set(lower);
    		Hellovator2.set(lower);
    	} else {
    		Hellovator1.set(0);
    		Hellovator2.set(0);
    	}
    	}*/
    	
    	//Saulenoid.set(sticktoriaJustice.getToggleButton(2));
DriveRobot.mecanumDrive_Cartesian(x, y, z, 0);
    }
    
    }
    

