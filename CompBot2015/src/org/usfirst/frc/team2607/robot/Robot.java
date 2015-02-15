
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
	
	elevator motaVator; // this is the elevator....
	AutonomousEngine auto;
	Thread autoThread = null;
	
	IMUAdvanced navx;
	SerialPort comPort;
	Solenoid gearShiftSolenoid;
	robovikingMecanumDrive robotDrive;
	robovikingStick xboxSupremeController, xboxMinor;
	SmartDashboard smartDash;
	
	double x, y, z;
	double[] driveValue = new double[3];
	double[] deadZones = {0.15, 0.15, 0.15};
	double tempAngle = 0;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	motaVator = new elevator();
    //	new Thread(motaVator).start();
    	
    	xboxSupremeController = new robovikingStick(0);
    	xboxMinor = new robovikingStick(1);
    	FrontL = new WheelRPMController("FrontLeft",0,true);
    	FrontR = new WheelRPMController("FrontRight",1,true);
    	BackL = new WheelRPMController("BackLeft", 2,true);
    	BackR = new WheelRPMController("BackRight", 3,true);

    	gearShiftSolenoid = new Solenoid(1, Constants.gearShiftChannel);
    	
    		
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

    	robotDrive = new robovikingMecanumDrive(FrontL, BackL, FrontR, BackR, navx);
    	robotDrive.setInvertedMotor(MotorType.kFrontLeft, true);
    	robotDrive.setInvertedMotor(MotorType.kRearLeft, true);    	
    	smartDash = new SmartDashboard();
    	
    	auto = new AutonomousEngine(this);
    }

    public void disabledInit() {
    	
    }

    public void disabledPeriodic(){
    	if (xboxSupremeController.getOneShotButton(8)){
    		// increment auto mode
    		auto.selectMode();
    	}
    }
    
    /**
     * This function is called periodically during autonomous
     */
    
    
    public void autonomousInit(){
    	autoThread = new Thread(auto);
    	autoThread.start();
    	
    }
    
    public void autonomousPeriodic() {
    	
    }

    
    
    
    /*public void teleopInit(){
    	gyro.reset();
    }
    */

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	
    	BackL.enableLogging(xboxSupremeController.getToggleButton(7));
    	    	    	
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
    	
    	if(xboxSupremeController.getToggleButton(5) || xboxMinor.getToggleButton(5)){
    	
	    	if((xboxSupremeController.getOneShotButton(1) || xboxMinor.getOneShotButton(1))){
	    		motaVator.lower(); // lowers elevator
	    		
	    	} else if((xboxSupremeController.getOneShotButton(4) || xboxMinor.getOneShotButton(4))){
	    		motaVator.raise();  // raises elevator
	    	} 
	    		    
    	} else {
	    
	    if((xboxSupremeController.getRawButton(1) || xboxMinor.getRawButton(1))){
    		
    		motaVator.lowerManual(); // lowers elevator
    	} else if((xboxSupremeController.getRawButton(4) || xboxMinor.getRawButton(4))){
    		motaVator.raiseManual();  // raises elevator
    	} else {
    		motaVator.equilibrium();
    	}
    	}
    	
    	if(xboxSupremeController.getOneShotButton(2) || (xboxMinor.getOneShotButton(2))){
    	motaVator.grab(); // open or close arms
    	}
    	
	    	   	
    	//robotDrive.mecanumDrive_Cartesian(driveValue[0], driveValue[1], driveValue[2], 0);
	    robotDrive.correctedMecanumDrive(driveValue[0], driveValue[1], driveValue[2], 0.0, -.15);
	    

    	BackL.logEntry();

    }
     
    public void testInit(){
    	navx.zeroYaw();
    	testTick = 0;
    }
        
    private int testTick;
  
    public void testPeriodic() {

    	if(xboxSupremeController.getToggleButton(5) || xboxMinor.getToggleButton(5)){
        	
	    	if((xboxSupremeController.getOneShotButton(1) || xboxMinor.getOneShotButton(1))){
	    		motaVator.goToHeight(-12.0); // lowers elevator
	    		
	    	} else if((xboxSupremeController.getOneShotButton(4) || xboxMinor.getOneShotButton(4))){
	    		motaVator.goToHeight(-18.5);  // raises elevator
	    	} 
	    		    
    	} else {
	    
	    if((xboxSupremeController.getRawButton(1) || xboxMinor.getRawButton(1))){
    		
    		motaVator.lowerManual(); // lowers elevator
    	} else if((xboxSupremeController.getRawButton(4) || xboxMinor.getRawButton(4))){
    		motaVator.raiseManual();  // raises elevator
    	} else {
    		motaVator.equilibrium();
    	}
    	}
    	
    	if(xboxSupremeController.getOneShotButton(2) || (xboxMinor.getOneShotButton(2))){
    		motaVator.grab(); // open or close arms
    	}
    	
    	if (++testTick >= 20) {
    		System.out.printf("SP: %.8f PV: %.8f, Err: %.8f MV: %.8f\n", motaVator.pid.getSetpoint(), 
    															motaVator.enc.getDistance(), 
    															motaVator.pid.getError(), 
    															motaVator.pid.get());
    		testTick = 0;
    	}
    }
    
/*    
    public void testPeriodic() {
       	
    	double angler = 0.0; //xboxSupremeController.getToggleButton(8) ? navx.getYaw() : 0.0;
    	
    	if (xboxSupremeController.getOneShotButton(7)){
    		navx.zeroYaw();
    	}
    	
    	gearShiftSolenoid.set(false);
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
    	FrontL.logEntry();
    	FrontR.logEntry();
    	BackL.logEntry();
    	BackR.logEntry();
    }
*/	    
    
   
    
    
}
