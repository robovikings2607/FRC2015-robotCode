package org.usfirst.frc.team2607.robot;

import com.kauailabs.nav6.frc.IMUAdvanced;

import edu.wpi.first.wpilibj.PIDSource;

public class GyroSource implements PIDSource {
	IMUAdvanced y;
	
	GyroSource(IMUAdvanced x){
		y = x;
	}

	@Override
	public double pidGet() {
		System.out.println(y.getYaw());
		return y.getYaw();
	}

}
