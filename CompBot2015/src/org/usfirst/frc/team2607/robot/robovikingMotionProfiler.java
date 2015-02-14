package org.usfirst.frc.team2607.robot;

public class robovikingMotionProfiler implements Runnable{
	
	robovikingMecanumDrive drive;
	
	boolean doingStuff;
	int[][] pathToExecute = null;
	
	public robovikingMotionProfiler(robovikingMecanumDrive someDrive){
		drive = someDrive;
		
		new Thread(this).start();
	}
	
	private void drivePath(int[][] steps) throws InterruptedException{
		
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

	@Override
	public void run() {
		while (true){
			try {
				Thread.sleep(5);

				if (doingStuff){
					drivePath(pathToExecute);
					doingStuff = false;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void executePath (int[][] path){
		
		pathToExecute = path;
		doingStuff = true;
	}

}
