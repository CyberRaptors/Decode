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

public class BlueNearTameRunner extends ITeleOpRunner {
	ActionableRaptorRobot bot = new ActionableRaptorRobot(true);

	Action main;

	@Override
	protected void customInit() {
		MecanumDrive drive = bot.drive;

		drive.localizer.setPose(CommonPoses.INITIAL_BLUE_NEAR_POSE);

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_BLUE_NEAR_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE.position,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
						.strafeToSplineHeading(
								CommonPoses.BLUE_FIRST_SPIKE_START_POSE.position,
								CommonPoses.BLUE_FIRST_SPIKE_START_POSE.heading
						)
						.build(),
//				bot.setIntakeAndTransferPower(1),
				drive.actionBuilder(CommonPoses.BLUE_FIRST_SPIKE_START_POSE)
						.strafeToSplineHeading(
								CommonPoses.BLUE_FIRST_SPIKE_END_POSE.position,
								CommonPoses.BLUE_FIRST_SPIKE_END_POSE.heading,
								bot.SPIKE_PICKUP_VEL_CONSTRAINT
						)
						.build()
		);

		Action secondMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FIRST_SPIKE_END_POSE)
				.afterTime(0, bot.setIntakeAndTransferPower(0))
				.strafeToSplineHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE.position,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action park = drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_NEAR_SHORT_PARK_POSE.position,
						CommonPoses.BLUE_NEAR_SHORT_PARK_POSE.heading
				)
				.build();

		main = new SequentialAction(
				bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				initialMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.disableShootersAsync(),
				bot.setIntakePower(1),
				bot.setTransferPower(0.15),
				pickupFirstSpike,
				bot.startShootersAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				secondMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
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
