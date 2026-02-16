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

public class BlueFarScaryRunner extends ITeleOpRunner {
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
						CommonPoses.BLUE_SECOND_SPIKE_START_POSE,
						CommonPoses.BLUE_SECOND_SPIKE_START_POSE.heading
				)
				.splineToSplineHeading(
						CommonPoses.BLUE_SECOND_SPIKE_END_POSE,
						CommonPoses.BLUE_SECOND_SPIKE_END_POSE.heading,
						bot.SPIKE_PICKUP_VEL_CONSTRAINT
				)
				.afterTime(1, bot.setIntakeAndTransferPower(0))
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
				.afterTime(1, bot.setIntakeAndTransferPower(0))
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
				bot.relocalize(),

				pickupSecondSpikeAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
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
