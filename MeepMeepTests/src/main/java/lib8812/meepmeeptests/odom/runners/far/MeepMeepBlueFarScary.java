package lib8812.meepmeeptests.odom.runners.far;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.stubs.game.CommonPoses;

public class MeepMeepBlueFarScary {
	static ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();
	static Action main;

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(CommonPoses.INITIAL_BLUE_FAR_POSE);

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_BLUE_FAR_POSE)
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.strafeToSplineHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE.position,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpikeAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.BLUE_SECOND_SPIKE_START_POSE,
						CommonPoses.BLUE_SECOND_SPIKE_START_POSE.heading
				)
				.splineToSplineHeading(
						CommonPoses.BLUE_SECOND_SPIKE_END_POSE,
						CommonPoses.BLUE_SECOND_SPIKE_END_POSE.heading,
						bot.SPIKE_PICKUP_VEL_CONSTRAINT
				)
				.afterTime(0, bot.setIntakeAndTransferPower(0))
				.strafeToLinearHeading(
						CommonPoses.BLUE_CLEAR_GATE_FROM_FAR_START_POSE.position,
						CommonPoses.BLUE_CLEAR_GATE_FROM_FAR_START_POSE.heading
				)
				.strafeToLinearHeading(
						CommonPoses.BLUE_CLEAR_GATE_FROM_FAR_END_POSE.position,
						CommonPoses.BLUE_CLEAR_GATE_FROM_FAR_END_POSE.heading
				)
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT))

				// backup before returning to shot pos to avoid hitting the third spike
				.strafeToConstantHeading(
						CommonPoses.BLUE_SECOND_SPIKE_START_POSE.position
				)
				.strafeToLinearHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE.position,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupSecondSpikeAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.BLUE_THIRD_SPIKE_START_POSE,
						CommonPoses.BLUE_THIRD_SPIKE_START_POSE.heading
				)
				.splineToSplineHeading(
						CommonPoses.BLUE_THIRD_SPIKE_END_POSE,
						CommonPoses.BLUE_THIRD_SPIKE_END_POSE.heading,
						bot.SPIKE_PICKUP_VEL_CONSTRAINT
				)
				.afterTime(0, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.splineToLinearHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action park = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_FAR_PARK_POSE.position,
						CommonPoses.BLUE_FAR_PARK_POSE.heading
				)
				.build();
		
		main = new SequentialAction(
				initialMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				pickupFirstSpikeAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				pickupSecondSpikeAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				park
		);

		return main;
	}
}
