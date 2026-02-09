package org.firstinspires.ftc.teamcode.auton.odom.runners.near;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.InteropFields;
import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.game.CommonPoses;
import lib8812.common.robot.IRobot;
import lib8812.common.rr.MecanumDrive;
import lib8812.common.teleop.ITeleOpRunner;

public class BlueNearTerrifyingRunner extends ITeleOpRunner {
	ActionableRaptorRobot bot = new ActionableRaptorRobot(true);

	Action main;

	@Override
	protected void customInit() {
		MecanumDrive drive = bot.drive;

		drive.localizer.setPose(CommonPoses.INITIAL_BLUE_NEAR_POSE);

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_BLUE_NEAR_POSE)
				.strafeToLinearHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE.position,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpikeClearGateAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
				.splineToSplineHeading(
						CommonPoses.BLUE_FIRST_SPIKE_START_POSE,
						CommonPoses.BLUE_FIRST_SPIKE_START_POSE.heading
				)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.BLUE_FIRST_SPIKE_END_POSE,
						CommonPoses.BLUE_FIRST_SPIKE_END_POSE.heading,
						bot.SPIKE_PICKUP_VEL_CONSTRAINT
				)
				.afterTime(2.5, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT))
				.splineToLinearHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupSecondSpikeAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
				.splineToSplineHeading(
						CommonPoses.BLUE_SECOND_SPIKE_START_POSE,
						CommonPoses.BLUE_SECOND_SPIKE_START_POSE.heading
				)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.BLUE_SECOND_SPIKE_END_POSE,
						CommonPoses.BLUE_SECOND_SPIKE_END_POSE.heading,
						bot.SPIKE_PICKUP_VEL_CONSTRAINT
				)
				.afterTime(2.5, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT))
				.splineToLinearHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupThirdSpikeAndMoveToPark = drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
				.splineToSplineHeading(
						CommonPoses.BLUE_THIRD_SPIKE_START_POSE,
						CommonPoses.BLUE_THIRD_SPIKE_START_POSE.heading
				)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.BLUE_THIRD_SPIKE_END_POSE,
						CommonPoses.BLUE_THIRD_SPIKE_END_POSE.heading,
						bot.SPIKE_PICKUP_VEL_CONSTRAINT
				)
				.strafeToLinearHeading(
						CommonPoses.BLUE_THIRD_SPIKE_START_POSE.position,
						CommonPoses.BLUE_THIRD_SPIKE_START_POSE.heading
				)
				.afterTime(0.5, bot.setIntakeAndTransferPower(0))
//				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT))
				.strafeToLinearHeading(
						CommonPoses.BLUE_CLEAR_GATE_START_POSE.position,
						CommonPoses.BLUE_CLEAR_GATE_START_POSE.heading
				)
				.build();


		main = new SequentialAction(
				bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				initialMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.disableShootersAsync(),

				pickupFirstSpikeClearGateAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.disableShootersAsync(),

				pickupSecondSpikeAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.disableShootersAsync(),

				pickupThirdSpikeAndMoveToPark
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
