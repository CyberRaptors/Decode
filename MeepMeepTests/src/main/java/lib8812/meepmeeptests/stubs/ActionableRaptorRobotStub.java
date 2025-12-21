package lib8812.meepmeeptests.stubs;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.NullAction;
import com.acmerobotics.roadrunner.Pose2d;

public class ActionableRaptorRobotStub {
	public final double SHOOTER_VELO_FOR_CLOSE_SHOT = 1440;

	public Action setIntakeAndTransferPower(double velo) {
		return new NullAction();
	}

	public Action shootThree(double velo) {
		return new NullAction();
	}

	public Action limelightAlignToGoal() {
		return new NullAction();
	}

	public Action requireLimelightRelocalization(Action action, int maxTries) {
		return new NullAction();
	}

	public Action teleOpUnlocalizedStrafeTo(Pose2d pose) {
		return new NullAction();
	}

	public Action strafeToBase() {
		return new NullAction();
	}

	public Action localizationEnabledAlignToGoal() {
		return new NullAction();
	}
}
