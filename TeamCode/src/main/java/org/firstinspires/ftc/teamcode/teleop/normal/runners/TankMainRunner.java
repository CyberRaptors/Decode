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
			bot.leftFront.setPower(-gamepad1.inner.left_stick_y);
			bot.rightFront.setPower(-gamepad1.inner.right_stick_y);
		}
	}
	@Override
	protected IRobot getBot() {
		return bot;
	}
}
