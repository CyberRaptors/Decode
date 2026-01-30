/* RaptorMain - Main TeleOp OpMode for Raptor Series Bots

List of Controls

DRIVER A
	LEFT STICK (Y) - Move forward (+) and backward (-)
	LEFT STICK (X) - Strafe right (+) and left (-)
	RIGHT STICK (X) - Turn clockwise (+) and counterclockwise (-)

	B - Globally cancel all macros
	X - Enable/disable verbose mode
	Y (HOLD) - Limelight-enabled goal alignment
	A (HOLD) - Strafe to base (full park)

	RIGHT BUMPER (HOLD) - Clear gate
	LEFT BUMPER (HOLD) - Clear gate with possibility for intaking

DRIVER B
	RIGHT TRIGGER - Run intake & rail group forwards
	LEFT TRIGGER - Run intake group in reverse
	LEFT STICK (Y) - Run transfer forwards (+) and backwards (-)
	RIGHT STICK (Y) - Run transfer forwards (+) and backwards (-)

	RIGHT BUMPER (HOLD) - Relinquish shooting control to auto-shoot mode
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

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
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
	boolean autoShootMode = false;
	boolean verbose = false;

	boolean autoVeloActionStillRunning = false;
	boolean autoShotActionStillRunning = false;

	double shooterMaxVelo;

	long timeOfLastLimelightLocalization = 0;

	Vector2d lastLinearVel;
	Rotation2d headingBeforeAutoShootMode;

	Action clearGateAction;
	Action clearGateSimplyStratAction;
	Action strafeToBaseAction;

	void toggleShooterEnabled() {
		autoShootMode = false; // toggling the shooter should relinquish auto-shoot control

		if (shooterMaxVelo < 0) {
			shooterMaxVelo = bot.SHOOTER_VELO_FOR_MID_SHOT;
		} else {
			shooterEnabled = !shooterEnabled;
		}

		bot.driverControl.setShooterOff();
	}

	void runShooter() {
		if (!shooterEnabled && !autoShootMode) {
			bot.driverControl.setShooterOff();
			return;
		}

		bot.driverControl.setShooterVelocity(
				bot.cancelTransferVelo(shooterMaxVelo, bot.transfer.getVelocity())
		);
	}

	void scheduleAutoVelocityAction() {
		if (autoVeloActionStillRunning) return; // ensure that only one velocity update action is dispatched at a time

		autoVeloActionStillRunning = true;

		actions.scheduleAll(
				new SequentialAction(
						bot.requireLimelightRelocalization(new InstantAction(() -> {
							Vector2d botPos = bot.drive.localizer.getPose().position;

							double distanceFromBotCenterToGoalCorner = GameConstants.DECODE.GOAL_POSITION(bot.onBlueTeam).minus(botPos).norm();

							double distanceFromBotToTarget = Math.sqrt(
									distanceFromBotCenterToGoalCorner*distanceFromBotCenterToGoalCorner + GameConstants.DECODE.APPROX_GOAL_HEIGHT_IN *GameConstants.DECODE.APPROX_GOAL_HEIGHT_IN
							);

							shooterMaxVelo = bot.calculateV0ForShooter(distanceFromBotToTarget);
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

	// returns whether or not localization was successful this iteration
	boolean requireLimelightLocalizationSyncIteration() {
		if (bot.use(bot.limelight, bot.drive.localizer)) {
			try {
				bot.limelight.pipelineSwitch(bot.LIMELIGHT_APRILTAG_INDEX);

				LLResult res = bot.limelight.getLatestResult();

				if (!(res.isValid() && res.getPipelineIndex() == bot.LIMELIGHT_APRILTAG_INDEX) || res.getStaleness() > 100) {
					return false;
				}

				if (res.getBotposeTagCount() == 0) return false;

				Pose3D botPose = res.getBotpose(); // always use MegaTag to get robot pose (we cannot rely on the OTOS heading to give us a correct MT2 pose, so we tank the pose ambiguity for now (which actually doesn't seem too bad from the web UI))

				// update RR localizer with MT bot pose
				Position botPos = botPose.getPosition().toUnit(DistanceUnit.INCH);
				YawPitchRollAngles botOrientation = botPose.getOrientation();

				bot.drive.localizer.setPose(new Pose2d(
						botPos.x,
						botPos.y,
						botOrientation.getYaw(AngleUnit.RADIANS)
				));

				bot.drive.localizer.update();

				return true;
			} finally {
				bot.release(bot.limelight, bot.drive.localizer);
			}
		}

		return false;
	}

	double limelightAlignToGoalSyncIteration() {
		if (localizationIsCurrent()) {
				Rotation2d targetRot = GameConstants.DECODE.GOAL_POSITION(bot.onBlueTeam).minus(bot.drive.localizer.getPose().position).angleCast();

				double deltaThetaDeg = Math.toDegrees(targetRot.minus(bot.drive.localizer.getPose().heading));

				if (Math.abs(deltaThetaDeg) < 1) return 0; // don't move if within acceptable error range to avoid back-and-forth swinging

				return deltaThetaDeg / 20; // pure proportional controller for angular velocity
		}

		return 0;
	}

	double getGoalAlignmentErrorDeg() {
		if (localizationIsCurrent()) {
			Rotation2d targetRot = GameConstants.DECODE.GOAL_POSITION(bot.onBlueTeam).minus(bot.drive.localizer.getPose().position).angleCast();

			return Math.toDegrees(targetRot.minus(bot.drive.localizer.getPose().heading));
		}

		return Integer.MAX_VALUE;
	}

	void autoShootModeSyncIteration(Vector2d linearVel) {
		// here kinda start out the same as LL sync iter, but if aligned, then use auto-velocity logic to set velo, then cancel out the linear vels and try to shoot, maybe we have a sensor in here but idk also could use transfer/shooter vels to see if a shot was made or actually just run the transfers + intake and assume you have smt
		// but crucial check must be made: that robot is actually in a legal shooting zone
		// if the LL performed alignment, it should have localized the bot so we can assume that we have a decently accurate localization here if the goal can actually be seen

		boolean alignedToGoal = false;
		double goalAlignmentErrorDeg = getGoalAlignmentErrorDeg();

		// use a more complex algorithm to determine if the bot is (roughly) aligned to the goal +-2 degrees, incorporating linear velocity
		// if the error is large (more than 6 deg), then don't bother
		// otherwise, incorporate linear y velocity

		if (Math.abs(goalAlignmentErrorDeg) < 6) {
			// positive y velocity will be right [roughly clockwise on blue (negative), roughly counterclockwise on red (positive)]
			// negative y velocity will be left [roughly counterclockwise on blue (positive), roughly clockwise on red (negative)]

			// we can then add the velocity to the error via a constant multiplier

			double linearToAngularMultiplier = 4;

			goalAlignmentErrorDeg+=linearVel.y*(bot.onBlueTeam ? -1 : 1)*linearToAngularMultiplier;

			alignedToGoal = Math.abs(goalAlignmentErrorDeg) <= 2;
		}

		if (localizationIsCurrent() && alignedToGoal) {
			// do auto-velocity ourselves because we don't want some weird locking conflicts where auto-velocity hogs locks

			Vector2d botPos = bot.drive.localizer.getPose().position;

			double distanceFromBotCenterToGoalCorner = GameConstants.DECODE.GOAL_POSITION(bot.onBlueTeam).minus(botPos).norm();

			double distanceFromBotToTarget = Math.sqrt(
					distanceFromBotCenterToGoalCorner*distanceFromBotCenterToGoalCorner + GameConstants.DECODE.APPROX_GOAL_HEIGHT_IN *GameConstants.DECODE.APPROX_GOAL_HEIGHT_IN
			);

			double tentativeShooterVelo = bot.calculateV0ForShooter(distanceFromBotToTarget);

			// since the drive is bot-centric, the x component in the linear velocity will always be forwards (same direction as the shooter)
			// because of this, we don't need to do any fancy vector maths to cancel out our linear velocity, we just have to subtract the x component
			// from the tentativeShooterVelo with a constant involved

			shooterMaxVelo = tentativeShooterVelo - linearVel.x*bot.LINEAR_TO_SHOOTER_VELO_CANCEL_MULTIPLIER;

			double realTargetVelo = bot.cancelTransferVelo(shooterMaxVelo, bot.transfer.getVelocity());

			double maxShooterVelocityDeviation = Math.max(
					Math.abs(bot.shooterLeft.getVelocity()-realTargetVelo),
					Math.abs(bot.shooterRight.getVelocity()-realTargetVelo)
			);

			if (maxShooterVelocityDeviation <= 20) { // once we begin to get into the velocity range, start the transfer (don't worry about waiting for it to converge)
				bot.intake.setPower(0.7);
				bot.transfer.setPower(1);
			}

			if (!bot.canShootFromCurrentPosition()) { // do not make illegal or hopeless (too close) shots
				bot.intake.setPower(0);
				bot.transfer.setPower(0);
			}
		} else {
			// either we are not localized or we are no longer facing the goal, do not transfer
			bot.intake.setPower(0);
			bot.transfer.setPower(0);
		}
	}

	void attemptToLocalizeUsingLL() {
		if (requireLimelightLocalizationSyncIteration()) {
			timeOfLastLimelightLocalization = System.currentTimeMillis();
		}
	}

	boolean localizationIsCurrent() {
		long timeSinceLastLocalization = System.currentTimeMillis()-timeOfLastLimelightLocalization;

		return timeSinceLastLocalization <= 30_000;
	}

	void dispatchClearGateIteration(double v) {
		// use value bind fn to make this a "hold" control rather than a press control

		if (v == 0 && clearGateAction != null) {
			bot.release(bot.drive);
			clearGateAction = null;

			return;
		}

		if (clearGateAction == null && localizationIsCurrent() && bot.use(bot.drive)) {
			Pose2d start = GameConstants.DECODE.GATE_CLEAR_START_POSE(bot.onBlueTeam);
			Pose2d end = GameConstants.DECODE.GATE_CLEAR_END_POSE(bot.onBlueTeam);

			clearGateAction = bot.drive.actionBuilder(bot.drive.localizer.getPose())
//					.strafeToLinearHeading(start.position, start.heading)
					.strafeToLinearHeading(end.position, end.heading)
					.build();
		}

		if (clearGateAction != null) {
			boolean done = !clearGateAction.run(new TelemetryPacket());

			if (done) {
				bot.release(bot.drive);
				clearGateAction = null;
			}
		}
	}

	void dispatchSimplyStratIteration(double v) {
		// use value bind fn to make this a "hold" control rather than a press control

		if (v == 0 && clearGateSimplyStratAction != null) {
			bot.release(bot.drive);
			clearGateSimplyStratAction = null;

			return;
		}

		if (clearGateSimplyStratAction == null && localizationIsCurrent() && bot.use(bot.drive)) {
			Pose2d target = GameConstants.DECODE.MONSTER_GATE_CLEAR_POSE(bot.onBlueTeam);

			clearGateSimplyStratAction = bot.drive.actionBuilder(bot.drive.localizer.getPose())
					.strafeToLinearHeading(target.position, target.heading)
					.build();
		}

		if (clearGateSimplyStratAction != null) {
			boolean done = !clearGateSimplyStratAction.run(new TelemetryPacket());

			if (done) {
				bot.release(bot.drive);
				clearGateSimplyStratAction = null;
			}
		}
	}

	void dispatchStrafeToBaseIteration(double v) {
		// use value bind fn to make this a "hold" control rather than a press control

		if (v == 0 && strafeToBaseAction != null) {
			bot.release(bot.drive);
			strafeToBaseAction = null;

			return;
		}

		if (strafeToBaseAction == null && localizationIsCurrent() && bot.use(bot.drive)) {
			Pose2d targetPose = GameConstants.DECODE.BASE_PARKING_POSE(bot.onBlueTeam);

			strafeToBaseAction = bot.drive.actionBuilder(bot.drive.localizer.getPose())
					.strafeToLinearHeading(targetPose.position, targetPose.heading)
					.build();
		}

		if (strafeToBaseAction != null) {
			boolean done = !strafeToBaseAction.run(new TelemetryPacket());

			if (done) {
				bot.release(bot.drive);
				strafeToBaseAction = null;
			}
		}
	}


	@Override
	protected void internalRun() {
		Runnable cancelMacros = () -> {
			actions.clear();
			bot.releaseAllDevices();

			// if macros are canceled and hold macros like autoShootMode are enabled, the user will have to manually disable & re-enable
			// those macros by releasing and pressing their control button in order to ensure that the locks for the required
			// hardware devices are re-acquired
		};

		// GAMEPAD 1 ------------------------------------------------------------------------------------

		keybinder.bind("b").of(gamepad1).to(cancelMacros);
		keybinder.bind("x").of(gamepad1).to(() -> verbose = !verbose);

		keybinder.bind("y").of(gamepad1).to(this::dispatchStrafeToBaseIteration);
		keybinder.bind("right_bumper").of(gamepad1).to(this::dispatchClearGateIteration);
		keybinder.bind("left_bumper").of(gamepad1).to(this::dispatchSimplyStratIteration);


		// GAMEPAD 2 ------------------------------------------------------------------------------------

		keybinder.bind("dpad_up").of(gamepad2).to(this::incrementVeloPreset);
		keybinder.bind("dpad_down").of(gamepad2).to(this::decrementVeloPreset);

		keybinder.bind("right_bumper").of(gamepad2).to((v) -> {
			// use value bind fn to ensure that this binding runs every iteration (so this effectively becomes a hold-button

			// NOTE: we only lock transfer for the duration of the button hold because this is the only device that we directly write to,
			// all other writes are performed externally or separately from autoShootModeSyncIteration()

			if (v != 0) {
				if (!autoShootMode && bot.use(bot.transfer, bot.intake)) {
					headingBeforeAutoShootMode = bot.drive.localizer.getPose().heading;

					autoShootMode = true;
				}
			} else if (autoShootMode) {
				bot.release(bot.transfer, bot.intake);
				autoShootMode = false;
			}
		});
		keybinder.bind("left_bumper").of(gamepad2).to(() -> autoVelocityMode = true);

		keybinder.bind("a").of(gamepad2).to(this::toggleShooterEnabled);
		keybinder.bind("b").of(gamepad2).to(cancelMacros);
		keybinder.bind("x").of(gamepad2).to(() -> actions.scheduleAll(bot.shootThree(shooterMaxVelo)));

		keybinder.bind("dpad_left").of(gamepad2).to(() -> {
			autoVelocityMode = false;

			shooterMaxVelo = Math.max(0, shooterMaxVelo-30);
		});
		keybinder.bind("dpad_right").of(gamepad2).to(() -> {
			autoVelocityMode = false;

			shooterMaxVelo+=30;
		});

		while (opModeIsActive()) {
			boolean holdingGoalAlignment = false;

			/* DRIVE CONTROL */

			Vector2d linearVel = new Vector2d(-gamepad1.inner.left_stick_y,  -gamepad1.inner.left_stick_x);
			double angVel = -gamepad1.inner.right_stick_x;

			if (gamepad1.inner.y) {
				angVel = limelightAlignToGoalSyncIteration();

				holdingGoalAlignment = angVel == 0;
			}

			if (headingBeforeAutoShootMode != null) {
				double deltaTheta = bot.drive.localizer.getPose().heading.minus(headingBeforeAutoShootMode);
				double deltaThetaDeg = Math.toDegrees(deltaTheta);

				if (autoShootMode) {
					angVel = limelightAlignToGoalSyncIteration();
					holdingGoalAlignment = angVel == 0;

					double rotationOffset = angVel*2;

					double rotateBy = - (deltaTheta + rotationOffset);

					double xPrime = linearVel.x * Math.cos(rotateBy) - linearVel.y * Math.sin(rotateBy);
					double yPrime = linearVel.x * Math.sin(rotateBy) + linearVel.y * Math.cos(rotateBy);

					linearVel = new Vector2d(xPrime, yPrime);
				} else if (Math.abs(deltaThetaDeg) >= 2 && angVel == 0) {
					angVel = -deltaThetaDeg/20;

					double xPrime = linearVel.x * Math.cos(deltaTheta) - linearVel.y * Math.sin(deltaTheta);
					double yPrime = linearVel.x * Math.sin(deltaTheta) + linearVel.y * Math.cos(deltaTheta);

					linearVel = new Vector2d(xPrime, yPrime);
				} else {
					headingBeforeAutoShootMode = null;
				}
			}

			bot.driverControl.applyDrivePower(linearVel.x, linearVel.y, angVel);

			lastLinearVel = linearVel;

			/* END DRIVE CONTROL */

			bot.driverControl.setIntakePower(gamepad2.inner.right_trigger-gamepad2.inner.left_trigger); // use a direct call instead of two separate keybind patterns for this to avoid overwrites
			bot.driverControl.setTransferPower(gamepad2.inner.right_stick_y+gamepad2.inner.left_stick_y);

			if (autoShootMode) {
				autoShootModeSyncIteration(linearVel);
			}
			else if (autoVelocityMode) {
				scheduleAutoVelocityAction();
			}

			runShooter();

			attemptToLocalizeUsingLL();

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

			telemetry.addData("linearVel set", "x (%.2f) y (%.2f)", linearVel.x, linearVel.y);

			telemetry.addData(
					"intake group",
					"power (%.2f)",
					bot.intake.getPower()
			);

			telemetry.addData(
					"transfer",
					"power (%.2f)",
					bot.transfer.getPower()
			);

			telemetry.addData(
					"shooter gate",
					"%s",
					bot.shooterGate.inner.getPositionLabel()
			);

			if (autoShootMode) {
				telemetry.addData(
						"FULL AUTO SHOOTING ENABLED",
						"disable auto-shoot mode to see full shooter telemetry"
				);
			}
			else if (shooterEnabled) {
				double realTargetVelo = bot.cancelTransferVelo(shooterMaxVelo, bot.transfer.getVelocity());

				double maxError = Math.max(
						Math.abs(bot.shooterLeft.getVelocity()-realTargetVelo),
						Math.abs(bot.shooterRight.getVelocity()-realTargetVelo)
				);

				boolean canShoot = maxError <= 20;

				telemetry.addData(
						"shooter",
						"velo limit (%.2f) left velo (%.2f) right velo (%.2f) shot type (%s) %s",
						shooterMaxVelo,
						bot.shooterLeft.getVelocity(),
						bot.shooterRight.getVelocity(),
						autoVelocityMode ? "auto" : bot.getShooterVelocityPresetLabel(shooterMaxVelo),
						canShoot ? "READY TO SHOOT" : ""
				);
			} else {
				telemetry.addData(
						"shooter",
						"disabled, velo limit (%.2f) shot type (%s)",
						shooterMaxVelo,
						autoVelocityMode ? "auto" : bot.getShooterVelocityPresetLabel(shooterMaxVelo)
				);
			}

			Pose2d botPose = bot.drive.localizer.getPose();
			telemetry.addData("current position", "x: %.2f in, y: %.2f in, heading: %.2f deg", botPose.position.x, botPose.position.y, Math.toDegrees(botPose.heading.toDouble()));
			telemetry.addData("localization", "%s (%s)", localizationIsCurrent() ? "current" : "stale", holdingGoalAlignment ? "holding goal alignment" : "goal alignment unknown");
			telemetry.addData("current shot zone", GameConstants.DECODE.getShotZoneLabel(botPose.position, bot.RADIUS));

			telemetry.update();
		}
	}

	@Override
	protected IRobot getBot() {
		return bot;
	}
}
