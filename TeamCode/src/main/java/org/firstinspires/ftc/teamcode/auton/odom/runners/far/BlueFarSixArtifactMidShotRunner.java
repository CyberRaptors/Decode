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

public class BlueFarSixArtifactMidShotRunner extends ITeleOpRunner {
	ActionableRaptorRobot bot = new ActionableRaptorRobot(true);

	Action main;

	@Override
	protected void customInit() {
		MecanumDrive drive = bot.drive;

		drive.localizer.setPose(CommonPoses.INITIAL_BLUE_FAR_POSE);

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_BLUE_FAR_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_MID_SHOT_POSE.position,
						CommonPoses.BLUE_MID_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.BLUE_MID_SHOT_POSE)
						.strafeToSplineHeading(
								CommonPoses.BLUE_THIRD_SPIKE_START_POSE.position,
								CommonPoses.BLUE_THIRD_SPIKE_START_POSE.heading
						)
						.build(),
				bot.setIntakeGroupPower(1),
				drive.actionBuilder(CommonPoses.BLUE_THIRD_SPIKE_START_POSE)
						.strafeToSplineHeading(
								CommonPoses.BLUE_THIRD_SPIKE_END_POSE.position,
								CommonPoses.BLUE_THIRD_SPIKE_END_POSE.heading
						)
						.strafeToSplineHeading(
								CommonPoses.BLUE_THIRD_SPIKE_START_POSE.position,
								CommonPoses.BLUE_THIRD_SPIKE_START_POSE.heading
						)
						.build()
		);

		Action secondMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_THIRD_SPIKE_START_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_MID_SHOT_POSE.position,
						CommonPoses.BLUE_MID_SHOT_POSE.heading
				)
				.build();

		Action park = drive.actionBuilder(CommonPoses.BLUE_MID_SHOT_POSE)
				.strafeTo(CommonPoses.BLUE_FAR_PARK_POS)
				.build();

		main = new SequentialAction(
				bot.setShooterVelocityAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT), // spin up shooter in advance
				initialMoveToShoot,
				bot.shootWithVelo(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.feedNext(1.5),
				bot.shootWithVelo(bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				bot.disableShootersAsync(),
				pickupFirstSpike,
				bot.setRailDriveTwoPower(-1.0),
				secondMoveToShoot,
				new SleepAction(0.3),
				bot.setIntakeGroupPower(0),
				bot.setShooterVelocityAsync(bot.SHOOTER_VELO_FOR_CLOSE_SHOT), // spin up shooter in advance
				new SleepAction(0.4),
				bot.successiveShootWithVelo(2, bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
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
