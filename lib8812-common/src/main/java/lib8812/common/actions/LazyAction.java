package lib8812.common.actions;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import lib8812.common.util.ActionCreator;

public class LazyAction implements Action {
	final ActionCreator lazyAction;
	Action innerAction;

	public LazyAction(ActionCreator lazyAction) {
		this.lazyAction = lazyAction;
	}

	@Override
	public boolean run(@NonNull TelemetryPacket telemetryPacket) {
		if (innerAction == null) {
			innerAction = lazyAction.run();

			if (innerAction == null) return false; // just end the action, nothing was created
		}

		return innerAction.run(telemetryPacket);
	}
}
