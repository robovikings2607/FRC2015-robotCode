package org.usfirst.frc.team2607.robot;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.can.CANNotInitializedException;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tInstances;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;

public class robovikingMecanumDrive extends RobotDrive {
    static final int kFrontLeft_val = 0;
    static final int kFrontRight_val = 1;
    static final int kRearLeft_val = 2;
    static final int kRearRight_val = 3;

	public robovikingMecanumDrive(SpeedController frontLeftMotor,
			SpeedController rearLeftMotor, SpeedController frontRightMotor,
			SpeedController rearRightMotor) {
		super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
		// TODO Auto-generated constructor stub
	}

	public void correctedMecanumDrive(double x, double y, double rotation, double gyroAngle, double frontBackRatio){
		if(!kMecanumCartesian_Reported) {
            UsageReporting.report(tResourceType.kResourceType_RobotDrive, getNumMotors(), tInstances.kRobotDrive_MecanumCartesian);
            kMecanumCartesian_Reported = true;
        }
        double xIn = x;
        double yIn = y;
        // Negate y for the joystick.
        yIn = -yIn;
        // Compensate for gyro angle.
        double rotated[] = rotateVector(xIn, yIn, gyroAngle);
        xIn = rotated[0];
        yIn = rotated[1];

        double wheelSpeeds[] = new double[kMaxNumberOfMotors];
        wheelSpeeds[kFrontLeft_val] = xIn + yIn + rotation;
        wheelSpeeds[kFrontRight_val] = -xIn + yIn - rotation;
        wheelSpeeds[kRearLeft_val] = -xIn + yIn + rotation;
        wheelSpeeds[kRearRight_val] = xIn + yIn - rotation;

        normalize(wheelSpeeds);

        byte syncGroup = (byte)0x80;
        
        if(frontBackRatio > 0){
	        m_frontLeftMotor.set((1-frontBackRatio) * wheelSpeeds[kFrontLeft_val] * m_invertedMotors[kFrontLeft_val] * m_maxOutput, syncGroup);
	        m_frontRightMotor.set((1-frontBackRatio)* wheelSpeeds[kFrontRight_val] * m_invertedMotors[kFrontRight_val] * m_maxOutput, syncGroup);
	        m_rearLeftMotor.set(wheelSpeeds[kRearLeft_val] * m_invertedMotors[kRearLeft_val] * m_maxOutput, syncGroup);
	        m_rearRightMotor.set(wheelSpeeds[kRearRight_val] * m_invertedMotors[kRearRight_val] * m_maxOutput, syncGroup);
        } else {
        	m_frontLeftMotor.set(wheelSpeeds[kFrontLeft_val] * m_invertedMotors[kFrontLeft_val] * m_maxOutput, syncGroup);
            m_frontRightMotor.set(wheelSpeeds[kFrontRight_val] * m_invertedMotors[kFrontRight_val] * m_maxOutput, syncGroup);
            m_rearLeftMotor.set((1-Math.abs(frontBackRatio))* wheelSpeeds[kRearLeft_val] * m_invertedMotors[kRearLeft_val] * m_maxOutput, syncGroup);
            m_rearRightMotor.set((1-Math.abs(frontBackRatio)) * wheelSpeeds[kRearRight_val] * m_invertedMotors[kRearRight_val] * m_maxOutput, syncGroup);

        }
        
        if (m_safetyHelper != null) m_safetyHelper.feed();
	}
	
	public void resetDistance(){
		if (m_frontLeftMotor instanceof WheelRPMController){
			((WheelRPMController) m_frontLeftMotor).resetDistance();
			((WheelRPMController) m_frontRightMotor).resetDistance();
			((WheelRPMController) m_rearLeftMotor).resetDistance();
			((WheelRPMController) m_rearRightMotor).resetDistance();
		}
	}
	
	public double getWheelDistance(int i){
		if (m_frontLeftMotor instanceof WheelRPMController){
			switch (i){
			case 0:
				return ((WheelRPMController) m_frontLeftMotor).getDistance();
			case 1:
				return ((WheelRPMController) m_frontRightMotor).getDistance();
			case 2:
				return ((WheelRPMController) m_rearLeftMotor).getDistance();
			case 3: 
				return ((WheelRPMController) m_rearRightMotor).getDistance();
			default:
				return 0.0;
			}
		}
		return 0.0;
	}
	
	

}
