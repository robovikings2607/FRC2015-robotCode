
package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
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
	
	robovikingStick sticktoriaJustice, xboxMinor; 
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
	DigitalInput topSwitch, bottomSwitch;
	double x, y, z;
	double lift = -.80;
	double lower = .50;
	double[] driveValerie = new double[3];
	double[] DeadZones = new double[]{0.15, 0.15, 0.15};
	boolean arms = false;
	
	I2C arduino = new I2C(Port.kOnboard ,4);
	int i2cTick = 0;
	/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	sticktoriaJustice = new robovikingStick(0);
    	xboxMinor = new robovikingStick(1);
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
    	encVator = new SmoothedEncoder(10, 11, false, Encoder.EncodingType.k1X);
    	bottomSwitch = new DigitalInput(8);
    	topSwitch = new DigitalInput(9);
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
	private double correctedZ = 0.0;
    public void teleopPeriodic() {
    	
    	if(sticktoriaJustice.getOneShotButton(7)){
    		gyroPyro.reset();
    	}
    	
    	double angler = gyroPyro.getAngle();

    	
    	driveValerie[0] = -(sticktoriaJustice.getX() * .65);
    	driveValerie[1] = -(sticktoriaJustice.getY() * .65);
    	driveValerie[2] = -(sticktoriaJustice.getRawAxis(4)/2);
    	
    	for (int i = 0; i < 3; i++) {
    		if (Math.abs(driveValerie[i]) <= DeadZones[i]) {
    			driveValerie[i] = 0;
    		}
    		if (driveValerie[i] < -DeadZones[i] && driveValerie[i] >= -2 * DeadZones[i]) {
    			driveValerie[i] = (driveValerie[i] + .15) * 1.5;
    		}
    		if (driveValerie[i] > DeadZones[i] && driveValerie[i] <= DeadZones[i] * 2) {
    			driveValerie[i] = (driveValerie[i] - .15) * 1.5;
    		} 
    		
    		
    	}
    	//	DriveRobot.mecanumDrive_Cartesian(sticktoriaJustice.getX(), sticktoriaJustice.getY(), -sticktoriaJustice.getRawAxis(4), 0);
    	
    	if(sticktoriaJustice.getRawButton(1) || xboxMinor.getRawButton(1)){
    		
    		Hellovator1.set(lower);
    		Hellovator2.set(lower);
    		
    	}else{ if(sticktoriaJustice.getRawButton(4) || xboxMinor.getRawButton(4)){
    		
    		Hellovator1.set(lift);
    		Hellovator2.set(lift);
    	} else {
    		Hellovator1.set(0);
    		Hellovator2.set(0);
    	}
    	}
    	
    	if (driveValerie[2] == 0){
    		driveValerie[2] = angler * .005;
    		iDash5s.putNumber("Angle ", gyroPyro.getAngle());
    		iDash5s.putNumber("Adjustment speed ", driveValerie[2]);
    	} else {
    		gyroPyro.reset();
    		iDash5s.putNumber("fail Angle ", gyroPyro.getAngle());
    		iDash5s.putNumber("fail Adjustment speed ", driveValerie[2]);
    	}
    	
    	if (sticktoriaJustice.getOneShotButton(2) || xboxMinor.getOneShotButton(2)){
    		arms = !arms;
    	Saulenoid.set(arms);
    	}

    	/*iDash5s.putNumber("Front Right Rate ", encFR.getRate());
    	iDash5s.putNumber("Front Left Rate ", encFL.getRate());
    	iDash5s.putNumber("Back Right Rate ", encBR.getRate());
    	iDash5s.putNumber("Back Left Rate ", encBL.getRate());
    	iDash5s.getNumber("Vator Rate ", encVator.getRate());
    	*/
    	DriveRobot.mecanumDrive_Cartesian(driveValerie[0], driveValerie[1], driveValerie[2], 0);
    
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testInit(){
    	gyroPyro.reset();
    	correctedZ = 0.0;
    }
    
    public void teleopInit(){
    	gyroPyro.reset();
    	correctedZ = 0.0;
    }
    
    public void testPeriodic() {
    	if (i2cTick++ > 30){
    		i2cTick = 0;
    		
    		byte[] toSend = {26};
    		
    		arduino.transaction(toSend, toSend.length, null, 0);
    		
    		System.out.println("Sent");
    	}
    }
    	
//    	
//    	if(sticktoriaJustice.getOneShotButton(7)){
//    		gyroPyro.reset();
//    		correctedZ = 0.0;
//    	}
//    	double angler = gyroPyro.getAngle();
//    	iDash5s.putNumber("Front Right Rate ", encFR.getRate());
//    	iDash5s.putNumber("Front Left Rate ", encFL.getRate());
//    	iDash5s.putNumber("Back Right Rate ", encBR.getRate());
//    	iDash5s.putNumber("Back Left Rate ", encBL.getRate());
//    	iDash5s.getNumber("Vator Rate ", encVator.getRate());
//    	iDash5s.getNumber("Angle of the Bot", gyroPyro.getAngle());
//    	
//    	correctedZ = angler * .005;
//       if (sticktoriaJustice.getRawButton(4)) {
//            y = .5;
//            x = 0.0;
//        } else if (sticktoriaJustice.getRawButton(1)) {
//            y = -.5;
//            x = 0.0;
//        } else if (sticktoriaJustice.getRawButton(3)) {
//            y = 0.0;
//            x = .5;
//        } else if (sticktoriaJustice.getRawButton(2)) {
//            y = 0.0;
//            x = -.5;
//        } else {
//            y = 0.0;
//            x = 0.0;
//        } 
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
//DriveRobot.mecanumDrive_Cartesian(x, y, correctedZ, 0);
    public void disabledPeriodic(){
    	iDash5s.putNumber("Angle of the Bot", gyroPyro.getAngle());
    	iDash5s.putBoolean("topSwitch", topSwitch.get());
    	iDash5s.putBoolean("bottomSwitch",bottomSwitch.get());
    }
    
    }
    

