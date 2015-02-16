package org.usfirst.frc.team2607.robot;

import java.lang.reflect.Array;
import java.util.Vector;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousEngine implements Runnable {
	Timer autoTimer;
	DigitalInput autoEye;
	Robot theBot;
	robovikingMotionProfiler motion;
	
	int step;
	int mode;
	
	
	public AutonomousEngine(Robot robot){
		theBot = robot;
		autoEye = new DigitalInput(12); //not necessarily 12
		autoTimer = new Timer();
		step = 0;
		mode = 0;
		theBot.BackL.setGearPID(false);
		theBot.FrontL.setGearPID(false);
		theBot.BackR.setGearPID(false);
		theBot.FrontR.setGearPID(false);
		motion = new robovikingMotionProfiler(theBot.robotDrive);
	}

	public void selectMode() {
		if (++mode > 6) mode = 0;	
		
		SmartDashboard.putNumber("autoMode", mode);
	}

	private void autoModeOne() {
		Vector<Double> strafeRight = new Vector<Double>();
		strafeRight.add(.35);
		strafeRight.add(0.0);
		
		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0); 
		forward.add(-.5);
		
		Vector<Double> strafeLeft = new Vector<Double>();
		strafeLeft.add(-.35); 
		strafeLeft.add(-.08);
		
		theBot.robotDrive.resetDistance();
		try {
			// close the hooks
			theBot.motaVator.arms.set(true);
			Thread.sleep(330);
			// set elevator to carrying position
			theBot.motaVator.goToHeight(-18.5);
			// strafe right 
			Thread.sleep(100);
			motion.setFtB(Constants.ftbCorrectionOneTote);
			motion.driveUntilDistance(31,  strafeRight, false);
			// drive forward to next tote
			motion.driveUntilDistance(87,  forward, false);
			// Stop so mecanum wheels can accelerate together
			theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
			Thread.sleep(600);
			// strafe left 
			motion.driveUntilDistance(36,  strafeLeft, false);
			
			theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
			
			theBot.motaVator.lowerManual();
			while(theBot.motaVator.enc.getDistance() < -12) Thread.sleep(2);
			theBot.motaVator.equilibrium();
			
			theBot.motaVator.arms.set(false);
			Thread.sleep(100);
			
			theBot.motaVator.goToHeight(0);
			while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
			
			theBot.motaVator.arms.set(true);
			Thread.sleep(700);
			
			theBot.motaVator.goToHeight(-18.5);
			
			// strafe right 
			motion.setFtB(Constants.ftbCorrectionTwoTote);
			motion.driveUntilDistance(36,  strafeRight, false);
			
			motion.driveUntilDistance(90,  forward, false);
			
			theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
			Thread.sleep(600);
			// strafe left 
			motion.driveUntilDistance(38,  strafeLeft, false);
			
			theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
			
			/*motion.driveUntilDistance(82.5,  forward, false);
			// Stop so mecanum wheels can accelerate together
			theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
			Thread.sleep(600);
			// strafe left 
			motion.driveUntilDistance(40,  strafeLeft, false);
			
			theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
			
			theBot.motaVator.lowerManual();
			while(theBot.motaVator.enc.getDistance() < -12) Thread.sleep(2);
			theBot.motaVator.equilibrium();
			
			theBot.motaVator.arms.set(false);
			Thread.sleep(100);
			
			theBot.motaVator.goToHeight(-.5);
			while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
			
			theBot.motaVator.arms.set(true);
			Thread.sleep(500);
			
			theBot.motaVator.goToHeight(-18.5);
			
			// strafe right 
			motion.driveUntilDistance(30,  strafeRight, false);
			
			theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
			 * 
			 * 
			 */
		} catch (Exception e) {}
	}
	
	private void autoModeTwo() {
		theBot.motaVator.arms.set(true);
		theBot.motaVator.goToCarryingPos();
		
	}
	
	//Stacks a Recycling container on a tote, then rotates and drives to auto zone
	public void autoModeThree(){
		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0); 
		forward.add(-.35);
		
		theBot.robotDrive.resetDistance();
		
		try {
		theBot.motaVator.arms.set(true);
		Thread.sleep(500);
		
		theBot.motaVator.goToHeight(-18);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		motion.driveUntilDistance(17, forward, false);
		
		theBot.motaVator.goToHeight(-15);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		theBot.motaVator.arms.set(false);
		
		theBot.motaVator.goToHeight(-3);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		theBot.motaVator.arms.set(true);
		Thread.sleep(500);
		
		motion.rotateUntilDegree(-90, false);
		Thread.sleep(300);
		
		motion.driveUntilDistance(135, forward, false);
		
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		
		theBot.motaVator.goToHeight(-1);
		
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		theBot.motaVator.arms.set(false);
		
		} catch (InterruptedException e) {

		}
		
	}
	
	// Just Drives Forward
	public void autoModeFour(){
		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0); 
		forward.add(-.3);
		
		theBot.robotDrive.resetDistance();
		
		motion.driveUntilDistance(75, forward, false);
		
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		
	}
	
	//Grabs recycling container, rotates 90 degrees, and drives to auto zone
	public void autoModeFive(){
		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0); 
		forward.add(-.4);
		
		theBot.robotDrive.resetDistance();
		
		try {
		theBot.motaVator.arms.set(true);
		Thread.sleep(500);
		
		theBot.motaVator.goToHeight(-18);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		motion.rotateUntilDegree(-90, false);
		Thread.sleep(300);
		
		motion.driveUntilDistance(135, forward, false);
		
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		
		theBot.motaVator.goToHeight(-1);
		
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		theBot.motaVator.arms.set(false);
			
			
		} catch (InterruptedException e) {

		}
		
		
	}
	
	//One Tote Auto
	public void autoModeSix(){
		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0); 
		forward.add(-.3);
		
		theBot.robotDrive.resetDistance();
		
		try {
		theBot.motaVator.arms.set(true);
		
		motion.rotateUntilDegree(90, false);
		
		Thread.sleep(300);
			
		motion.driveUntilDistance(150, forward, false);
			
		} catch (InterruptedException e) {
		}
		
		
	}
	
	
	@Override
	public void run() {
		// called by Thread.start();
		System.out.println("Auto Thread Start");
			switch (mode) {
				case 0:
					// turn off outputs
					break;
				case 1:
					System.out.println("Running Auto 1");
					autoModeOne();	// only exits when done, or interrupted
					mode = 0;
					break;
				case 2:
					System.out.println("Running Auto 2");
					autoModeTwo();	// only exits when done, or interrupted
					mode = 0;
					break;
					
				case 3: 
					System.out.println("Running Auto 3");
					autoModeThree();
					mode = 0;
					break;
					
				case 4:
					System.out.println("Running Auto 4");
					autoModeFour();
					mode = 0;
					break;
					
				case 5:
					System.out.println("Running Auto 5");
					autoModeFive();
					mode = 0;
					break;
					
				case 6:
					System.out.println("Running Auto 6");
					autoModeSix();
					mode = 0;
					break;
					
				default:
			}
		System.out.println("Exiting Auto");
		
	}
	
	
	
}
