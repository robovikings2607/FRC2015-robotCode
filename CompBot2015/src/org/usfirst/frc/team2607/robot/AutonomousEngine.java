package org.usfirst.frc.team2607.robot;

import java.lang.reflect.Array;
import java.util.Vector;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

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
	}

	private void autoModeOne() {
		Vector<Double> strafeRight = new Vector<Double>();
		strafeRight.add(.4);
		strafeRight.add(0.0);
		theBot.robotDrive.resetDistance();
		try {
			// close the hooks
			theBot.motaVator.arms.set(true);
			// set elevator to carrying position
			theBot.motaVator.goToCarryingPos();
			// strafe right 
			motion.driveUntilDistance(30 / 7,  strafeRight, false);
			
		
		
		
		} catch (Exception e) {}
	}
	
	private void autoModeTwo() {
		
	}
	
	@Override
	public void run() {
		// called by Thread.start();
			switch (mode) {
				case 0:
					// turn off outputs
					break;
				case 1:
					autoModeOne();	// only exits when done, or interrupted
					mode = 0;
					break;
				case 2:
					autoModeTwo();	// only exits when done, or interrupted
					mode = 0;
					break;
				default:
			}
		
	}
	
	
	
}
