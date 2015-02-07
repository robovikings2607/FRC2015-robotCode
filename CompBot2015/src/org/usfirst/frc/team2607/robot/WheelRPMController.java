package org.usfirst.frc.team2607.robot;


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
    public WheelRPMController(String name, int index, boolean useEncoders) {
//        motors = new TalonPair(pwmMecanumAddresses[index][0], 
//                               pwmMecanumAddresses[index][1]);
    	encodersFlag = useEncoders;
        motor = new CANTalon(Constants.talonCANAddresses[index], 0);  // 2nd parameter = 0 when 
                                                                   // using the Y-splitter
        if (encodersFlag){
        enc = new SmoothedEncoder(Constants.encoders[index][0], Constants.encoders[index][1],
                                  true, Encoder.EncodingType.k1X);
        pidLoop = new PIDController(Constants.talonHighGearPIDGains[index][0],
        							Constants.talonHighGearPIDGains[index][1],
        							Constants.talonHighGearPIDGains[index][2],
                                    enc, motor);
        curMaxSpeed = Constants.talonHighGearMaxSpeed;
        pidLoop.setInputRange(-curMaxSpeed, curMaxSpeed);
        }
        wheelIndex = index;
        deviceName = name;
        
        
    }

    public void displayWheelRPM() {
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
    }

    public void disable() {
        enc.reset();
        pidLoop.disable();
    }

    public void enable() {
        enc.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
        pidLoop.enable();
    }
    
    public void pidWrite(double d) {
        
    } 
}