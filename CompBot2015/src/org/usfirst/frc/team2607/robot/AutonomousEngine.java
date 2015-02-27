package org.usfirst.frc.team2607.robot;

import java.lang.reflect.Array;
import java.util.Vector;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousEngine implements Runnable {
	Timer autoTimer;
	Robot theBot;
	robovikingMotionProfiler motion;

	int step;
	int mode;


	public AutonomousEngine(Robot robot){
		theBot = robot;
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
		switch(mode) {
			case 0:
				SmartDashboard.putString("autonMode", "Auton: NONE");
				break;
			case 1:
				SmartDashboard.putString("autonMode", "Auton: 3-tote serpentine");
				break;
			case 2:
				SmartDashboard.putString("autonMode", "Auton: 3-tote straight");
				break;
			case 3:
				SmartDashboard.putString("autonMode", "Auton: Recycle Bin and 1 Tote");
				break;
			case 4:
				SmartDashboard.putString("autonMode", "Auton: Just drive forward");
				break;
			case 5:
				SmartDashboard.putString("autonMode", "Auton: Recycle Bin");
				break;
			case 6:
				SmartDashboard.putString("autonMode", "Auton: 1 Tote");
				break;
			default:
				SmartDashboard.putString("autonMode", "UNKNOWN!!");
				break;
		}
	}


	//True 3-tote auto
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

	//Drive straight 3 tote auto
	private void autoModeTwo() {
		Vector<Double> strafeRight = new Vector<Double>();
		strafeRight.add(.6);
		strafeRight.add(.2);

		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0);
		forward.add(-.6);

		Vector<Double> fast = new Vector<Double>();
		fast.add(0.0);
		fast.add(-.9);

		Vector<Double> strafeLeft = new Vector<Double>();
		strafeLeft.add(-.35);
		strafeLeft.add(-.08);

		Vector<Double> back = new Vector<Double>();
		back.add(0.0);
		back.add(.1);

		theBot.robotDrive.resetDistance();
		try {
			// close the hooks
			theBot.motaVator.arms.set(true);
			Thread.sleep(1500);
			// set elevator to carrying position
			theBot.motaVator.goToHeight(-18.5);

			// drive forward to next tote
			motion.driveUntilDistance(80.4,  forward, false);
			// Stop so mecanum wheels can accelerate together
			theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);

			theBot.motaVator.lowerManual();
			while(theBot.motaVator.enc.getDistance() < -12) Thread.sleep(2);
			theBot.motaVator.equilibrium();

			theBot.motaVator.arms.set(false);
			Thread.sleep(100);

			theBot.motaVator.goToHeight(0);
			while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);

			theBot.motaVator.arms.set(true);
			Thread.sleep(600);

			// set elevator to carrying position
			theBot.motaVator.goToHeight(-18.5);
			// drive forward to next tote
			motion.driveUntilDistance(80.4,  forward, false);
			// Stop so mecanum wheels can accelerate together
			theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);

			theBot.motaVator.lowerManual();
			while(theBot.motaVator.enc.getDistance() < -12) Thread.sleep(2);
			theBot.motaVator.equilibrium();

			theBot.motaVator.arms.set(false);
			Thread.sleep(100);

			theBot.motaVator.goToHeight(0);
			while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);

			theBot.motaVator.arms.set(true);
			Thread.sleep(600);

			motion.setFtB(Constants.ftbCorrectionTwoTote);
			motion.driveUntilDistance(40,  strafeRight, false);

			System.out.println("Rotating!");
			motion.rotateUntilDegree(90, false);

			System.out.println("Driving!");
			motion.driveUntilDistance(87, fast, false);

			theBot.motaVator.arms.set(false);

			motion.driveUntilDistance(10, back, false);
		} catch (Exception e){

		}
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
//		Vector<Double> forward = new Vector<Double>();
//		forward.add(0.0);
//		forward.add(-.3);
//
//		theBot.robotDrive.resetDistance();
//
//		try {
//		theBot.motaVator.arms.set(true);
//
//		motion.rotateUntilDegree(90, false);
//
//		Thread.sleep(300);
//
//		motion.driveUntilDistance(150, forward, false);
//
//		} catch (InterruptedException e) {
//		}

		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0);
		forward.add(-.4);

		theBot.robotDrive.resetDistance();

		try {
		theBot.motaVator.arms.set(true);
		Thread.sleep(500);

		theBot.motaVator.goToHeight(-18);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);

		motion.rotateUntilDegree(90, false);
		Thread.sleep(300);

		motion.driveUntilDistance(135, forward, false);

		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);

		theBot.motaVator.goToHeight(-1);

		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);

		theBot.motaVator.arms.set(false);


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
