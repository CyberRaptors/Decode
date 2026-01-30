package lib8812.meepmeeptests;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.game.CommonPoses;

public class PoseVisualizer {
	static Pose2d targetPose = CommonPoses.BLUE_MONSTER_CLEAR_GATE_POSE;

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(new Pose2d(0, 0, 0));

		return drive.actionBuilder(drive.getPoseEstimate())
				.strafeToSplineHeading(targetPose.position, targetPose.heading)
				.stopAndAdd(new SleepAction(100))
				.strafeTo(new Vector2d(0, 0))
				.build();
	}
}
