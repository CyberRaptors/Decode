package lib8812.meepmeeptests.odom.runners.far;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.stubs.game.CommonPoses;

public class MeepMeepBlueFarTame {
	static ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();
	static Action main;

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(CommonPoses.INITIAL_BLUE_FAR_POSE);

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_BLUE_FAR_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE.position,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
						.strafeToSplineHeading(
								CommonPoses.BLUE_THIRD_SPIKE_START_POSE.position,
								CommonPoses.BLUE_THIRD_SPIKE_START_POSE.heading
						)
						.build(),
				bot.setIntakeAndTransferPower(1),
				drive.actionBuilder(CommonPoses.BLUE_THIRD_SPIKE_START_POSE)
						.strafeToSplineHeading(
								CommonPoses.BLUE_THIRD_SPIKE_END_POSE.position,
								CommonPoses.BLUE_THIRD_SPIKE_END_POSE.heading
						)
						.build()
		);

		Action secondMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_THIRD_SPIKE_END_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE.position,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action park = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.strafeTo(CommonPoses.BLUE_FAR_PARK_POS)
				.build();

		main = new SequentialAction(
				initialMoveToShoot,
				pickupFirstSpike,
				secondMoveToShoot,
				park
		);

		return main;
	}
}
