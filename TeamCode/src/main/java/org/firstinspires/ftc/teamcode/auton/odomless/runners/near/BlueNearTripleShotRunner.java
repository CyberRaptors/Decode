package org.firstinspires.ftc.teamcode.auton.odomless.runners.near;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;

import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class BlueNearTripleShotRunner extends ITeleOpRunner {
	public ActionableRaptorRobot bot = new ActionableRaptorRobot();

	@Override
	protected void internalRun() {
		bot.drive.setDrivePowers(
				new PoseVelocity2d(
						new Vector2d(
								-1,
								0
						),
						0
				)
		);

		sleep(1500);

		bot.drive.setDrivePowers(
				new PoseVelocity2d(
						new Vector2d(
								0,
								0
						),
						0
				)
		);

		Actions.runBlocking(bot.successiveShootWithPower(3, bot.SHOOTER_POWER_FOR_CLOSE_SHOT));

		bot.drive.setDrivePowers(
				new PoseVelocity2d(
						new Vector2d(
								0,
								-1
						),
						0
				)
		);

		sleep(1500);

		bot.drive.setDrivePowers(
				new PoseVelocity2d(
						new Vector2d(
								0,
								0
						),
						0
				)
		);
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
