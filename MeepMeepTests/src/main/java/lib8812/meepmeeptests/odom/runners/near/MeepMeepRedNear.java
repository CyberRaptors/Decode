package lib8812.meepmeeptests.odom.runners.near;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.stubs.CommonPoses;

public class MeepMeepRedNear {
	static ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();

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
				drive.actionBuilder(CommonPoses.RED_FIRST_SPIKE_START_POSE)
						.afterDisp(5, bot.setRailDriveTwoPower(1))
						.strafeToSplineHeading(
								CommonPoses.RED_FIRST_SPIKE_END_POSE.position,
								CommonPoses.RED_FIRST_SPIKE_END_POSE.heading,
								bot.SPIKE_PICKUP_VEL_CONSTRAINT
						)
						.build(),
				bot.setIntakeGroupPower(0),
				new SleepAction(0.5),
				bot.setRailDriveTwoPower(0)
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
				bot.setIntakeGroupPower(1),
				drive.actionBuilder(CommonPoses.RED_SECOND_SPIKE_START_POSE)
						.afterDisp(5, bot.setRailDriveTwoPower(1))
						.strafeToSplineHeading(
								CommonPoses.RED_SECOND_SPIKE_END_POSE.position,
								CommonPoses.RED_SECOND_SPIKE_END_POSE.heading,
								bot.SPIKE_PICKUP_VEL_CONSTRAINT
						)
						.build(),
				bot.setIntakeGroupPower(0),
				new SleepAction(0.5),
				bot.setRailDriveTwoPower(0)
		);

		Action thirdMoveToShoot = drive.actionBuilder(CommonPoses.RED_SECOND_SPIKE_END_POSE)
				.strafeToSplineHeading(
						CommonPoses.RED_NEAR_SHOT_POSE.position,
						CommonPoses.RED_NEAR_SHOT_POSE.heading
				)
				.build();

		Action park = drive.actionBuilder(CommonPoses.RED_NEAR_SHOT_POSE)
				.strafeTo(CommonPoses.RED_NEAR_PARK_POS)
				.build();

		Action main = new SequentialAction(
				initialMoveToShoot,
				bot.successiveShootWithVelo(2, bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				pickupFirstSpike,
				secondMoveToShoot,
				bot.successiveShootWithVelo(3, bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				pickupSecondSpike,
				thirdMoveToShoot,
				bot.successiveShootWithVelo(3, bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				park
		);

		return main;
	}
}
