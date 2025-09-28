package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class WheelDebuggerRunner extends ITeleOpRunner {
	RaptorRobot bot = new RaptorRobot();

	@Override
	protected void internalRun() {
		keybinder.bind("dpad_up").of(gamepad1).to(bot.leftFront::setPower);
		keybinder.bind("dpad_right").of(gamepad1).to(bot.rightFront::setPower);
		keybinder.bind("dpad_left").of(gamepad1).to(bot.leftBack::setPower);
		keybinder.bind("dpad_down").of(gamepad1).to(bot.rightBack::setPower);

		while (opModeIsActive()) {
			keybinder.executeActions();

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
