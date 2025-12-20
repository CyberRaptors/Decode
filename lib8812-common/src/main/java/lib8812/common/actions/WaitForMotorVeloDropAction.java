package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class WaitForMotorVeloDropAction implements Action {
	final DcMotorEx motor;
	final double maxVelo;

	boolean initialized = false;

	public WaitForMotorVeloDropAction(DcMotorEx motor, double fromVelo) {
		this.motor = motor;
		this.maxVelo = fromVelo;
	}

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		return motor.getVelocity() >= maxVelo; // if >= maxVelo, run again, else this action is complete (velocity has dropped)
	}
}
