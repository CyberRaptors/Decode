/* RaptorMain - Main TeleOp OpMode for Raptor Series Bots

List of Controls

DRIVER A
	LEFT STICK (Y) - Move forward (+) and backward (-)
	LEFT STICK (X) - Strafe right (+) and left (-)
	RIGHT STICK (X) - Turn clockwise (+) and counterclockwise (-)

	B - Globally cancel all macros
	X - Enable/disable verbose mode

DRIVER B
	LEFT TRIGGER - Run intake & rail drive one (collectively rail group one)
	RIGHT TRIGGER - Apply shooter brakes
	LEFT STICK (Y) - Run rail drive two counterclockwise (+) and clockwise (-)
	RIGHT STICK (Y) - Move rail drive three (feeder) forwards (+) and backwards (-)

	DPAD UP - Set shooter speed for mid shot
	DPAD DOWN - Set shooter speed for close shot

	A - Enable/disable shooter
	B - Globally cancel all macros
	X - Dispatch auto-shoot macro
	Y - Dispatch successive auto-shoot macro (3 shots)
 */

package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class RaptorMainRunner extends ITeleOpRunner {
	ActionableRaptorRobot bot = new ActionableRaptorRobot();

	boolean shooterEnabled = false;
	boolean verbose = false;

	double shooterMaxVelo = bot.SHOOTER_VELO_FOR_MID_SHOT;

	void setRailGroupOnePower(double power) {
		bot.driverControl.setIntakePower(power);
		bot.driverControl.setRailDriveOnePower(power);
	}

	void toggleShooterEnabled() {
		shooterEnabled = !shooterEnabled;

		bot.driverControl.setShooterPower(0);
	}

	void runShooter() {
		if (!shooterEnabled) {
			bot.driverControl.setShooterPower(0);
			return;
		}

		bot.driverControl.setShooterVelocity(Math.max(0, shooterMaxVelo));
	}

	@Override
	protected void internalRun() {
		Runnable cancelMacros = () -> {
			actions.clear();
			bot.releaseAllDevices();
		};

		keybinder.bind("b").of(gamepad1).to(cancelMacros);
		keybinder.bind("x").of(gamepad1).to(() -> verbose = !verbose);
		keybinder.bind("y").of(gamepad1).to(() -> actions.scheduleAll(bot.limelightAlignToGoal()));

		// for reject
		// shooter power : 0.175
		// move feeder to 0.65, then min

		keybinder.bind("left_trigger").of(gamepad2).to(this::setRailGroupOnePower);
		keybinder.bind("right_trigger").of(gamepad2).to((power) -> bot.driverControl.setRailDriveOnePower(-power));
		keybinder.bind("left_stick_y").of(gamepad2).to(bot.driverControl::setRailDriveTwoPower);
		keybinder.bind("right_stick_y").of(gamepad2).to((value) -> bot.driverControl.setRailDriveThreePosition(
				bot.railDriveThree.getPosition()+(value/100)
		));

		keybinder.bind("dpad_up").of(gamepad2).to(() -> shooterMaxVelo = bot.SHOOTER_VELO_FOR_MID_SHOT);
		keybinder.bind("dpad_down").of(gamepad2).to(() -> shooterMaxVelo = bot.SHOOTER_VELO_FOR_CLOSE_SHOT);

		keybinder.bind("a").of(gamepad2).to(this::toggleShooterEnabled);
		keybinder.bind("b").of(gamepad2).to(cancelMacros);
		keybinder.bind("x").of(gamepad2).to(() -> actions.scheduleAll(bot.shootWithVelo(shooterMaxVelo)));
		keybinder.bind("y").of(gamepad2).to(() -> actions.scheduleAll(bot.reject()));


		keybinder.bind("dpad_left").of(gamepad2).to(() -> shooterMaxVelo = Math.max(0, shooterMaxVelo-30));
		keybinder.bind("dpad_right").of(gamepad2).to(() -> shooterMaxVelo+=30);

		while (opModeIsActive()) {
			bot.driverControl.applyDrivePower(gamepad1.inner.left_stick_y, gamepad1.inner.left_stick_x, gamepad1.inner.right_stick_x);
			runShooter();

			keybinder.executeActions();
			actions.execute();

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
						"velo limit (%.2f) left velo (%.2f) right velo (%.2f)",
						shooterMaxVelo,
						bot.shooterLeft.getVelocity(),
						bot.shooterRight.getVelocity()
				);
			} else {
				telemetry.addData(
						"shooter",
						"disabled, velo limit (%.2f)",
						shooterMaxVelo
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
