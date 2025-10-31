package lib8812.meepmeeptests.odom.runners.far;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.stubs.CommonPoses;

public class MeepMeepBlueFar {
	static ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();

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
				bot.setIntakeGroupPower(1),
				drive.actionBuilder(CommonPoses.BLUE_THIRD_SPIKE_START_POSE)
						.afterDisp(5, bot.setRailDriveTwoPower(1))
						.strafeToSplineHeading(
								CommonPoses.BLUE_THIRD_SPIKE_END_POSE.position,
								CommonPoses.BLUE_THIRD_SPIKE_END_POSE.heading,
								bot.SPIKE_PICKUP_VEL_CONSTRAINT
						)
						.build(),
				bot.setIntakeGroupPower(0),
				new SleepAction(0.5),
				bot.setRailDriveTwoPower(0)
		);

		Action secondMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_THIRD_SPIKE_END_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE.position,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupSecondSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE
						)
						.strafeToSplineHeading(
								CommonPoses.BLUE_SECOND_SPIKE_START_POSE.position,
								CommonPoses.BLUE_SECOND_SPIKE_START_POSE.heading
						)
						.build(),
				bot.setIntakeGroupPower(1),
				drive.actionBuilder(CommonPoses.BLUE_SECOND_SPIKE_START_POSE)
						.afterDisp(5, bot.setRailDriveTwoPower(1))
						.strafeToSplineHeading(
								CommonPoses.BLUE_SECOND_SPIKE_END_POSE.position,
								CommonPoses.BLUE_SECOND_SPIKE_END_POSE.heading,
								bot.SPIKE_PICKUP_VEL_CONSTRAINT
						)
						.build(),
				bot.setIntakeGroupPower(0),
				new SleepAction(0.5),
				bot.setRailDriveTwoPower(0)
		);

		Action thirdMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_SECOND_SPIKE_END_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE.position,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action park = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.strafeTo(CommonPoses.BLUE_FAR_PARK_POS)
				.build();

		Action main = new SequentialAction(
				initialMoveToShoot,
				bot.successiveShootWithVelo(2, bot.SHOOTER_VELO_FOR_FAR_SHOT),
				pickupFirstSpike,
				secondMoveToShoot,
				bot.successiveShootWithVelo(3, bot.SHOOTER_VELO_FOR_FAR_SHOT),
				pickupSecondSpike,
				thirdMoveToShoot,
				bot.successiveShootWithVelo(3, bot.SHOOTER_VELO_FOR_FAR_SHOT),
				park
		);

		return main;
	}
}
