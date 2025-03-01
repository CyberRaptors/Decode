package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.robot.FalconRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;
import lib8812.common.teleop.TeleOpUtils;

public class FalconOutreachRunner extends ITeleOpRunner {
	FalconRobot bot = new FalconRobot();

	void moveWheels() {
		// Take the average of both gamepads' power
		double greatestXValue = (gamepad1.inner.right_stick_x + gamepad1.inner.left_stick_x) / 1.5;
		double greatestYValue = (gamepad1.inner.right_stick_y + gamepad1.inner.left_stick_y) / 1.5;

		// swap y and x here as the robot's position is technically rotated by PI/2 radians
		double yPower = -TeleOpUtils.fineAndFastControl(greatestXValue);
		double xPower = -TeleOpUtils.fineAndFastControl(greatestYValue);

		double turnPower = (gamepad1.inner.left_trigger - gamepad1.inner.right_trigger) * 0.8;

		if (Math.signum(gamepad1.inner.right_stick_y) == -Math.signum(gamepad1.inner.left_stick_y) && Math.signum(gamepad1.inner.right_stick_y) != 0) {
			turnPower = - (gamepad1.inner.right_stick_y - gamepad1.inner.left_stick_y) * 0.5;
		}

		bot.drive.setDrivePowers(new PoseVelocity2d(
				new Vector2d(
						xPower * 0.9,
						yPower * 0.9
				),
				turnPower * 0.9
		));
	}

	void togglePlaneShooter() {
		if (bot.planeShooter.getPosition() == bot.PLANE_READY) {
			bot.planeShooter.setPosition(bot.PLANE_SHOT);
		} else {
			bot.planeShooter.setPosition(bot.PLANE_READY);
		}
	}

	@Override
	protected void internalRun() {

		keybinder.bind("right_bumper").of(gamepad2).to(this::togglePlaneShooter);

		while (opModeIsActive()) {
			moveWheels();

			bot.lift.setPower(gamepad2.inner.right_stick_y);
			bot.intake.setPower(gamepad2.inner.left_trigger-gamepad2.inner.right_trigger);

			keybinder.executeActions();

			telemetry.addData("lift", "power (%.2f)", bot.lift.getPower());
		}
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
