package lib8812.common.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RRActionsDelegator {
	final FtcDashboard dash = FtcDashboard.getInstance();
	List<Action> actions = new ArrayList<>();

	public void scheduleAll(Action... actionsList) {
		for (Action action : actionsList) {
			if (action == null) continue;

			actions.add(action);
		}
	}

	public void clear() { actions.clear(); }

	public void execute() {
		TelemetryPacket packet = new TelemetryPacket();

		actions = actions
				.stream()
				.filter(action -> action.run(packet))
				.collect(Collectors.toList());


		dash.sendTelemetryPacket(packet);
	}

	public int count() { return actions.size(); }
}
