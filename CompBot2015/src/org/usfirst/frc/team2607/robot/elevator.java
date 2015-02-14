package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.ControlMode;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.Solenoid;

public class elevator implements Runnable {
	
	CANTalon elevatorTalon1, elevatorTalon2;
	DigitalInput topSwitch, bottomSwitch;
	Encoder enc;
	Solenoid arms, breaks, shifter;
	PIDController pid;
	double raiseSpeed = -.4;
	double lowerSpeed = .4;
	boolean armsFlag = false;
	boolean override = false;
	
	public elevator(){
		shifter.set(false);
		enc = new Encoder(Constants.encoderElevatorChannelA,  Constants.encoderBackRightChannelB, true, EncodingType.k1X);
		elevatorTalon1 = new CANTalon(Constants.talonElevator1);
		elevatorTalon2 = new CANTalon(Constants.talonElevator2);
		topSwitch = new DigitalInput(Constants.topSwitchPort);
		bottomSwitch = new DigitalInput(Constants.bottomSwitchPort);
		arms = new Solenoid(Constants.armsChannel);
		breaks = new Solenoid(Constants.breaksChannel);
		shifter = new Solenoid(Constants.winchChannel);
		pid = new PIDController(0.079, 0.0004, 0.0006, enc, elevatorTalon1);
		elevatorTalon2.changeControlMode(ControlMode.Follower);
		elevatorTalon2.set(Constants.talonElevator1);
		elevatorTalon1.enableBrakeMode(true);
		elevatorTalon2.enableBrakeMode(true);
		enc.setPIDSourceParameter(PIDSourceParameter.kDistance);
    	enc.setDistancePerPulse(Constants.distancePerPulse);
    	pid.setOutputRange(-.8, .2);
    	pid.setInputRange(-50, 0);
    	enc.reset();
    	
    	
	}
	
	
	public double getHeight(){
		return enc.getDistance();
	}
	
	public double getToteHeight(){
		return (getHeight()/12.1);
	}
	
	public void resetEncoder(){
		enc.reset();
	}
	
	public void raise(){
		pid.setSetpoint(getHeight() - 12.1);
		pid.enable();
	}
	
	public void lower(){
		pid.setSetpoint(getHeight() + 12.1);
		pid.enable();
	}
	
	public void raiseManual(){
		pid.disable();
		elevatorTalon1.set(raiseSpeed);
	}
	
	public void lowerManual(){
		pid.disable();
		elevatorTalon1.set(lowerSpeed);
	}
	
	public void equilibrium(){
		pid.disable();
		elevatorTalon1.set(0.0);
	}
	
	public void grab(){
		armsFlag = !armsFlag;
		arms.set(armsFlag);
	}
	
	public void toggleOverride(){
		override = !override;
	}
	
	public boolean getOverride(){
		return override;
	}


	@Override
	public void run() {
		while(true){
			if (!getOverride()){
		if (!bottomSwitch.get() && (elevatorTalon1.get() < 0)){
			elevatorTalon1.set(0.0);	
		}
		if (!topSwitch.get() && (elevatorTalon1.get() > 0)){
			elevatorTalon1.set(0.0);	
		}
			}
	try {
		Thread.sleep(10);
	} catch (InterruptedException e) {
	}
		}
	}
		
		
}
	


