package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.List;

import lib8812.common.actions.LazyAction;
import lib8812.common.actions.MotorSetVelocityAction;
import lib8812.common.actions.OnceAction;
import lib8812.common.game.ArtifactConfiguration;
import lib8812.common.game.GameConstants;

public class ActionableRaptorRobot extends RaptorRobot {
	public ActionableRaptorRobot(boolean blueTeam) {
		super(blueTeam);
	}


	public final double MAX_ROBOT_VELO_FOR_SPIKE_PICKUP = 100;
	public final VelConstraint SPIKE_PICKUP_VEL_CONSTRAINT = (pose2dDual, posePath, v) -> MAX_ROBOT_VELO_FOR_SPIKE_PICKUP;

	public final double MAX_ROBOT_VELO_FOR_FAST_SPIKE_PICKUP = 100;
	public final VelConstraint FAST_SPIKE_PICKUP_VEL_CONSTRAINT = (pose2dDual, posePath, v) -> MAX_ROBOT_VELO_FOR_FAST_SPIKE_PICKUP;

	public Action wiggleFeeder(int iterations) {
		Action[] actions = new Action[iterations*4];

		for (int i = 0; i < actions.length; i+=4) {
			actions[i] = new InstantAction(() -> railDriveThree.setPosition(FEEDER_READY_POS));
			actions[i+1] = new SleepAction(0.2);
			actions[i+2] = new InstantAction(() -> railDriveThree.setPosition(FEEDER_READY_POS-FEEDER_WIGGLE_DISTANCE));
			actions[i+3] = new SleepAction(0.2);
		}

		return new SequentialAction(actions);
	}

	public Action setRailDriveTwoPower(double power) {
		return new LockedUsageAction(
				new InstantAction(() -> railDriveTwo.setPower(power)),
				railDriveTwo
		);
	}	

	public Action setIntakeGroupPower(double power) {
		return new LockedUsageAction(
				new InstantAction(() -> intakeAndRailDriveOne.setPower(power)),
				intakeAndRailDriveOne
		);
	}

	public Action disableShootersAsync() {
		return new LockedUsageAction(
				new InstantAction(() -> {
					shooterLeft.setPower(0);
					shooterRight.setPower(0);
				}),
				shooterLeft, shooterRight
		);
	}

	public Action shootWithVelo(double velo) {
		return new LockedUsageAction(
				new SequentialAction(
						new ParallelAction(
								new MotorSetVelocityAction(shooterLeft, velo, 175),
								new MotorSetVelocityAction(shooterRight, velo, 175)
						),
						new InstantAction(
								() -> railDriveThree.setPosition(FEEDER_SHOOT_POS)
						),
						new SleepAction(0.7),
						new InstantAction(() -> railDriveThree.setPosition(FEEDER_READY_POS))
				),
				shooterLeft, shooterRight, railDriveThree
		);
	}

	public Action setShooterVelocityAsync(double velo) {
		return new LockedUsageAction(
				new InstantAction(() -> {
					shooterLeft.setVelocity(velo);
					shooterRight.setVelocity(velo);
				}),
				shooterLeft, shooterRight
		);
	}

	public Action feedNext(double runTime) {
		return new LockedUsageAction(
				new SequentialAction(
						new InstantAction(() -> {
							railDriveThree.setPosition(FEEDER_READY_POS);
							railDriveTwo.setPower(-1);
						}),
						new SleepAction(runTime),
						new InstantAction(() -> railDriveTwo.setPower(0))
				),
				railDriveTwo, railDriveThree
		);
	}

	public Action successiveShootWithVelo(int ammunition, double velo) {
		Action[] actionSequence = new Action[ammunition+2];

		for (int i = 0; i < ammunition; i++) {
			actionSequence[i+1] = new SequentialAction(
					// load
					new InstantAction(() -> railDriveTwo.setPower(-1.0)),
					new SleepAction(1.7),
					new InstantAction(() -> railDriveTwo.setPower(0)),

					// shoot
					new InstantAction(
							() -> railDriveThree.setPosition(FEEDER_SHOOT_POS)
					),
					new SleepAction(0.6),
					new InstantAction(() -> railDriveThree.setPosition(FEEDER_READY_POS)),
					new SleepAction(0.2)
			);
		}

		actionSequence[0] = new InstantAction(() -> {
			railDriveThree.setPosition(FEEDER_READY_POS);
			shooterLeft.setVelocity(velo);
			shooterRight.setVelocity(velo);
		});

		actionSequence[actionSequence.length-1] = new InstantAction(() -> {
			shooterLeft.setPower(0);
			shooterRight.setPower(0);
		});

		return new LockedUsageAction(
				new SequentialAction(actionSequence),
				railDriveTwo, railDriveThree, shooterLeft, shooterRight
		);
	}

	public Action reject() {
		return new LockedUsageAction(
				new SequentialAction(
					new InstantAction(() -> {
						railDriveThree.setPosition(FEEDER_SHOOT_POS);
						shooterLeft.setVelocity(0);
						shooterRight.setVelocity(0);
					}),
					new SleepAction(0.6),
					new InstantAction(() -> {
						shooterRight.setVelocity(SHOOTER_VELO_FOR_REJECT);
						shooterLeft.setVelocity(SHOOTER_VELO_FOR_REJECT);
						intakeAndRailDriveOne.setPower(-1);
					}),
					new SleepAction(0.4),
					new InstantAction(() -> railDriveThree.setPosition(FEEDER_READY_POS)),
					new SleepAction(1),
					new InstantAction(() -> {
						intakeAndRailDriveOne.setPower(0);
						shooterRight.setVelocity(0);
						shooterLeft.setVelocity(0);
					})
				),
				intakeAndRailDriveOne, railDriveThree, shooterLeft, shooterRight
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

	public Action storeMotif() {
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

									for (LLResultTypes.FiducialResult fiducial : fiducials) {
										ArtifactConfiguration motif = GameConstants.DECODE.getMotifFromAprilTagId(fiducial.getFiducialId());

										if (motif == null) continue;

										storedMotif = motif.copySelf();

										return false;
									}

									return false;
								},
								10
						)
				),
				limelight
		);
	}

	public Action sortToMotif() {
		return new OnceAction(
				() -> storedMotif != null,
				new LazyAction(() -> {
					int delta = artifactConfiguration.calculateForwardRotatesNeeded(storedMotif);

					Action[] rejects = new Action[delta*2];

					for (int i = 0; i < rejects.length; i+=2) {
						rejects[i] = reject();
						rejects[i+1] = feedNext(1.5);
					}

					return new SequentialAction(rejects);
				}),
				10
		);
	}

	public Action setInternalArtifactConfig(ArtifactConfiguration config) {
		return new InstantAction(() -> artifactConfiguration = config.copySelf());
	}

	public Action requireLimelightRelocalization(Action action, int maxTries) {
		return new LockedUsageAction(
				new SequentialAction(
						new InstantAction(() -> limelight.pipelineSwitch(LIMELIGHT_APRILTAG_INDEX)),
						new OnceAction(
								() -> {
									// in case we can use MT2
									double otosYawDegrees = Math.toDegrees(drive.localizer.getPose().heading.toDouble());
									limelight.updateRobotOrientation(otosYawDegrees);

									LLResult res = limelight.getLatestResult();

									if (!(res.isValid() && res.getPipelineIndex() == LIMELIGHT_APRILTAG_INDEX) || res.getStaleness() > 100) {
										return false;
									}

									Pose3D botPose;

									int tagsUsedForLocalization = res.getBotposeTagCount();

									if (tagsUsedForLocalization > 1) { // use MegaTag to get robot pose (multiple tags allow zero-ambiguity localization)
										botPose = res.getBotpose();
									} else if (tagsUsedForLocalization == 1) { // we can only see one tag, use MegaTag2 with the orientation from the OTOS to get an unambiguous pose
										botPose = res.getBotpose_MT2();
									} else { // no tags, can't localize
										return false;
									}

									// update RR localizer with MT bot pose
									Position botPos = botPose.getPosition();
									YawPitchRollAngles botOrientation = botPose.getOrientation();

									drive.localizer.setPose(new Pose2d(
											botPos.x,
											botPos.y,
											botOrientation.getYaw(AngleUnit.RADIANS)+Math.PI // limelight coordinate system offset
									));

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
						new LazyAction(() -> drive.actionBuilder(drive.localizer.getPose()).strafeToSplineHeading(pose.position, pose.heading).build()),
						drive
				), -1
		);
	}

	public Action strafeToBase() {
		return teleOpUnlocalizedStrafeTo(GameConstants.DECODE.BASE_PARKING_POSE(onBlueTeam));
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
