package lib8812.meepmeeptests.odom.runners.near;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.stubs.game.CommonPoses;

public class MeepMeepRedNear {
	static ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();
	static Action main;

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(CommonPoses.INITIAL_RED_NEAR_POSE);

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_RED_NEAR_POSE)
				.strafeToSplineHeading(
						CommonPoses.RED_NEAR_SHOT_POSE.position,
						CommonPoses.RED_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.RED_NEAR_SHOT_POSE)
						.strafeToSplineHeading(
								CommonPoses.RED_FIRST_SPIKE_START_POSE.position,
								CommonPoses.RED_FIRST_SPIKE_START_POSE.heading
						)
						.build(),
				bot.setIntakeGroupPower(1),
				new ParallelAction(
						new SequentialAction(
								new SleepAction(3),
								bot.setRailDriveTwoPower(-0.5)
						),
						drive.actionBuilder(CommonPoses.RED_FIRST_SPIKE_START_POSE)
								.strafeToSplineHeading(
										CommonPoses.RED_FIRST_SPIKE_END_POSE.position,
										CommonPoses.RED_FIRST_SPIKE_END_POSE.heading,
										bot.SPIKE_PICKUP_VEL_CONSTRAINT
								)
								.build()
				)
		);

		Action secondMoveToShoot = drive.actionBuilder(CommonPoses.RED_FIRST_SPIKE_END_POSE)
				.strafeToSplineHeading(
						CommonPoses.RED_NEAR_SHOT_POSE.position,
						CommonPoses.RED_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupSecondSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.RED_NEAR_SHOT_POSE)
						.strafeToSplineHeading(
								CommonPoses.RED_SECOND_SPIKE_START_POSE.position,
								CommonPoses.RED_SECOND_SPIKE_START_POSE.heading
						)
						.build(),
				new ParallelAction(
						drive.actionBuilder(CommonPoses.RED_SECOND_SPIKE_START_POSE)
								.strafeToSplineHeading(
										CommonPoses.RED_SECOND_SPIKE_END_POSE.position,
										CommonPoses.RED_SECOND_SPIKE_END_POSE.heading,
										bot.SPIKE_PICKUP_VEL_CONSTRAINT
								)
								.build()
				)
		);

		Action thirdMoveToShootAndPark = drive.actionBuilder(CommonPoses.RED_SECOND_SPIKE_END_POSE)
				.strafeToSplineHeading(
						CommonPoses.RED_NEAR_PARK_POSE.position,
						CommonPoses.RED_NEAR_PARK_POSE.heading
				)
				.build();

		main = new SequentialAction(
				initialMoveToShoot,
				bot.shootWithVelo(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.feedNext(1.5),
				bot.shootWithVelo(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.disableShootersAsync(),
				pickupFirstSpike,
				bot.setRailDriveTwoPower(-1.0),
				secondMoveToShoot,
				new SleepAction(1.7),
				bot.successiveShootWithVelo(2, bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				pickupSecondSpike,
				bot.setRailDriveTwoPower(-1.0),
				thirdMoveToShootAndPark,
				bot.shootWithVelo(bot.SHOOTER_VELO_FOR_CLOSE_PARALLEL_SHOT)
		);

		return main;
	}
}
