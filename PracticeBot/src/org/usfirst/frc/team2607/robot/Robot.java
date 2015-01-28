
package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.RobotDrive;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	Joystick sticktoriaJustice; 
	Compressor comPreston; 
	Talon FrontL;
	Talon FrontR;
	Talon BackL;
	Talon BackR;
	Talon Hellovator1, Hellovator2;
	Solenoid Saulenoid;
	RobotDrive DriveRobot;
	SmoothedEncoder encFL, encFR, encRL, encRR;
	SmoothedEncoder encVator;
	
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	sticktoriaJustice = new Joystick(0);
    	comPreston = new Compressor(1);
    	FrontL = new Talon(1);
    	FrontR = new Talon(0);
    	BackL = new Talon(3);
    	BackR = new Talon(2);
    	Hellovator1 = new Talon(4);
    	Hellovator2 = new Talon(5);
    	Saulenoid = new Solenoid(1, 0);
    	encFR = new SmoothedEncoder(0, 1, false, Encoder.EncodingType.k1X);
    	encFL = new SmoothedEncoder(2, 3, false, Encoder.EncodingType.k1X);
    	encRR = new SmoothedEncoder(4, 5, false, Encoder.EncodingType.k1X);
    	encRL = new SmoothedEncoder(6, 7, false, Encoder.EncodingType.k1X);
    	encVator = new SmoothedEncoder(8, 9, false, Encoder.EncodingType.k1X);
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
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
