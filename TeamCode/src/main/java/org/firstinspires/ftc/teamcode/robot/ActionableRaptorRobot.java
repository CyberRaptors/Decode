package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.MinMax;
import com.acmerobotics.roadrunner.NullAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.VelConstraint;
import com.qualcomm.hardware.limelightvision.LLResult;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

import lib8812.common.actions.MotorSetVelocityAction;
import lib8812.common.actions.OnceAction;

public class ActionableRaptorRobot extends RaptorRobot {
	public final double MAX_VELO_FOR_SPIKE_PICKUP = 20;
	public final VelConstraint SPIKE_PICKUP_VEL_CONSTRAINT = (a, b, c) -> MAX_VELO_FOR_SPIKE_PICKUP;

	public final double MAX_VELO_FOR_MONSTER_LZ_PICKUP = 60;
	public final VelConstraint MONSTER_LZ_PICKUP_VEL_CONSTRAINT = (a, b, c) -> MAX_VELO_FOR_MONSTER_LZ_PICKUP;


	public final MinMax ACCEL_FOR_MONSTER_LZ_PICKUP = new MinMax(-80, 80);
	public final AccelConstraint MONSTER_LZ_PICKUP_ACCEL_CONSTRAINT = (a, b, c) -> ACCEL_FOR_MONSTER_LZ_PICKUP;

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
		return shootBase(velo, 2);
	}

	public Action shootTwo(double velo){
		return shootBase(velo, 1);
	}

	public Action shootThreeSlow(double velo) {
		return new SequentialAction(
				shootBase(velo, 0.8),
				new SleepAction(0.5),
				shootBase(velo, 0.5)
		);
	}

	public Action shootTwoSlow(double velo) {
		return shootBase(velo, 2);
	}

	public Action shootOne(double velo) {
		return shootBase(velo, 0.5);
	}

	Action shootBase(double velo, double dt) {
		return new LockedUsageAction(
				new SequentialAction(
						new MotorSetVelocityAction(shooterRight, velo, 40),
						new MotorSetVelocityAction(shooterLeft, velo, 40),
						new InstantAction(() -> {
							intake.setPower(1);
							transfer.setPower(1);
						}),
						new SleepAction(dt),
						new InstantAction(() -> {
							intake.setPower(0);
							transfer.setPower(0);
						})
				),
				intake, transfer, shooterLeft, shooterRight
		);
	}

	public Action relocalize() {
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
								new NullAction(),
								20
						)
				),
				limelight, drive.localizer
		);
	}
}