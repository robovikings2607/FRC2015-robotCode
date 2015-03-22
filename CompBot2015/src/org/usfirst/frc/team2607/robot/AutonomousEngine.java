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
		if (++mode > 12) mode = 0;

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
			case 7:
				SmartDashboard.putString("autonMode", "Auton: Recycle Bin From Front");
				break;
			case 8:
				SmartDashboard.putString("autonMode", "Auton: Rotate other way one bin");
				break;
			case 9:
				SmartDashboard.putString("autonMode", "Auton: Don't run this (unfinished 3 tote)");
				break;
			case 10:
				SmartDashboard.putString("autonMode", "Auton: Test odometry");
				break;
			case 11:
				SmartDashboard.putString("autonMode", "Auton: test 3 tote pushing cans (triangles)");
				break;
			case 12:
				SmartDashboard.putString("autonMode", "Auton: Testing rotation target code");
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
			Thread.sleep(2000);
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

		motion.driveUntilDistance(115, forward, false);

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

		motion.driveUntilDistance(115, forward, false);

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

		motion.driveUntilDistance(115, forward, false);

		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);

		theBot.motaVator.goToHeight(-1);

		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);

		theBot.motaVator.arms.set(false);


		} catch (InterruptedException e) {

		}


	}
	
	//grabs a recycling container and moves backwards
	public void autoModeSeven(){
		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0);
		forward.add(-.4);
		
		Vector<Double> back = new Vector<Double>();
		back.add(0.0);
		back.add(.4);
		
		

		theBot.robotDrive.resetDistance();

		try {
		theBot.motaVator.goToHeight(-8.0);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		motion.driveUntilDistance(21, forward, false);
			
		theBot.motaVator.arms.set(true);
		Thread.sleep(500);

		theBot.motaVator.goToHeight(-12);
		Thread.sleep(400);

		motion.driveUntilDistance(100, back, false);

		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);

		theBot.motaVator.goToHeight(-7);

		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);

		theBot.motaVator.arms.set(false);
		
		} catch (InterruptedException e) {

		}

		
	}
	
	//Grabs recycling container, rotates other way 90 degrees, and drives to auto zone
	public void autoModeEight(){
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

		motion.driveUntilDistance(115, forward, false);

		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);

		theBot.motaVator.goToHeight(-1);

		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);

		theBot.motaVator.arms.set(false);


		} catch (InterruptedException e) {

		}


	}
	
	//3 Tote auton for reals this time, no joke. AKA God Mode
	public void autoModeNine(){
		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0);
		forward.add(-.6);
		
		Vector<Double> strafeRight = new Vector<Double>();
		strafeRight.add(.35);
		strafeRight.add(0.0);
		
		Vector<Double> strafeRightFast = new Vector<Double>();
		strafeRight.add(.75);
		strafeRight.add(0.0);
		
		Vector<Double> back = new Vector<Double>();
		forward.add(0.0);
		forward.add(.4);
		
		theBot.robotDrive.resetDistance();
		
		try {
		theBot.motaVator.arms.set(true);
		Thread.sleep(300);
		
		theBot.motaVator.goToHeight(-6);
		
		motion.rotateUntilDegree(40, false);
		Thread.sleep(300);
		
		motion.driveUntilDistance(10, forward, false);
		Thread.sleep(300);
		
		motion.rotateUntilDegree(-40, false);
		
		motion.driveUntilDistance(8, strafeRight, false);
		Thread.sleep(500);
		
		
		motion.driveUntilDistance(87,  forward, false);
		theBot.motaVator.goToHeight(-14);
		
		Thread.sleep(500);
		
		theBot.motaVator.goToHeight(-10);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		Thread.sleep(300);
		
		theBot.motaVator.arms.set(false);
		
		theBot.motaVator.goToHeight(-1);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		theBot.motaVator.arms.set(true);
		
		Thread.sleep(300);
		
		theBot.motaVator.goToHeight(-6);
		
		motion.rotateUntilDegree(40, false);
		Thread.sleep(300);
		
		motion.driveUntilDistance(10, forward, false);
		Thread.sleep(300);
		
		motion.rotateUntilDegree(-40, false);
		
		motion.driveUntilDistance(8, strafeRight, false);
		Thread.sleep(500);
		
		
		motion.driveUntilDistance(87,  forward, false);
		theBot.motaVator.goToHeight(-14);
		
		Thread.sleep(500);
		
		theBot.motaVator.goToHeight(-10);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		Thread.sleep(300);
		
		theBot.motaVator.arms.set(false);
		
		theBot.motaVator.goToHeight(-1);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		theBot.motaVator.arms.set(true);
		
		Thread.sleep(300);
		
		motion.driveUntilDistance(100,  strafeRightFast, false);
		
		theBot.motaVator.goToHeight(-1);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		theBot.motaVator.arms.set(false);
		
		motion.driveUntilDistance(8, back, false);
		
		
		
		} catch (InterruptedException e){
			
		}
	}
	
	//strafes at -45 degrees until reaching 48 inches
	public void autoModeTen(){
		theBot.robotDrive.resetDistance();
		motion.driveUntilDistancePulse(48, .8, -45, false);
		
	}
	
	public void autoModeEleven(){
		Vector<Double> forward = new Vector<Double>();
		forward.add(0.0);
		forward.add(-.6);
		
		Vector<Double> fastforward = new Vector<Double>();
		fastforward.add(0.0);
		fastforward.add(-.8);
		

		
		try {
		theBot.motaVator.arms.set(true);
		Thread.sleep(300);
		motion.rotateUntilDegree(-30, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);		
		theBot.motaVator.goToCarryingPos();
			
		motion.driveUntilDistance(17, forward, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		motion.rotateUntilDegree(60, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		
		motion.driveUntilDistance(10, fastforward, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		Thread.sleep(200);
		motion.rotateUntilDegree(-33.5, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		
		Thread.sleep(200);
		
		theBot.motaVator.goToHeight(-16);
		Thread.sleep(350);
		motion.driveUntilDistance(58.5, forward, false);  
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		
		theBot.motaVator.lowerManual();
		while(theBot.motaVator.enc.getDistance() < -12) Thread.sleep(2);
		theBot.motaVator.equilibrium();
		theBot.motaVator.arms.set(false);
		Thread.sleep(200);
		motion.setFtB(Constants.ftbCorrectionTwoTote);
		theBot.motaVator.goToHeight(-1);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		theBot.motaVator.arms.set(true);
		Thread.sleep(200);
		motion.rotateUntilDegree(-30, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);		
		theBot.motaVator.goToCarryingPos();
			
		motion.driveUntilDistance(17, forward, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		motion.rotateUntilDegree(60, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		
		motion.driveUntilDistance(10, fastforward, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		Thread.sleep(200);
		motion.rotateUntilDegree(-36, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		
		Thread.sleep(200);
		
		theBot.motaVator.goToHeight(-16);
		Thread.sleep(350);
		motion.driveUntilDistance(58.5, forward, false);  
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);

		theBot.motaVator.lowerManual();
		while(theBot.motaVator.enc.getDistance() < -12) Thread.sleep(2);
		theBot.motaVator.equilibrium();
		theBot.motaVator.arms.set(false);
		Thread.sleep(200);
		theBot.motaVator.goToHeight(-1);
		while(!theBot.motaVator.pid.onTarget()) Thread.sleep(2);
		
		theBot.motaVator.arms.set(true);
		Thread.sleep(200);
		theBot.motaVator.goToCarryingPos();
		
		motion.dsAcceptableRange=(15);
		motion.rotateUntilDegree(80, false);
		motion.dsAcceptableRange=(3);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);
		
    	theBot.gearShiftSolenoid.set(true);    	    	
    	theBot.FrontL.setGearPID(true);
    	theBot.FrontR.setGearPID(true);
    	theBot.BackL.setGearPID(true);
    	theBot.BackR.setGearPID(true);
    	
		theBot.motaVator.goToHeight(0.0);
		motion.driveUntilDistance(75, forward, false);
		theBot.robotDrive.correctedMecanumDrive(0, 0, 0, 0, 0);


		theBot.motaVator.arms.set(false);
		
    	theBot.gearShiftSolenoid.set(false);    	    	
    	theBot.FrontL.setGearPID(false);
    	theBot.FrontR.setGearPID(false);
    	theBot.BackL.setGearPID(false);
    	theBot.BackR.setGearPID(false);
		
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
	
	public void autoModeTwelve(){
		motion.driveUntilTargetRotation(.6, -90.0, 90.0, -.3, false);
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
					
				case 7:
					System.out.println("Running Auto 7");
					autoModeSeven();
					mode = 0;
					break;
				case 8:
					System.out.println("Running Auto 8");
					autoModeEight();
					mode = 0;
					break;
					
				case 9:
					System.out.println("Running Auto 9");
					autoModeNine();
					mode = 0;
					break;
					
				case 10:
					System.out.println("Running Auto 10");
					autoModeTen();
					mode = 0;
					break;
					
				case 11: 
					System.out.println("Running Auto 11");
					autoModeEleven();
					mode = 0;
					break;
					
				case 12:
					System.out.println("Running Auto 12");
					autoModeTwelve();
					mode = 0;

				default:
			}
		System.out.println("Exiting Auto");

	}



}
