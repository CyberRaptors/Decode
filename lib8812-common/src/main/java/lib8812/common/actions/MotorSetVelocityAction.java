package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class MotorSetVelocityAction implements Action {
	final DcMotorEx motor;
	final double targetVelocity;
	final double deltaThresh;

	boolean initialized = false;

	public MotorSetVelocityAction(DcMotorEx motor, double velo, double deltaThresh) {
		this.motor = motor;
		this.deltaThresh = deltaThresh;
		this.targetVelocity = velo;
	}

	public MotorSetVelocityAction(DcMotorEx motor, double velo) {
		this(motor, velo, 30);
	}

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (!initialized) {
			motor.setVelocity(targetVelocity);
			initialized = true;
		}

		return Math.abs(motor.getVelocity() - targetVelocity) > deltaThresh; // if delta velo is greater than velo thresh, then we need to run again
	}
}
