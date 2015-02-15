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
		if (++mode > 2) mode = 0;	
		
		SmartDashboard.putNumber("autoMode", mode);
	}

	private void autoModeOne() {
		Vector<Double> strafeRight = new Vector<Double>();
		strafeRight.add(.35);
		strafeRight.add(0.0);
		
		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0); 
		forward.add(-.3);
		
		Vector<Double> strafeLeft = new Vector<Double>();
		strafeLeft.add(-.35); 
		strafeLeft.add(-.14);
		
		theBot.robotDrive.resetDistance();
		try {
			// close the hooks
			theBot.motaVator.arms.set(true);
			Thread.sleep(300);
			// set elevator to carrying position
			theBot.motaVator.goToHeight(-18.5);
			// strafe right 
			motion.driveUntilDistance(30,  strafeRight, false);
			// drive forward to next tote
			motion.driveUntilDistance(82.5,  forward, false);
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
		} catch (Exception e) {}
	}
	
	private void autoModeTwo() {
		theBot.motaVator.arms.set(true);
		theBot.motaVator.goToCarryingPos();
		
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
				default:
			}
		System.out.println("Exiting Auto");
		
	}
	
	
	
}
