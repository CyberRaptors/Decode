package org.firstinspires.ftc.teamcode.auton.odom.runners.far;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.InteropFields;
import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.game.CommonPoses;
import lib8812.common.robot.IRobot;
import lib8812.common.rr.MecanumDrive;
import lib8812.common.teleop.ITeleOpRunner;

public class RedFarTerrifyingRunner extends ITeleOpRunner {
	ActionableRaptorRobot bot = new ActionableRaptorRobot(true);

	Action main;

	@Override
	protected void customInit() {
		MecanumDrive drive = bot.drive;

		drive.localizer.setPose(CommonPoses.INITIAL_RED_FAR_POSE);

		// SimplyCompat

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_RED_FAR_POSE)
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.strafeToSplineHeading(
						CommonPoses.RED_FAR_SHOT_POSE.position,
						CommonPoses.RED_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpikeAndMoveToShoot = drive.actionBuilder(CommonPoses.RED_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.RED_THIRD_SPIKE_START_POSE,
						CommonPoses.RED_THIRD_SPIKE_START_POSE.heading
				)
				.splineToSplineHeading(
						CommonPoses.RED_THIRD_SPIKE_END_POSE,
						CommonPoses.RED_THIRD_SPIKE_END_POSE.heading,
						bot.SPIKE_PICKUP_VEL_CONSTRAINT
				)
				.afterTime(1, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.splineToLinearHeading(
						CommonPoses.RED_FAR_SHOT_POSE,
						CommonPoses.RED_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFromLoadingZoneAndMoveToShoot = drive.actionBuilder(CommonPoses.RED_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.strafeToLinearHeading(
						CommonPoses.RED_FAR_TERRIFYING_PICKUP_FROM_LOADING_ZONE_POSE.position,
						CommonPoses.RED_FAR_TERRIFYING_PICKUP_FROM_LOADING_ZONE_POSE.heading
				)
				.stopAndAdd(new SleepAction(2.5))
				.afterTime(1, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.strafeToLinearHeading(
						CommonPoses.RED_FAR_SHOT_POSE.position,
						CommonPoses.RED_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFromSecretTunnelAndMoveToShoot = drive.actionBuilder(CommonPoses.RED_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.strafeToLinearHeading(
						CommonPoses.RED_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE.position,
						CommonPoses.RED_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE.heading
				)
				.stopAndAdd(new SleepAction(2.5))
				.afterTime(1, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.strafeToLinearHeading(
						CommonPoses.RED_FAR_SHOT_POSE.position,
						CommonPoses.RED_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFromSecretTunnelAndPark =  drive.actionBuilder(CommonPoses.RED_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.strafeToLinearHeading(
						CommonPoses.RED_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE.position,
						CommonPoses.RED_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE.heading
				)
				.stopAndAdd(new SleepAction(2.5))
				.afterTime(1, bot.setIntakeAndTransferPower(0))
				.build();

		main = new SequentialAction(
				initialMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				pickupFirstSpikeAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),
				bot.relocalize(),

				pickupFromLoadingZoneAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),
				bot.relocalize(),

				pickupFromSecretTunnelAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				pickupFromSecretTunnelAndPark
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
