
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
	double x, y, z;
	boolean Saul;
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
    	Saulenoid = new Solenoid(Constants.solenoidChannel);
    	encFR = new SmoothedEncoder(0, 1, false, Encoder.EncodingType.k1X);
    	encFL = new SmoothedEncoder(2, 3, false, Encoder.EncodingType.k1X);
    	encBR = new SmoothedEncoder(4, 5, false, Encoder.EncodingType.k1X);
    	encBL = new SmoothedEncoder(6, 7, false, Encoder.EncodingType.k1X);
    	encVator = new SmoothedEncoder(8, 9, false, Encoder.EncodingType.k1X);
    	DriveRobot = new RobotDrive(FrontL, BackL, FrontR, BackR);
    	DriveRobot.setInvertedMotor(MotorType.kFrontLeft, true);
    	DriveRobot.setInvertedMotor(MotorType.kRearLeft, true);
    	gyroPyro = new Gyro(Constants.gyroChannel);
    	
    	comPreston.start();
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
    	DriveRobot.mecanumDrive_Cartesian(sticktoriaJustice.getX(), sticktoriaJustice.getY(), -sticktoriaJustice.getRawAxis(4), 0);

    	
    	
    	Saulenoid.set(sticktoriaJustice.getToggleButton(2));

    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	

        if (sticktoriaJustice.getRawButton(4)) {
            y = -.5;
            x = 0.0;
            z = 0.0;
        } else if (sticktoriaJustice.getRawButton(1)) {
            y = .5;
            x = 0.0;
            z = 0.0;
        } else if (sticktoriaJustice.getRawButton(3)) {
            y = 0.0;
            x = -.5;
            z = 0.0;
        } else if (sticktoriaJustice.getRawButton(2)) {
            y = 0.0;
            x = .5;
            z = 0.0;
        } else {
            y = 0.0;
            x = 0.0;
            z = 0.0;
            
            DriveRobot.mecanumDrive_Cartesian(x, y, z, 0);
        }

    }
    
    }
    

