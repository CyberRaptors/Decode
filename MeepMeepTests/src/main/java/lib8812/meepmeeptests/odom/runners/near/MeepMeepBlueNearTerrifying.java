package lib8812.meepmeeptests.odom.runners.near;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.stubs.game.CommonPoses;

public class MeepMeepBlueNearTerrifying {
	static ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();
	static Action main;

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(CommonPoses.INITIAL_BLUE_NEAR_POSE);

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_BLUE_NEAR_POSE)
				.strafeToLinearHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE.position,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpikeClearGateAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.BLUE_FIRST_SPIKE_START_POSE,
						CommonPoses.BLUE_FIRST_SPIKE_START_POSE.heading
				)
				.splineToSplineHeading(
						CommonPoses.BLUE_FIRST_SPIKE_END_POSE,
						CommonPoses.BLUE_FIRST_SPIKE_END_POSE.heading,
						bot.SPIKE_PICKUP_VEL_CONSTRAINT
				)
				.afterTime(0, bot.setIntakeAndTransferPower(0))
				.splineToLinearHeading(
						CommonPoses.BLUE_QUICK_CLEAR_GATE_END_POSE,
						CommonPoses.BLUE_QUICK_CLEAR_GATE_END_POSE.heading
				)
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT))
				.strafeToLinearHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE.position,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupSecondSpikeAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
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
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT))
				.splineToLinearHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupThirdSpikeAndMoveToShootAndPark = drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
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
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT))
				.splineToLinearHeading(
						CommonPoses.BLUE_NEAR_SHORT_PARK_POSE,
						CommonPoses.BLUE_NEAR_SHORT_PARK_POSE.heading
				)
				.build();


		main = new SequentialAction(
				initialMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.disableShootersAsync(),

				pickupFirstSpikeClearGateAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.disableShootersAsync(),

				pickupSecondSpikeAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.disableShootersAsync(),

				pickupThirdSpikeAndMoveToShootAndPark,
				bot.shootThree(bot.SHOOTER_VELO_FOR_SHORT_PARK_SHOT),
				bot.disableShootersAsync()
		);

		return main;
	}
}
