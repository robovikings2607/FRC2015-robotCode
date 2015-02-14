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
	boolean pidDisabled = false;
	
	public elevator(){
		
		enc = new Encoder(Constants.encoderElevatorChannelA,  Constants.encoderElevatorChannelB, true, EncodingType.k1X);
		elevatorTalon1 = new CANTalon(Constants.talonElevator1);
		elevatorTalon2 = new CANTalon(Constants.talonElevator2);
		topSwitch = new DigitalInput(Constants.topSwitchPort);
		bottomSwitch = new DigitalInput(Constants.bottomSwitchPort);
		arms = new Solenoid(1, Constants.armsChannel);
		breaks = new Solenoid(1, Constants.breaksChannel);
		shifter = new Solenoid(1, Constants.winchChannel);
		shifter.set(false);
		elevatorTalon2.changeControlMode(ControlMode.Follower);
		elevatorTalon2.set(Constants.talonElevator1);
		elevatorTalon1.enableBrakeMode(true);
		elevatorTalon2.enableBrakeMode(true);
		enc.setPIDSourceParameter(PIDSourceParameter.kDistance);
    	enc.setDistancePerPulse(Constants.distancePerPulse);
    	enc.reset();
    	pid = new PIDController(0.079, 0.0004, 0.0006, enc, elevatorTalon1);
    	pid.setOutputRange(-.6, .2);
    	pid.setInputRange(-50, 0);
    	disablePID();
	}
	
	public void goToCarryingPos() {
		pid.setSetpoint(-3.0);
		enablePID();
	}
	
	
	public void disablePID() {
		if (!pidDisabled) {
			pid.disable();
			pidDisabled = true;
		}
	}
	
	public void enablePID() {
		if (pidDisabled) {
			pid.enable();
			pidDisabled = false;
		}
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
		enablePID();
	}
	
	public void lower(){
		pid.setSetpoint(getHeight() + 12.1);
		enablePID();
	}
	
	public void raiseManual(){
		disablePID();
		elevatorTalon1.set(raiseSpeed);
	}
	
	public void lowerManual(){
		disablePID();
		elevatorTalon1.set(lowerSpeed);
	}
	
	public void equilibrium(){
		disablePID();
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
	


