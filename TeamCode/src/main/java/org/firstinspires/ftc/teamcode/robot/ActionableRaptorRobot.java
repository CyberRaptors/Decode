package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import java.util.List;

import lib8812.common.actions.OnceAction;
import lib8812.common.game.GameConstants;

public class ActionableRaptorRobot extends RaptorRobot {
	public ActionableRaptorRobot() {
		this(true);
	}

	public ActionableRaptorRobot(boolean blueTeam) {
		super(blueTeam);
	}

	public Action shootWithVelo(double velo) {
		return new LockedUsageAction(
				_shootWithVelo(velo),
				shooterLeft, shooterRight, railDriveThree
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
				new InstantAction(
						() -> railDriveThree.setPosition(FEEDER_SHOOT_POS)
				),
				new SleepAction(0.6),
				new InstantAction(() -> {
					railDriveThree.setPosition(FEEDER_READY_POS);
				})
		);
	}

	public Action feedNext() {
		return new LockedUsageAction(
				_feedNext(),
				railDriveTwo, railDriveThree
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
					})
				),
				railDriveOne, railDriveThree, shooterLeft, shooterRight
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

								for (LLResultTypes.FiducialResult fiducial : fiducials) {
									if (fiducial.getFiducialId() == GameConstants.DECODE.GOAL_APRILTAG_ID(onBlueTeam)) {
										double delX = fiducial.getTargetXDegrees();

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
					)
				),
				limelight, drive
		);
	}
}
