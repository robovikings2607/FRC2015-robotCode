package org.usfirst.frc.team2607.robot;

import java.io.File;
import java.io.PrintWriter;

import edu.wpi.first.wpilibj.DriverStation;

public class Logger extends Thread {
	private PrintWriter logFile = null;
	private boolean loggingEnabled = false;
	String deviceName;
	
	Robot theBot;
	
	
	@Override
	public void run() {
		while (true){
			logEntry();
		}
		
	}
	
	public Logger(Robot robot){
		theBot = robot;
		
		
		
	}
	
	 public void enableLogging(boolean enable) {
	    	if (enable && !loggingEnabled) {
	    		if (logFile != null) {
	    			logFile.close();
	    			logFile = null;
	    		}
	    		try {
	    			String s = "/home/lvuser/" + "MatchFiles" + "." + System.currentTimeMillis() + ".csv";
	    			logFile = new PrintWriter(new File(s));
	    			logFile.println("matchTime,buttonsPilot,buttonsSubPilot,"
	    					+ "FrontLeftSetPoint, FrontLeftActual,"
	    					+ "FrontRightSetPoint, FrontRightActual,"
	    					+ "BackLeftSetPoint, BackLeftActual, "
	    					+ "BackRightSetPoint, BackRightActual,"
	    					+ "ElevatorSetPoint, ElevatorActual");
	    		} catch (Exception e) {}
	    	} 
	    	
	    	if (!enable && loggingEnabled) {
	    		if (logFile != null) {
	    			logFile.close();
	    			logFile = null;
	    		}
	    	}
	    	
	    	loggingEnabled = enable;
	    }
	 
	 public void logEntry() {
	        if (loggingEnabled) {
	        	logFile.printf("%d,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f,%.4f\n", DriverStation.getInstance().getMatchTime(), DriverStation.getInstance().getStickButtons(0), DriverStation.getInstance().getStickButtons(1), 
    					theBot.FrontL.getPID().getSetpoint(), theBot.FrontL.getPID().get(),
    					theBot.FrontR.getPID().getSetpoint(), theBot.FrontR.getPID().get(),
    					theBot.BackL.getPID().getSetpoint(), theBot.BackL.getPID().get(),
    					theBot.BackR.getPID().getSetpoint(), theBot.BackR.getPID().get(),
    					theBot.motaVator.pid.getSetpoint(), theBot.motaVator.pid.get());
	        }
	    }

}
