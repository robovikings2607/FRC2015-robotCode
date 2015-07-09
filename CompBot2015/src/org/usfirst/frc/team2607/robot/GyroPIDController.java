package org.usfirst.frc.team2607.robot;

import com.kauailabs.nav6.frc.IMUAdvanced;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

public class GyroPIDController extends PIDController {

	public GyroPIDController(double Kp, double Ki, double Kd, IMUAdvanced x) {
		super(Kp, Ki, Kd, new GyroSource(x), new DummyOut());
	}

}
