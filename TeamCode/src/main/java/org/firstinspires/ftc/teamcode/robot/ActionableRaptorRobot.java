package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import java.util.List;

import lib8812.common.actions.LazyAction;
import lib8812.common.actions.OnceAction;
import lib8812.common.game.GameConstants;

public class ActionableRaptorRobot extends RaptorRobot {
	public ActionableRaptorRobot(boolean blueTeam) {
		super(blueTeam);
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