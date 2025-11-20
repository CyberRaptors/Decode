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
	LEFT STICK (Y) - Run rail drive two counterclockwise [towards shooter] (+) and clockwise [towards intake] (-)
	RIGHT STICK (Y) - Move rail drive three (feeder) forwards (+) and backwards (-)

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
	X - Dispatch auto-shoot macro
	Y - Dispatch successive auto-shoot macro (3 shots)
 */

package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.teamcode.robot.ActionableRaptorRobot;

import java.util.List;

import lib8812.common.actions.OnceAction;
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

	double shooterMaxVelo;

	void toggleShooterEnabled() {
		if (shooterMaxVelo < 0) {
			shooterMaxVelo = bot.SHOOTER_VELO_FOR_MID_SHOT;
		} else {
			shooterEnabled = !shooterEnabled;
		}

		bot.driverControl.setShooterPower(0);
	}

	void runShooter() {
		if (!shooterEnabled) {
			bot.driverControl.setShooterPower(0);
			return;
		}

		bot.driverControl.setShooterVelocity(shooterMaxVelo);
	}

	void autoAdjustShooterMaxVelo() {
		actions.scheduleAll(
				bot.new LockedUsageAction(
						new SequentialAction(
								new InstantAction(() -> bot.limelight.pipelineSwitch(bot.LIMELIGHT_APRILTAG_INDEX)),
								(telemetryPacket) -> {
									LLResult res = bot.limelight.getLatestResult();

									return res.getPipelineIndex() != bot.LIMELIGHT_APRILTAG_INDEX;
								},
								new OnceAction(
										() -> {
											LLResult res = bot.limelight.getLatestResult();

											return res.isValid();
										},
										new InstantAction(() -> {
											LLResult res = bot.limelight.getLatestResult();

											if (!res.isValid()) return;

											List<LLResultTypes.FiducialResult> fiducials = res.getFiducialResults();

											for (LLResultTypes.FiducialResult fiducial : fiducials) {
												if (fiducial.getFiducialId() == GameConstants.DECODE.GOAL_APRILTAG_ID(bot.onBlueTeam)) {
													double targetVerticalDegreesOffset = fiducial.getTargetYDegrees();

													double angleToGoal = Math.toRadians(bot.limelightMountInfo.mountDegreesFromVertical + targetVerticalDegreesOffset);

													double approxDistanceFromGoalToShooter = (
															GameConstants.DECODE.GOAL_APRILTAG_HEIGHT_IN - bot.limelightMountInfo.lensInchesFromGround
													) / Math.tan(angleToGoal);

													shooterMaxVelo = bot.calculateV0ForV2Shooter(approxDistanceFromGoalToShooter);
												}
											}
										})
								)
						),
						bot.limelight
				)
		);
	}

	@Override
	protected void internalRun() {
		Runnable cancelMacros = () -> {
			actions.clear();
			bot.releaseAllDevices();
		};

		keybinder.bind("b").of(gamepad1).to(cancelMacros);
		keybinder.bind("x").of(gamepad1).to(() -> verbose = !verbose);
		keybinder.bind("y").of(gamepad1).to(() -> actions.scheduleAll(bot.localizationEnabledAlignToGoal()));
		keybinder.bind("a").of(gamepad1).to(() -> actions.scheduleAll(bot.strafeToBase()));


		keybinder.bind("right_trigger").of(gamepad2).to(bot.driverControl::setIntakeGroupPower);
		keybinder.bind("left_trigger").of(gamepad2).to((power) -> bot.driverControl.setIntakeGroupPower(-power));

		keybinder.bind("left_stick_y").of(gamepad2).to(bot.driverControl::setRailDriveTwoPower);
		keybinder.bind("right_stick_y").of(gamepad2).to((value) -> bot.driverControl.setRailDriveThreePosition(
				bot.railDriveThree.getPosition()+(value/100)
		));

		keybinder.bind("dpad_up").of(gamepad2).to(() -> {
			autoVelocityMode = false;

			if (shooterMaxVelo <= bot.SHOOTER_VELO_FOR_CLOSE_SHOT) {
				shooterMaxVelo = bot.SHOOTER_VELO_FOR_MID_SHOT;
			} else {
				shooterMaxVelo = bot.SHOOTER_VELO_FOR_FAR_SHOT;
			}
		});
		keybinder.bind("dpad_down").of(gamepad2).to(() -> {
			autoVelocityMode = false;

			if (shooterMaxVelo >= bot.SHOOTER_VELO_FOR_FAR_SHOT) {
				shooterMaxVelo = bot.SHOOTER_VELO_FOR_MID_SHOT;
			} else {
				shooterMaxVelo = bot.SHOOTER_VELO_FOR_CLOSE_SHOT;
			}
		});

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
		keybinder.bind("x").of(gamepad2).to(() -> actions.scheduleAll(bot.shootWithVelo(shooterMaxVelo)));

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

			if (autoVelocityMode) {
				autoAdjustShooterMaxVelo();
			}

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
					"intake group",
					"power (%.2f)",
					bot.intakeAndRailDriveOne.getPower()
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
