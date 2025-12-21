package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
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
import lib8812.common.game.GameConstants;

public class ActionableRaptorRobot extends RaptorRobot {

	public final double MAX_VELO_FOR_SPIKE_PICKUP = 10;
	public final VelConstraint SPIKE_PICKUP_VEL_CONSTRAINT = (a, b, c) -> MAX_VELO_FOR_SPIKE_PICKUP;

	public ActionableRaptorRobot(boolean blueTeam) {
		super(blueTeam);
	}

	public Action setIntakeAndTransferPower(double power) {
		return new LockedUsageAction(
				new InstantAction(() -> intakeAndTransfer.setPower(power)),
				intakeAndTransfer
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
		double secondShotVelo = velo*(5.0/6);
		double thirdShotVelo = velo*(3.0/4);

		return new LockedUsageAction(
				new SequentialAction(
						new MotorSetVelocityAction(shooterRight, velo),
						new MotorSetVelocityAction(shooterLeft, velo),

						new InstantAction(() -> {
							shooterGate.open();
							intakeAndTransfer.setPower(1);
						}),
						new SleepAction(0.3),
						new InstantAction(() -> intakeAndTransfer.setPower(0)),
						new SleepAction(0.2),
						new InstantAction(() -> intakeAndTransfer.setPower(1)),
						new SleepAction(0.3),
						new InstantAction(() -> intakeAndTransfer.setPower(0)),
						new SleepAction(0.2),
						new InstantAction(() -> intakeAndTransfer.setPower(1)),
						new SleepAction(0.3),
						new InstantAction(() -> intakeAndTransfer.setPower(0))
				),
				intakeAndTransfer, shooterLeft, shooterRight, shooterGate
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

									Pose3D botPose = res.getBotpose(); // always use MegaTag to get robot pose (we cannot rely on the OTOS heading to give us a correct MT2 pose)

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