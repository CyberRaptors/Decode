package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class RaptorMainRunner extends ITeleOpRunner {
	RaptorRobot bot = new RaptorRobot();

	public void moveWheels() {
		double forwardPower = gamepad1.inner.left_stick_y;
		double strafePower = gamepad1.inner.left_stick_x;
		double angularPower = gamepad1.inner.right_stick_x;

		bot.drive.setDrivePowers(new PoseVelocity2d(
				new Vector2d(
						forwardPower,
						strafePower
				),
				angularPower
		));
	}

	@Override
	protected void internalRun() {
		keybinder.bind("right_trigger").of(gamepad2).to(bot.intake::setPower);

		while (opModeIsActive()) {
			moveWheels();


			keybinder.executeActions();

			telemetry.addData(
					"wheels",
					"rightFront (%.2f) leftFront (%.2f) rightBack (%.2f) leftBack (%.2f)",
					bot.rightFront.getPower(),
					bot.leftFront.getPower(),
					bot.rightBack.getPower(),
					bot.leftBack.getPower()
			);

			telemetry.addData(
					"intake",
					"power (%.2f)",
					bot.intake.getPower()
			);

			telemetry.update();
		}
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
