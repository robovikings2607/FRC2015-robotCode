package org.usfirst.frc.team2607.robot;


import java.io.File;
import java.io.PrintWriter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author rossron
 */

public class WheelRPMController implements SpeedController {
    private PIDController pidLoop;
    private SmoothedEncoder enc;
    private CANTalon motor;
    private double curMaxSpeed;
    private int wheelIndex;
    private String deviceName;
    private boolean forceOff = false;
    public static boolean off;
    private short errorCount = 0;
    private boolean encodersFlag = false;
    private PrintWriter logFile = null;
    private boolean loggingEnabled = false;
    
    public WheelRPMController(String name, int index, boolean useEncoders) {
//        motors = new TalonPair(pwmMecanumAddresses[index][0], 
//                               pwmMecanumAddresses[index][1]);
    	encodersFlag = useEncoders;
        motor = new CANTalon(Constants.talonCANAddresses[index], 0);  // 2nd parameter = 0 when 
                                                                   // using the Y-splitter
        if (encodersFlag){
        enc = new SmoothedEncoder(Constants.encoders[index][0], Constants.encoders[index][1],
                                  true, Encoder.EncodingType.k1X);
        pidLoop = new PIDController(Constants.talonLowGearPIDGains[index][0],
        							Constants.talonLowGearPIDGains[index][1],
        							Constants.talonLowGearPIDGains[index][2],
                                    enc, motor);
        curMaxSpeed = Constants.talonLowGearMaxSpeed;
        pidLoop.setInputRange(-curMaxSpeed, curMaxSpeed);
        }
        wheelIndex = index;
        deviceName = name;
        
        
    }

    public void enableLogging(boolean enable) {
    	if (enable && !loggingEnabled) {
    		if (logFile != null) {
    			logFile.close();
    			logFile = null;
    		}
    		try {
    			String s = "/home/lvuser/" + deviceName + "." + System.currentTimeMillis() + ".csv";
    			logFile = new PrintWriter(new File(s));
    			logFile.printf("Kp %.8f Ki %.8f Kd %.8f ff %.8f\n", pidLoop.getP(), pidLoop.getI(), pidLoop.getD(), pidLoop.getF());
    			logFile.println("Time,SP,PV,Err,MV");
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
    
    public double getRate() {
    	if (!encodersFlag) return 0.0;
    	return enc.getCurrentRate();
    }
    
    public void displayWheelRPM() {
    	if (!encodersFlag) return;
        System.out.print(deviceName + ": ");
        System.out.print(enc.getCurrentRate() + " ");
    }
    
    public void setGearPID(boolean highGear) {
    	if (!encodersFlag){
    		return;
    	}
        if (highGear) {
            
            pidLoop.setPID(Constants.talonHighGearPIDGains[wheelIndex][0],
            			Constants.talonHighGearPIDGains[wheelIndex][1],
            			Constants.talonHighGearPIDGains[wheelIndex][2]);
            curMaxSpeed = Constants.talonHighGearMaxSpeed;
            pidLoop.setInputRange(-curMaxSpeed, curMaxSpeed);                       
        } else {
            pidLoop.setPID(Constants.talonLowGearPIDGains[wheelIndex][0],
            			Constants.talonLowGearPIDGains[wheelIndex][1],
            			Constants.talonLowGearPIDGains[wheelIndex][2]);
            curMaxSpeed = Constants.talonLowGearMaxSpeed;
            pidLoop.setInputRange(-curMaxSpeed, curMaxSpeed);                                   
        }
    }
    
    public double get() {
    	if (!encodersFlag){
    		return motor.get();
    	}
        return pidLoop.getSetpoint();
    }

    public void set(double d, byte b) {
        set(d);
    }
    
    public int getError()
    {
        if (errorCount>10)
        {
            errorCount = 10;
            return (1<<wheelIndex);
        }
        return 0;
    }
    
    public void set(double d) {
    	if (!encodersFlag){
    		motor.set(d);
    		return;
    	}
        if (d == 0&&off)
        {
            pidLoop.reset();
            forceOff = true;
        }
        else
        {
            if (forceOff)
            {
                forceOff = false;
                pidLoop.enable();
            }
            pidLoop.setSetpoint(d * curMaxSpeed);
            if (Math.abs(d*curMaxSpeed)>0&&enc.getCurrentRate()==0)
            {
                errorCount++;
            }
            else
            {
                errorCount = 0;
            }
        }
        if (loggingEnabled) {
        	logFile.println(System.currentTimeMillis() + "," + pidLoop.getSetpoint() + "," +
        			        enc.getCurrentRate() + "," + pidLoop.getError() + "," + pidLoop.get());
        }
    }

    public void disable() {
    	if (!encodersFlag) return;
        enc.reset();
        pidLoop.disable();
    }

    public void enable() {
    	if (!encodersFlag) return;
        enc.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
        pidLoop.enable();
    }
    
    public void pidWrite(double d) {
        
    } 
}