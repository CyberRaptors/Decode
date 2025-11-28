/* RaptorMain - Main TeleOp OpMode for Raptor Series Bots

List of Controls

DRIVER A
	LEFT STICK (Y) - Move forward (+) and backward (-)
	LEFT STICK (X) - Strafe right (+) and left (-)
	RIGHT STICK (X) - Turn clockwise (+) and counterclockwise (-)

	B - Globally cancel all macros
	X - Enable/disable verbose mode
	Y - Dispatch Limelight-enabled goal alignment macro

DRIVER B
	RIGHT TRIGGER - Run intake & rail group forwards
	LEFT TRIGGER - Run intake group in reverse
	LEFT STICK (Y) - Run transfer forwards (+) and backwards (-)
	RIGHT STICK (Y) - Run transfer forwards (+) and backwards (-)

	RIGHT BUMPER - Run shooter to intake from human
	LEFT BUMPER - Enable automatic shooter velocity control
		- Shooter velocity control starts as manual; the driver can set velocity presets using their designated controls
		- In automatic control, the optimal velocity is automatically calculated based on the robot's localization
			- Pressing any of the manual velocity setting controls will return the velocity control to manual mode

	DPAD UP - Increase shooter preset velocity
	DPAD DOWN - Decrease shooter preset velocity
	DPAD RIGHT - Increase shooter velocity
	DPAD LEFT - Decrease shooter velocity

	A - Enable/disable shooter
	B - Globally cancel all macros
 */

package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import lib8812.common.game.GameConstants;
import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class RaptorMainRunner extends ITeleOpRunner {
	public RaptorMainRunner(boolean blueTeam) {
		bot = new ActionableRaptorRobot(blueTeam);
		shooterMaxVelo = bot.SHOOTER_VELO_FOR_MID_SHOT;
	}

	final ActionableRaptorRobot bot;

	boolean shooterEnabled = false;
	boolean autoVelocityMode = false;
	boolean verbose = false;

	boolean autoVeloActionStillRunning = false;

	double shooterMaxVelo;

	void toggleShooterEnabled() {
		if (shooterMaxVelo < 0) {
			shooterMaxVelo = bot.SHOOTER_VELO_FOR_MID_SHOT;
		} else {
			shooterEnabled = !shooterEnabled;
		}

		bot.driverControl.setShooterOff();
	}

	void runShooter() {
		if (!shooterEnabled) {
			bot.driverControl.setShooterOff();
			return;
		}

		bot.driverControl.setShooterVelocity(shooterMaxVelo);
	}

	void autoAdjustShooterMaxVelo() {
		if (autoVeloActionStillRunning) return; // ensure that only one velocity update action is dispatched at a time

		autoVeloActionStillRunning = true;

		actions.scheduleAll(
				new SequentialAction(
						bot.requireLimelightRelocalization(new InstantAction(() -> {
							Vector2d botPos = bot.drive.localizer.getPose().position;

							double distanceFromBotCenterToGoalCorner = GameConstants.DECODE.GOAL_POSITION(bot.onBlueTeam).minus(botPos).norm();

							shooterMaxVelo = bot.calculateV0ForV2Shooter(distanceFromBotCenterToGoalCorner);
						}), 20),
						new InstantAction(() -> autoVeloActionStillRunning = false) // we need a SequentialAction to reset this flag in case the requireLimelightRelocalization gives up and never dispatches the velo update InstantAction
				)
		);
	}

	void incrementVeloPreset() {
		autoVelocityMode = false;

		for (int i = 0; i < bot.SHOOTER_VELO_PRESETS.length-1; i++) {
			if (shooterMaxVelo <= bot.SHOOTER_VELO_PRESETS[i]) {
				shooterMaxVelo = bot.SHOOTER_VELO_PRESETS[i+1];
				return;
			}
		}

		shooterMaxVelo = bot.SHOOTER_VELO_PRESETS[bot.SHOOTER_VELO_PRESETS.length-1];
	}

	void decrementVeloPreset() {
		autoVelocityMode = false;

		for (int i = bot.SHOOTER_VELO_PRESETS.length-2; i >= 0; i--) {
			if (shooterMaxVelo >= bot.SHOOTER_VELO_PRESETS[i+1]) {
				shooterMaxVelo = bot.SHOOTER_VELO_PRESETS[i];
				return;
			}
		}

		shooterMaxVelo = bot.SHOOTER_VELO_PRESETS[0];
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
//		keybinder.bind("a").of(gamepad1).to(() -> actions.scheduleAll(bot.strafeToBase()));

		keybinder.bind("left_stick_y").of(gamepad2).to(bot.driverControl::setTransferPower);
		keybinder.bind("right_stick_y").of(gamepad2).to(bot.driverControl::setTransferPower);

		keybinder.bind("dpad_up").of(gamepad2).to(this::incrementVeloPreset);
		keybinder.bind("dpad_down").of(gamepad2).to(this::decrementVeloPreset);

		keybinder.bind("right_bumper").of(gamepad2).to(() -> {
			autoVelocityMode = false;

			if (shooterMaxVelo < 0) {
				shooterMaxVelo = bot.SHOOTER_VELO_FOR_MID_SHOT;
				shooterEnabled = false;
			} else {
				shooterMaxVelo = -1000;
				shooterEnabled = true;
			}

		});
		keybinder.bind("left_bumper").of(gamepad2).to(() -> autoVelocityMode = true);

		keybinder.bind("a").of(gamepad2).to(this::toggleShooterEnabled);
		keybinder.bind("b").of(gamepad2).to(cancelMacros);

		keybinder.bind("dpad_left").of(gamepad2).to(() -> {
			autoVelocityMode = false;

			shooterMaxVelo = Math.max(0, shooterMaxVelo-30);
		});
		keybinder.bind("dpad_right").of(gamepad2).to(() -> {
			autoVelocityMode = false;

			shooterMaxVelo+=30;
		});

		while (opModeIsActive()) {
			bot.driverControl.applyDrivePower(-gamepad1.inner.left_stick_y, -gamepad1.inner.left_stick_x, -gamepad1.inner.right_stick_x);
			bot.driverControl.setIntakePower(gamepad2.inner.right_trigger-gamepad2.inner.left_trigger); // use a direct call instead of two separate keybind patterns for this to avoid overwrites

			if (autoVelocityMode) {
				autoAdjustShooterMaxVelo();
			}

			runShooter();

			bot.drive.localizer.update();

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
					"intake group",
					"power (%.2f)",
					bot.intake.getPower()
			);

			telemetry.addData(
					"transfer",
					"main power (%.2f), helper one power (%.2f), helper two power (%.2f)",
					bot.transfer.getPower(),
					bot.transferHelperOne.getPower(),
					bot.transferHelperTwo.getPower()
			);

			if (shooterEnabled) {
				telemetry.addData(
						"shooter",
						"velo limit (%.2f) left velo (%.2f) right velo (%.2f) shot type (%s)",
						shooterMaxVelo,
						bot.shooterLeft.getVelocity(),
						bot.shooterRight.getVelocity(),
						bot.getShooterVelocityPresetLabel(shooterMaxVelo)
				);
			} else {
				telemetry.addData(
						"shooter",
						"disabled, velo limit (%.2f) shot type (%s)",
						shooterMaxVelo,
						bot.getShooterVelocityPresetLabel(shooterMaxVelo)
				);
			}

			Pose2d botPose = bot.drive.localizer.getPose();
			telemetry.addData("current position", "x: %.2f in, y: %.2f in, heading: %.2f deg", botPose.position.x, botPose.position.y, Math.toDegrees(botPose.heading.toDouble()));

			telemetry.update();
		}
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
