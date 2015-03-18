package org.usfirst.frc.team2607.robot;

import java.util.Vector;

import com.kauailabs.nav6.frc.IMUAdvanced;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class robovikingMotionProfiler implements Runnable{
	
	robovikingMecanumDrive drive;
	DigitalInput autoEye;
	
	boolean running = false;
	
	int[][] pathToExecute = null;
	
	Vector<Double> usDirection = null;
	int usDistance = 0;
	Ultrasonic usSensor = null;
	int usAcceptableRange = 5;

	double dsDistance = 0;
	Vector<Double> dsDirection = null;
	int dsAcceptableRange = 5;
	
	double dgDegree = 0;
	Vector<Double> dgDirection = null;
	double dgAcceptableRange = 5;
	IMUAdvanced navX;
	
	Vector<Double> deDirection = null;
	boolean eyeTrigger;
	
	Vector<Double> dreDirection = null;
	
	private double ftbCorrection = Constants.ftbCorrectionNoTote;
	
	
	public robovikingMotionProfiler(robovikingMecanumDrive someDrive){
		drive = someDrive;
		navX = drive.getnavX();
	}
	
	public robovikingMotionProfiler(robovikingMecanumDrive someDrive, DigitalInput eye){
		drive = someDrive;
		navX = drive.getnavX();
		autoEye = eye;
	}
	
	private void drivePathCode (int[][] steps) throws InterruptedException{
		
		for (int i = 1; i < steps.length; i++){
			long startTime = System.currentTimeMillis();
			double targetPercent = 0;
			
			drive.correctedMecanumDrive((steps[i-1][0]), (steps[i-1][1]), 0, 0, -.15);
			
			while (System.currentTimeMillis() < startTime + steps[i][2]){
				targetPercent = tillTarget(startTime, System.currentTimeMillis(), steps[i][2]);
				
				drive.correctedMecanumDrive((steps[i-1][0] + targetPercent*(steps[i][0] - steps[i-1][0])),
						(steps[i-1][1] + targetPercent*(steps[i][1] - steps[i-1][1])),
						0, 0, -.15);
				
				Thread.sleep(3);
			}
			
			drive.correctedMecanumDrive((steps[i][0]), (steps[i][1]), 0, 0, -.15);
		}
	}
	
	private double tillTarget(long start, long current, int target){
		int timeProgressedFromStart = (int) (current - start);
		
		return timeProgressedFromStart/target; 
	}
	
	private void driveUntilUltrasonicCode() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		
		while (System.currentTimeMillis() < startTime + 3000){
			if (usSensor.getRangeInches() > usDistance + usAcceptableRange){
				drive.correctedMecanumDrive(usDirection.get(0), usDirection.get(1), 0, 0, 0);
			} else if (usSensor.getRangeInches() < usDistance - usAcceptableRange){
				drive.correctedMecanumDrive(-usDirection.get(0), -usDirection.get(1), 0, 0, 0);
			}
			if (usSensor.getRangeInches() < usDistance + usAcceptableRange &&
					usSensor.getRangeInches() > usDistance - usAcceptableRange){
				break;
			}
			
			Thread.sleep(3);
				
		}
	}
	
	private void driveUntilDistanceCode() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		
		drive.resetDistance();
		
		while (System.currentTimeMillis() < startTime + 10000){
			double averageDistance = 0;
			for (int i = 0; i < 4; i++){
				if (i != 2) averageDistance +=  Math.abs(drive.getWheelDistance(i));
				SmartDashboard.putNumber("Wheel 1 Dist", drive.getWheelDistance(1));
				
			}
			averageDistance /= 3;
			
			SmartDashboard.putNumber("Wheel distance", averageDistance);
			
			if (averageDistance > dsDistance + dsAcceptableRange){
				drive.correctedMecanumDrive(-dsDirection.get(0), -dsDirection.get(1), 0, 0, ftbCorrection);
			} else if (averageDistance < dsDistance - dsAcceptableRange){
				drive.correctedMecanumDrive(dsDirection.get(0), dsDirection.get(1), 0, 0, ftbCorrection);
			}
			if (averageDistance < dsDistance + dsAcceptableRange &&
					averageDistance > dsDistance - dsAcceptableRange){
				break;
			}
			
			Thread.sleep(3);
				
		}
		
	}
	
	public void rotateUntilDegreeCode() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		navX.zeroYaw();
		
		while (System.currentTimeMillis() < startTime + 5000){
			System.out.println("In loop : " + System.currentTimeMillis());
			if (navX.getYaw() > dgDegree + dgAcceptableRange){
				//drive.correctedMecanumDrive(0, 0, (navX.getYaw() - dgDegree * .0001), 0, ftbCorrection);
				drive.correctedMecanumDrive(0, 0, ((navX.getYaw() - dgDegree) * -.0038) - .15, 0, ftbCorrection);
			SmartDashboard.putNumber("Rotation Speed Positive", (navX.getYaw() - dgDegree) * .006);	
			} else if(navX.getYaw() < dgDegree - dgAcceptableRange){
				//drive.correctedMecanumDrive(0,0, (dgDegree - navX.getYaw() * -.0001), 0, ftbCorrection);
				drive.correctedMecanumDrive(0,0, ((dgDegree - navX.getYaw()) * .0038) + .15, 0, ftbCorrection);
				SmartDashboard.putNumber("Rotation Speed Positive", (dgDegree - navX.getYaw()) * -.006);
			}
			SmartDashboard.putNumber("Angle", navX.getYaw());
			
			if (navX.getYaw() < dgDegree + dgAcceptableRange && navX.getYaw() > dgDegree - dgAcceptableRange){
				break;
			}
			Thread.sleep(3);
		}
	}
	
	public void driveUntilTriggerCode() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		
		drive.resetDistance();
		
		while (System.currentTimeMillis() < startTime + 10000){
			
			if (autoEye.get() != eyeTrigger){
				drive.correctedMecanumDrive(0, 0, -dreDirection.get(0), 0, ftbCorrection);
			} else {
				break;
			}
			
			
			Thread.sleep(3);
				
		}
		
	}
	
	public void rotateUntilTriggerCode() throws InterruptedException{
		long startTime = System.currentTimeMillis();
		
		while (System.currentTimeMillis() < startTime + 5000){
			if (autoEye.get() != eyeTrigger){
				drive.correctedMecanumDrive(-deDirection.get(0), -deDirection.get(1), 0, 0, ftbCorrection);
			} else {
				break;
			}
			Thread.sleep(3);
		}
	}
	
	

	@Override
	public void run() {
		while (true){
			try {
				Thread.sleep(5);

				if (pathToExecute != null){
					drivePathCode(pathToExecute);
					pathToExecute = null;
					running = false;
				}
				
				if (usDistance > 0 && usSensor != null){
					driveUntilUltrasonicCode();
					usDistance = 0;
					usSensor = null;
					running = false;
				}
				
				if (dsDistance > 0){
					driveUntilDistanceCode();
					dsDistance = 0;
					dsDirection = null;
					running = false;
				}
				
				if (dgDegree > 0){
					rotateUntilDegreeCode();
					dgDegree = 0;
					running = false;
				}
				
				if (deDirection != null && autoEye != null){
					driveUntilTriggerCode();
					deDirection = null;
					running = false;
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/**The robot is not stopped after the path is executed!
	 * @param path 2-D array with the profile points. Arrays should hold {x speed, y speed, total time since profile start}
	 * The first elements time should be 0
	 */
	public void drivePath (int[][] path, boolean thread){
		running = true;
		pathToExecute = path;
		if (!thread){
			try {
				drivePathCode(pathToExecute);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**The robot is not stopped after this method concludes!
	 * @param s Valid Ultrasonic Sensor object
	 * @param distance Distance to operate until
	 * @param motion A Vector object with the x (0) and y (1) drive speed to use to get to the target distance.
	 * X-Positive = Right
	 * Y-Negative = Forward
	 * If the robot is farther away then the specified distance, the robot will use the power values specified. If it is 
	 * too close, it will use the negative of the specified value.
	 */
	public void driveUntilUltrasonic (Ultrasonic s, int distance, Vector<Double> motion, boolean thread){
		running = true;
		usDirection = motion;
		usDistance = distance;
		usSensor = s;
		if (!thread){
			try {
				driveUntilUltrasonicCode();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void driveUntilDistance (double distance, Vector<Double> motion, boolean thread){
		running = true;
		dsDistance = distance;
		dsDirection = motion;
		if (!thread){
			try {
				driveUntilDistanceCode();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void rotateUntilDegree(double degree, boolean thread){
		running = true;
		dgDegree = degree;
		if (!thread){
			try{
				rotateUntilDegreeCode();
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void driveUntilEyeTrigger(Vector<Double> motion, boolean trigger, boolean thread){
		running = true;
		deDirection = motion;
		eyeTrigger = trigger;
		if (!thread){
			try {
				driveUntilTriggerCode();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void rotateUntilEyeTrigger(Vector<Double> motion, boolean trigger, boolean thread){
		running = true;
		dreDirection = motion;
		eyeTrigger = trigger;
		if (!thread){
			try {
				rotateUntilTriggerCode();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setFtB(double d){
		ftbCorrection = d;
	}
}
