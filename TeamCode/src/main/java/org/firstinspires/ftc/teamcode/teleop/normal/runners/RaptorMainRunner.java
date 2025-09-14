package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class RaptorMainRunner extends ITeleOpRunner {
	RaptorRobot bot = new RaptorRobot();

	public void moveWheels() {
		bot.drive.setDrivePowers(new PoseVelocity2d(
				new Vector2d(
						gamepad1.inner.left_stick_x,
						gamepad1.inner.left_stick_y
				),
				gamepad1.inner.right_stick_x
		));
	}

	@Override
	protected void internalRun() {
		while (opModeIsActive()) {
			moveWheels();

			telemetry.addData(
					"wheels",
					"rightFront (%.2f) leftFront (%.2f) rightBack (%.2f) leftBack (%.2f)",
					bot.rightFront.getPower(),
					bot.leftFront.getPower(),
					bot.rightBack.getPower(),
					bot.leftBack.getPower()
			);
			telemetry.update();
		}
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
