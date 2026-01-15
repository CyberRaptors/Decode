package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.RaceAction;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.List;

import lib8812.common.actions.LazyAction;
import lib8812.common.actions.MotorSetVelocityAction;
import lib8812.common.actions.OnceAction;
import lib8812.common.game.GameConstants;

public class ActionableRaptorRobot extends RaptorRobot {

	public final double MAX_VELO_FOR_SPIKE_PICKUP = 20;
	public final VelConstraint SPIKE_PICKUP_VEL_CONSTRAINT = (a, b, c) -> MAX_VELO_FOR_SPIKE_PICKUP;

	public ActionableRaptorRobot(boolean blueTeam) {
		super(blueTeam);
	}

	public Action setIntakePower(double power) {
		return new LockedUsageAction(
				new InstantAction(() -> intake.setPower(power)),
				intake
		);
	}

	public Action setTransferPower(double power) {
		return new LockedUsageAction(
				new InstantAction(() -> transfer.setPower(power)),
				transfer
		);
	}

	public Action setIntakeAndTransferPower(double power) {
		return new SequentialAction(
				setIntakePower(power),
				setTransferPower(power)
		);
	}

	public Action startShootersAsync(double velo) {
		return new LockedUsageAction(
				new InstantAction(() -> {
					shooterRight.setVelocity(velo);
					shooterLeft.setVelocity(velo);
				}),
				shooterRight, shooterLeft
		);
	}

	public Action disableShootersAsync() {
		return new LockedUsageAction(
				new InstantAction(() -> {
					shooterRight.setPower(0);
					shooterLeft.setPower(0);
				}),
				shooterRight, shooterLeft
		);
	}

	public Action shootThree(double velo) {
		return new LockedUsageAction(
				new SequentialAction(
						new MotorSetVelocityAction(shooterRight, velo),
						new MotorSetVelocityAction(shooterLeft, velo),
						new RaceAction(
								new SequentialAction(
										new InstantAction(() -> transfer.setPower(1)),
										new SleepAction(3) // shoot three artifacts in 3s
								),
								telemetryPacket -> {
									// ensure shooter compensates for transfer velocity continuously

									shooterRight.setVelocity(
											cancelTransferVelo(velo, transfer.getVelocity())
									);

									shooterLeft.setVelocity(
											cancelTransferVelo(velo, transfer.getVelocity())
									);

									return true; // always run again, the SequentialAction will finish and end the RaceAction, thus ending this action
								}
						)
				),
				intake, transfer, shooterLeft, shooterRight
		);
	}

	public Action limelightAlignToGoal() {
		return new LockedUsageAction(
				new SequentialAction(
						new InstantAction(() -> limelight.pipelineSwitch(LIMELIGHT_APRILTAG_INDEX)),
						(telemetryPacket) -> {
							LLResult res = limelight.getLatestResult();

							return res.getPipelineIndex() != LIMELIGHT_APRILTAG_INDEX;
						},
						new OnceAction(
								() -> {
									LLResult res = limelight.getLatestResult();

									return res.isValid();
								},
								(telemetryPacket) -> {
									LLResult res = limelight.getLatestResult();

									if (!res.isValid()) return false;

									List<LLResultTypes.FiducialResult> fiducials = res.getFiducialResults();

									if (fiducials.isEmpty()) return false;

									double delX = res.getTx(); // use res.getTx for 3D point-of-interest tracking

									if (Math.abs(delX) < 1) {
										drive.setDrivePowers(
												new PoseVelocity2d(new Vector2d(0, 0), 0)
										);
										return false;
									}

									drive.setDrivePowers(
											new PoseVelocity2d(
													new Vector2d(0, 0),
													- delX / 20 // pure proportional controller
											)
									);

									return true;
								},
								10
						)
				),
				limelight, drive
		);
	}

	public Action requireLimelightRelocalization(Action action, int maxTries) {
		return new LockedUsageAction(
				new SequentialAction(
						new InstantAction(() -> limelight.pipelineSwitch(LIMELIGHT_APRILTAG_INDEX)),
						new OnceAction(
								() -> {
									LLResult res = limelight.getLatestResult();

									if (!(res.isValid() && res.getPipelineIndex() == LIMELIGHT_APRILTAG_INDEX) || res.getStaleness() > 100) {
										return false;
									}

									if (res.getBotposeTagCount() == 0) return false;

									Pose3D botPose = res.getBotpose(); // always use MegaTag to get robot pose (we cannot rely on the OTOS heading to give us a correct MT2 pose, so we tank the pose ambiguity for now (which actually doesn't seem too bad from the web UI))

									// update RR localizer with MT bot pose
									Position botPos = botPose.getPosition().toUnit(DistanceUnit.INCH);
									YawPitchRollAngles botOrientation = botPose.getOrientation();

									drive.localizer.setPose(new Pose2d(
											botPos.x,
											botPos.y,
											botOrientation.getYaw(AngleUnit.RADIANS)
									));

									drive.localizer.update();

									return true;
								},
								action,
								maxTries
						)
				),
				limelight, drive.localizer
		);
	}

	public Action teleOpUnlocalizedStrafeTo(Pose2d pose) {
		// requireLimelightRelocalization auto-locks limelight
		return requireLimelightRelocalization(
				new LockedUsageAction(
						new LazyAction(() -> {
							drive.localizer.update();

							Action move = drive.actionBuilder(drive.localizer.getPose())
									.strafeToSplineHeading(pose.position, pose.heading).build();

							return move;
						}),
						drive
				), -1
		);
	}

	public Action strafeToBase() {
		return new SequentialAction(
				new InstantAction(() -> {
					drive.keepRunningFlag = true; // run PID forever, because this should only be played at the end of a match and can be exited via cancel
				}),
				teleOpUnlocalizedStrafeTo(GameConstants.DECODE.BASE_PARKING_POSE(onBlueTeam))
		);
	}

	public Action localizationEnabledAlignToGoal() {
		return requireLimelightRelocalization(
				new LockedUsageAction(
						new LazyAction(() -> {
							Pose2d currentPose = drive.localizer.getPose();
							Vector2d currentPos = currentPose.position;

							Vector2d goalPos = GameConstants.DECODE.GOAL_POSITION(onBlueTeam);

							Vector2d deltaVector = goalPos.minus(currentPos);

							Rotation2d desiredOrientation = deltaVector.angleCast(); // direction of deltaVector is the desired angle to be exactly goal-facing

							return drive.actionBuilder(currentPose)
									.turnTo(desiredOrientation)
									.build();
						}),
						drive
				), 20
		);
	}
}