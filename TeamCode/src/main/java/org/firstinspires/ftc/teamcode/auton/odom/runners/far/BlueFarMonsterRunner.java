package org.firstinspires.ftc.teamcode.auton.odom.runners.far;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.InteropFields;
import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.game.CommonPoses;
import lib8812.common.robot.IRobot;
import lib8812.common.rr.MecanumDrive;
import lib8812.common.teleop.ITeleOpRunner;

public class BlueFarMonsterRunner extends ITeleOpRunner {
	ActionableRaptorRobot bot = new ActionableRaptorRobot(true);

	Action main;

	@Override
	protected void customInit() {
		MecanumDrive drive = bot.drive;

		drive.localizer.setPose(CommonPoses.INITIAL_BLUE_FAR_POSE);

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

		Action pickupSecondSpikeAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
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
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.splineToLinearHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		// contrary to the "terrifying" auto action, we don't wait at the loading zone but rather assume we have taken at least some artifacts to save time and possibly run the routine twice
		// because it is highly unlikely that we get three artifacts, we use shootTwo instead
		Action firstPickupFromLoadingZoneAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.BLUE_FAR_MONSTER_PICKUP_FROM_LOADING_ZONE_POSE,
						CommonPoses.BLUE_FAR_MONSTER_PICKUP_FROM_LOADING_ZONE_POSE.heading,
						bot.MONSTER_LZ_PICKUP_VEL_CONSTRAINT,
						bot.MONSTER_LZ_PICKUP_ACCEL_CONSTRAINT
				)
				.afterTime(0, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.splineToLinearHeading(
						CommonPoses.BLUE_FAR_MONSTER_SHOT_POSE,
						CommonPoses.BLUE_FAR_MONSTER_SHOT_POSE.heading
				)
				.build();


		Action secondPickupFromLoadingZoneAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FAR_MONSTER_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.BLUE_FAR_MONSTER_PICKUP_FROM_LOADING_ZONE_POSE,
						CommonPoses.BLUE_FAR_MONSTER_PICKUP_FROM_LOADING_ZONE_POSE.heading,
						bot.MONSTER_LZ_PICKUP_VEL_CONSTRAINT,
						bot.MONSTER_LZ_PICKUP_ACCEL_CONSTRAINT
				)
				.afterTime(0, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.splineToLinearHeading(
						CommonPoses.BLUE_FAR_MONSTER_SHOT_POSE,
						CommonPoses.BLUE_FAR_MONSTER_SHOT_POSE.heading
				)
				.build();

		Action park = drive.actionBuilder(CommonPoses.BLUE_FAR_MONSTER_SHOT_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_FAR_PARK_POSE.position,
						CommonPoses.BLUE_FAR_PARK_POSE.heading
				)
				.build();


		// NOTE: idea to increase speed to fit within time limit: move monster shot pose to as close as possible near the loading zone
		// then use strafes to get back there to avoid crashing into wall (you can see if strafes or splines are faster on the way in)
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

				firstPickupFromLoadingZoneAndMoveToShoot,
				bot.shootTwo(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				secondPickupFromLoadingZoneAndMoveToShoot,
				bot.shootTwo(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				park
		);
	}

	@Override
	protected void internalRun() {
		Actions.runBlocking(main);
		InteropFields.lastKnownPose = bot.drive.localizer.getPose();
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
