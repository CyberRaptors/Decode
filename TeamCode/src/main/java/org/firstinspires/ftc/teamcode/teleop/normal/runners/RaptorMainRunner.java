package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

/* RaptorMain - Main TeleOp OpMode for [Named]Raptor

List of Controls

DRIVER A
	LEFT STICK (Y) - Move forward (+) and backward (-)
	LEFT STICK (X) - Strafe right (+) and left (-)
	RIGHT STICK (X) - Turn clockwise (+) and counterclockwise (-)

	X - Enable/disable verbose mode

DRIVER B
	LEFT TRIGGER - Run intake & rail drive one (collectively rail group one)
	RIGHT TRIGGER - Apply shooter brakes
	LEFT STICK (Y) - Run rail drive two counterclockwise (+) and clockwise (-)

	A - Enable/disable shooter
 */

public class RaptorMainRunner extends ITeleOpRunner {
	RaptorRobot bot = new RaptorRobot();

	boolean shooterEnabled = false;
	boolean verbose = false;

	double shooterMaxPower = 0.6;

	void setRailGroupOnePower(double power) {
		bot.driverControl.setIntakePower(power);
		bot.driverControl.setRailDriveOnePower(power);
	}

	void toggleShooterEnabled() {
		shooterEnabled = !shooterEnabled;

		bot.driverControl.setShooterPower(0);
	}

	void applyShooterPowerFromInput(double brakePower) {
		if (!shooterEnabled) {
			bot.driverControl.setShooterPower(0);
			return;
		}

		bot.driverControl.setShooterPower(Math.max(0, shooterMaxPower -brakePower));
	}

	@Override
	protected void internalRun() {
		keybinder.bind("left_stick_y").of(gamepad2).to(bot.driverControl::setRailDriveTwoPower);

		keybinder.bind("a").of(gamepad2).to(this::toggleShooterEnabled);

		keybinder.bind("right_trigger").of(gamepad2).to(this::applyShooterPowerFromInput);
		keybinder.bind("left_trigger").of(gamepad2).to(this::setRailGroupOnePower);

		keybinder.bind("right_stick_y").of(gamepad2).to((value) -> bot.driverControl.setRailDriveThreePosition(
				bot.railDriveThree.getPosition()+(value/1000)
		));

		keybinder.bind("x").of(gamepad1).to(() -> verbose = !verbose);

		keybinder.bind("dpad_up").of(gamepad2).to(() -> shooterMaxPower = 0.6); // close corner shot
		keybinder.bind("dpad_down").of(gamepad2).to(() -> shooterMaxPower = 0.55); // close mid shot

		while (opModeIsActive()) {
			bot.driverControl.applyDrivePower(gamepad1.inner.left_stick_y, gamepad1.inner.left_stick_x, gamepad1.inner.right_stick_x);

			keybinder.executeActions();

			if (verbose) {
				telemetry.addData(
						"wheels",
						"rightFront (%.2f) leftFront (%.2f) rightBack (%.2f) leftBack (%.2f)",
						bot.rightFront.getPower(),
						bot.leftFront.getPower(),
						bot.rightBack.getPower(),
						bot.leftBack.getPower()
				);
			}

			telemetry.addData(
					"intake",
					"left power (%.2f) right power (%.2f)",
					bot.intakeLeft.getPower(), bot.intakeRight.getPower()
			);

			telemetry.addData(
					"rail drive one",
					"power (%.2f)",
					bot.railDriveOne.getPower()
			);

			telemetry.addData(
					"rail drive two",
					"power (%.2f)",
					bot.railDriveTwo.getPower()
			);

			telemetry.addData(
					"rail drive three (feeder)",
					"position (%.2f) min (%.2f) max (%.2f)",
					bot.railDriveThree.getPosition(),
					bot.RAIL_DRIVE_THREE_MIN_POS,
					bot.RAIL_DRIVE_THREE_MAX_POS
			);

			if (shooterEnabled) {
				telemetry.addData(
						"shooter",
						"power (%.2f) power limit (%.2f)",
						bot.shooterLeft.getPower(),
						shooterMaxPower
				);
			} else {
				telemetry.addData(
						"shooter",
						"disabled, power limit (%.2f)",
						shooterMaxPower
				);
			}

			telemetry.update();
		}
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
