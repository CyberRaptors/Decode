package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

import lib8812.common.actions.OnceAction;
import lib8812.common.game.GameConstants;

public class ActionableRaptorRobot extends RaptorRobot {
	public Action shootWithVelo(double velo) {
		if (!use(shooterLeft, shooterRight, railDriveThree)) return null;

		return new SequentialAction(
				_shootWithVelo(velo),
				new InstantAction(() -> release(shooterLeft, shooterRight, railDriveThree))
		);
	}

	public Action _shootWithVelo(double velo) {
		return new SequentialAction(
				new InstantAction(
						() -> {
							shooterRight.setVelocity(velo);
							shooterLeft.setVelocity(velo);
						}
				),
				new SleepAction(0.8),
//				new WaitUntilFullyAcceleratedAction(shooterLeft),
//				new WaitUntilFullyAcceleratedAction(shooterRight),
				new InstantAction(
						() -> railDriveThree.setPosition(FEEDER_SHOOT_POS)
				),
				new SleepAction(0.6),
//				new WaitUntilMotorVelocityChangedAction(shooterLeft), // when the motor velocity decreases, we realize that the ball has shot (as of now, there is unfortunately no other way to confirm this truth)
				new InstantAction(() -> {
					railDriveThree.setPosition(FEEDER_READY_POS);
				})
		);
	}

	public Action feedNext() {
		if (!use(railDriveTwo, railDriveThree)) return null;

		return new SequentialAction(
				_feedNext(),
				new InstantAction(() ->
						release(railDriveTwo, railDriveThree)
				)
		);
	}

	public Action _feedNext() {
		return new SequentialAction(
				new InstantAction(() -> {
					railDriveThree.setPosition(FEEDER_READY_POS);
					railDriveTwo.setPower(-1);
				}),
				new SleepAction(1.5),
				new InstantAction(() -> railDriveTwo.setPower(0))
		);
	}

	public Action successiveShootWithVelo(int ammunition, double velo) {
		if (!use(railDriveTwo, railDriveThree, shooterLeft, shooterRight)) return null;

		Action[] actionSequence = new Action[ammunition+2];

		for (int i = 0; i < ammunition; i++) {
			actionSequence[i+1] = new SequentialAction(
					// load
					new InstantAction(() -> {
						railDriveTwo.setPower(-1);
					}),
					new SleepAction(1.5),
					new InstantAction(() -> railDriveTwo.setPower(0)),

					// shoot
					new InstantAction(
							() -> railDriveThree.setPosition(FEEDER_SHOOT_POS)
					),
					new SleepAction(0.8),
					new InstantAction(() -> {
						railDriveThree.setPosition(FEEDER_READY_POS);
					})
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

			release(railDriveTwo, railDriveThree, shooterLeft, shooterRight);
		});

		return new SequentialAction(actionSequence);
	}

	public Action reject() {
		if (!use(railDriveOne, railDriveThree, shooterLeft, shooterRight)) return null;

		return new SequentialAction(
				new InstantAction(() -> {
					railDriveThree.setPosition(FEEDER_SHOOT_POS);
					shooterLeft.setVelocity(0);
					shooterRight.setVelocity(0);
				}),
				new SleepAction(0.6),
				new InstantAction(() -> {
					shooterRight.setVelocity(SHOOTER_VELO_FOR_REJECT);
					shooterLeft.setVelocity(SHOOTER_VELO_FOR_REJECT);
					railDriveOne.setPower(-1);
				}),
				new SleepAction(0.4),
				new InstantAction(() -> {
					railDriveThree.setPosition(FEEDER_READY_POS);
				}),
				new SleepAction(1),
				new InstantAction(() -> {
					railDriveOne.setPower(0);
					shooterRight.setVelocity(0);
					shooterLeft.setVelocity(0);

					release(railDriveOne, railDriveThree, shooterLeft, shooterRight);
				})
		);
	}

	// TODO: switch between red/blue
	public Action limelightAlignToGoal(Telemetry telemetry) {
		if (!use(limelight, drive)) return null;

		return new SequentialAction(
				new InstantAction(() -> limelight.pipelineSwitch(LIMELIGHT_APRILTAG_INDEX)),
				(telemetryPacket) -> {
					LLResult res = limelight.getLatestResult();

					telemetry.addData("res", "isValid (%b), pipelineIndex (%d)", res.isValid(), res.getPipelineIndex());

					return res.getPipelineIndex() != LIMELIGHT_APRILTAG_INDEX;
				},
				new OnceAction(
						() -> {
							LLResult res = limelight.getLatestResult();

							return res.isValid();
						},
						(telemetryPacket) -> {
							LLResult res = limelight.getLatestResult();

							telemetry.addData("res", "isValid (%b), pipelineIndex (%d)", res.isValid(), res.getPipelineIndex());

							if (!res.isValid()) return false;

							List<LLResultTypes.FiducialResult> fiducials = res.getFiducialResults();
							for (LLResultTypes.FiducialResult fiducial : fiducials) {
								telemetry.addData("Fiducial", "id (%d)", fiducial.getFiducialId());

								if (fiducial.getFiducialId() == GameConstants.DECODE.GOAL_APRILTAG_ID(true)) {
									double delX = fiducial.getTargetXDegrees();

									telemetry.addData("auto-align delX", delX);


									if (Math.abs(delX) < 2) {
										drive.setDrivePowers(
												new PoseVelocity2d(new Vector2d(0, 0), 0)
										);
										return false;
									}

									drive.setDrivePowers(
											new PoseVelocity2d(
													new Vector2d(0, 0),
													delX / 20 // pure proportional controller
											)
									);

									return true;
								}
							}

							return false;
						},
						10
				),
				new InstantAction(() -> release(limelight, drive))
		);
	}
}
