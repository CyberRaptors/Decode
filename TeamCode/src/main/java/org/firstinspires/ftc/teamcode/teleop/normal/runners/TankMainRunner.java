package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.TankRaptor;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class TankMainRunner extends ITeleOpRunner {
	TankRaptor bot = new TankRaptor();

	@Override
	protected void internalRun() {
		while (opModeIsActive()) {
			double leftPower = gamepad1.inner.left_stick_y;
			double rightPower = gamepad1.inner.right_stick_y;

			bot.leftFront.setPower(gamepad1.inner.left_stick_y);
			bot.rightFront.setPower(gamepad1.inner.right_stick_y);

			telemetry.addData("Left Motor Power", leftPower);
			telemetry.addData("Right Motor Power", rightPower);
			telemetry.update();
		}
	}
	@Override
	protected IRobot getBot() {
		return bot;
	}
}
