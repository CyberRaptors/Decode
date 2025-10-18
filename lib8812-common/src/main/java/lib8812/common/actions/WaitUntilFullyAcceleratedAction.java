package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitUntilFullyAcceleratedAction implements Action {
	final DcMotorEx motor;
	final double deltaThresh;
	double lastVelocity;
	ElapsedTime time;

	boolean initialized = false;


	public WaitUntilFullyAcceleratedAction(DcMotorEx motor, double threshPerSec) {
		this.motor = motor;
		deltaThresh = threshPerSec;
		lastVelocity = 0;
		time = new ElapsedTime();
	}

	public WaitUntilFullyAcceleratedAction(DcMotorEx motor) {
		this(motor, 10);
	}

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (!initialized) {
			initialized = true;
			lastVelocity = motor.getVelocity();
			time.reset();
			return true;
		}

		double deltaVelo = motor.getVelocity() - lastVelocity;
		double deltaT = time.seconds();

		lastVelocity = motor.getVelocity();
		time.reset();

		return Math.abs(deltaVelo/deltaT) >= deltaThresh; // still exceeding thresh, we are still accelerating
	}
}
