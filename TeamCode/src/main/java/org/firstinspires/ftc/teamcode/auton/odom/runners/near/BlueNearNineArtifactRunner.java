package org.firstinspires.ftc.teamcode.auton.odom.runners.near;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.game.CommonPoses;
import lib8812.common.robot.IRobot;
import lib8812.common.rr.MecanumDrive;
import lib8812.common.teleop.ITeleOpRunner;

public class BlueNearNineArtifactRunner extends ITeleOpRunner {
	ActionableRaptorRobot bot = new ActionableRaptorRobot();

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
				bot.setIntakeGroupPower(1),
				drive.actionBuilder(CommonPoses.BLUE_FIRST_SPIKE_START_POSE)
						.afterDisp(5, bot.setRailDriveTwoPower(-1))
						.strafeToSplineHeading(
								CommonPoses.BLUE_FIRST_SPIKE_END_POSE.position,
								CommonPoses.BLUE_FIRST_SPIKE_END_POSE.heading,
								bot.SPIKE_PICKUP_VEL_CONSTRAINT
						)
						.build()
		);

		Action secondMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FIRST_SPIKE_END_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE.position,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupSecondSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
						.strafeToSplineHeading(
								CommonPoses.BLUE_SECOND_SPIKE_START_POSE.position,
								CommonPoses.BLUE_SECOND_SPIKE_START_POSE.heading
						)
						.build(),
				bot.setIntakeGroupPower(1),
				drive.actionBuilder(CommonPoses.BLUE_SECOND_SPIKE_START_POSE)
						.afterDisp(5, bot.setRailDriveTwoPower(-1))
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
						CommonPoses.BLUE_NEAR_SHOT_POSE.position,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action park = drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
				.strafeTo(CommonPoses.BLUE_NEAR_PARK_POS)
				.build();

		main = new SequentialAction(
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
	}

	@Override
	protected void internalRun() {
		Actions.runBlocking(main);
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
