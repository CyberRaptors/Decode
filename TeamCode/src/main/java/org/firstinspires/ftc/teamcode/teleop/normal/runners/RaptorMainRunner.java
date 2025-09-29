package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class RaptorMainRunner extends ITeleOpRunner {
	RaptorRobot bot = new RaptorRobot();

	@Override
	protected void internalRun() {
		keybinder.bind("left_stick_y").of(gamepad2).to(bot.driverControl::setIntakePower);
		keybinder.bind("right_stick_y").of(gamepad2).to(bot.driverControl::setCentralToothPower);
		keybinder.bind("right_trigger").of(gamepad2).to(bot.driverControl::setShooterPower);

		while (opModeIsActive()) {
			bot.driverControl.applyDrivePower(gamepad1.inner.left_stick_y, gamepad1.inner.left_stick_x, gamepad1.inner.right_stick_x);

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
