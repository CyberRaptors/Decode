package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

public class OnceAction implements Action {
	public interface Predicate
	{
		boolean run();
	}

	int tries = 0;
	final int maxTries;
	final Predicate predicate;
	boolean started = false;
	final Action action;

	public OnceAction(Predicate predicate, Action then, int maxTries) {
		this.predicate = predicate;
		action = then;
		this.maxTries = maxTries;
	}

	public OnceAction(Predicate predicate, Action then) {
		this(predicate, then,-1);
	}


	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (started) {
			return action.run(telemetryPacket);
		}
		else if (predicate.run() || tries == maxTries) {
			started = true;
			return run(telemetryPacket);
		}

		tries+=1;

		return true;
	}
}
