package lib8812.meepmeeptests.odom.runners.far;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.stubs.game.CommonPoses;

public class MeepMeepRedFarMidShot {
	static ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();
	static Action main;

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(CommonPoses.INITIAL_RED_FAR_POSE);

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_RED_FAR_POSE)
				.strafeToSplineHeading(
						CommonPoses.RED_MID_SHOT_POSE.position,
						CommonPoses.RED_MID_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.RED_MID_SHOT_POSE)
						.strafeToSplineHeading(
								CommonPoses.RED_THIRD_SPIKE_START_POSE.position,
								CommonPoses.RED_THIRD_SPIKE_START_POSE.heading
						)
						.build(),
				bot.setIntakeGroupPower(1),
				drive.actionBuilder(CommonPoses.RED_THIRD_SPIKE_START_POSE)
						.strafeToSplineHeading(
								CommonPoses.RED_THIRD_SPIKE_END_POSE.position,
								CommonPoses.RED_THIRD_SPIKE_END_POSE.heading
						)
						.strafeToSplineHeading(
								CommonPoses.RED_THIRD_SPIKE_START_POSE.position,
								CommonPoses.RED_THIRD_SPIKE_START_POSE.heading
						)
						.build()
		);

		Action secondMoveToShoot = drive.actionBuilder(CommonPoses.RED_THIRD_SPIKE_START_POSE)
				.strafeToSplineHeading(
						CommonPoses.RED_MID_SHOT_POSE.position,
						CommonPoses.RED_MID_SHOT_POSE.heading
				)
				.build();

		Action park = drive.actionBuilder(CommonPoses.RED_MID_SHOT_POSE)
				.strafeTo(CommonPoses.RED_FAR_PARK_POS)
				.build();

		main = new SequentialAction(
				initialMoveToShoot,
				bot.shootWithVelo(bot.SHOOTER_VELO_FOR_MID_SHOT),
				bot.feedNext(1.5),
				bot.shootWithVelo(bot.SHOOTER_VELO_FOR_MID_SHOT),
				bot.disableShootersAsync(),
				pickupFirstSpike,
				bot.setRailDriveTwoPower(-1.0),
				bot.setIntakeGroupPower(0),
				secondMoveToShoot,
				new SleepAction(0.7),
				bot.successiveShootWithVelo(2, bot.SHOOTER_VELO_FOR_MID_SHOT),
				park
		);

		return main;
	}
}
