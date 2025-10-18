package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitUntilMotorVelocityChangedAction implements Action {
	final DcMotorEx motor;
	final double deltaThresh;
	double lastVelocity;
	final ElapsedTime time;

	boolean initialized = false;


	public WaitUntilMotorVelocityChangedAction(DcMotorEx motor, double threshPerSec) {
		this.motor = motor;
		deltaThresh = threshPerSec;
		lastVelocity = 0;
		time = new ElapsedTime();
	}

	public WaitUntilMotorVelocityChangedAction(DcMotorEx motor) {
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

		return Math.abs(deltaVelo / deltaT) < deltaThresh; // if less than thresh, we assume the velo has not changed, so return true to run again, else we believe the velo has changed so we return false
	}
}