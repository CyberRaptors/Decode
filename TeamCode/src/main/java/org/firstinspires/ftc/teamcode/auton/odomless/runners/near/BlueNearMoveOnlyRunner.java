package org.firstinspires.ftc.teamcode.auton.odomless.runners.near;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class BlueNearMoveOnlyRunner extends ITeleOpRunner {
	public ActionableRaptorRobot bot = new ActionableRaptorRobot();

	@Override
	protected void internalRun() {
		bot.drive.setDrivePowers(
				new PoseVelocity2d(
						new Vector2d(
								0,
								1
						),
						0
				)
		);

		sleep(1000);

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
