package org.usfirst.frc.team2607.robot;

import java.util.Vector;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Ultrasonic;

public class robovikingMotionProfiler implements Runnable{
	
	robovikingMecanumDrive drive;
	
	boolean running = false;
	
	int[][] pathToExecute = null;
	
	Vector<Double> usDirection = null;
	int usDistance = 0;
	Ultrasonic usSensor = null;
	int usAcceptableRange = 5;
	
	Encoder[] dsEncoders = null;
	int dsDistance = 0;
	Vector<Double> dsDirection = null;
	int dsAcceptableRange = 5;
	
	public robovikingMotionProfiler(robovikingMecanumDrive someDrive){
		drive = someDrive;
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
		
		while (System.currentTimeMillis() < startTime + 3000){
			int averageDistance = 0;
			for (int i = 0; i < 4; i++){
				averageDistance +=  Math.abs(drive.getWheelDistance(i));
			}
			averageDistance /= 4;
			
			if (averageDistance > dsDistance + dsAcceptableRange){
				drive.correctedMecanumDrive(-dsDirection.get(0), -dsDirection.get(1), 0, 0, 0);
			} else if (averageDistance < dsDistance - dsAcceptableRange){
				drive.correctedMecanumDrive(dsDirection.get(0), dsDirection.get(1), 0, 0, 0);
			}
			if (averageDistance < dsDistance + dsAcceptableRange &&
					averageDistance > dsDistance - dsAcceptableRange){
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
				
				if (dsDistance > 0 && dsEncoders != null){
					driveUntilDistanceCode();
					dsEncoders = null;
					dsDistance = 0;
					dsDirection = null;
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
	 * @param motion A Vector object with the x (0) and y (1) drive speed to use to get to the target distance. If
	 * the robot is farther away then the specified distance, the robot will use the power values specified. If it is 
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
	
	public void driveUntilDistance (Encoder[] es, int distance, Vector<Double> motion, boolean thread){
		running = true;
		dsEncoders = es;
		dsDistance = distance;
		if (!thread){
			try {
				driveUntilDistanceCode();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
