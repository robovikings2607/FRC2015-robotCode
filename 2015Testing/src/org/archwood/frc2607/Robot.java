
package org.archwood.frc2607;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	// min = -8200, max = 8200  (shifter in)
	// min = -18200, max = 18200 (shifter out)
	Solenoid fork;
	Joystick driveStick;
	CANTalon motor;
	Encoder enc;
	PIDController pid;
    PrintWriter logFile = null;
    
	public void robotInit() {
    	fork = new Solenoid(1,0);
    	driveStick = new Joystick(0);
    	motor = new CANTalon(4);
    	enc = new SmoothedEncoder(0, 1, true, EncodingType.k1X);
    	pid = new PIDController(0.0, 0.0, 0.0, enc, motor);
    	logFile = null;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    
    public void disabledInit() {
    	if (logFile != null) {
    		logFile.close();
    		logFile = null;
    	}
    }
    /**
     * This function is called periodically during operator control
     */
    double minRate, maxRate;    
    public void teleopInit() {
    	enc.reset();
    	minRate = 0.0;
    	maxRate = 0.0;
       	double Kp = .000058; //.000026 for highGear
    	double Ki = .000050; //.000015 for highGear 
    	double Kd = 0.0;

    	pid.setPID(Kp, Ki, Kd);
    	pid.setInputRange(-8000, 8000);
    	pid.setOutputRange(-1.0, 1.0);
    	pid.disable();

    	String fileName = "/home/lvuser/FR-PIDLoopencoderData.lowGear" + System.currentTimeMillis() + ".csv";
    	try {
    		logFile = new PrintWriter(new FileOutputStream(fileName));
    		logFile.printf("Kp = %.8f, Ki = %.8f, Kd = %.8f%n", Kp, Ki, Kd);
    		logFile.println("Time,SP,PV,OUTP");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public void teleopPeriodic() {
    	double desiredPct = 0.0;
    	if (driveStick.getRawButton(4)) {
    		desiredPct = .85;
    		//motor.set(1);
    	} else if (driveStick.getRawButton(2)) {
    		desiredPct = -.85;
    		// motor.set(-1);
    	} else {
    		desiredPct = 0.0;
    		//motor.set(0);
    	}
    	
    	if (driveStick.getRawButton(5)) {
    		pid.setSetpoint(desiredPct * 8000);
    		pid.enable();
//    		desiredPct *= 1.3;
    	} else {
    		pid.disable();
    	}
    	    	
    	logFile.printf("%d,%.0f,%.0f,%.4f%n", System.currentTimeMillis(), 
    			pid.getSetpoint(),
//    			0.0,
    			enc.pidGet(), 
    			pid.get());
    	if (enc.pidGet() < minRate) minRate = enc.pidGet();
    	if (enc.pidGet() > maxRate) maxRate = enc.pidGet();
    	SmartDashboard.putNumber("Encoder Rate", enc.getRate());
    	SmartDashboard.putNumber("Smoothed Rate", enc.pidGet());
    	SmartDashboard.putNumber("Min Rate", minRate);
    	SmartDashboard.putNumber("Max Rate", maxRate);
    	SmartDashboard.putBoolean("Stopped", enc.getStopped());
    	SmartDashboard.putBoolean("Direction", enc.getDirection());
   }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
