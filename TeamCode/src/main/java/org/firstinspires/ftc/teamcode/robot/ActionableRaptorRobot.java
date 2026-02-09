package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.MinMax;
import com.acmerobotics.roadrunner.RaceAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.VelConstraint;

import lib8812.common.actions.MotorSetVelocityAction;

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
						new MotorSetVelocityAction(shooterRight, velo),
						new MotorSetVelocityAction(shooterLeft, velo),
						new RaceAction(
								new SequentialAction(
										new InstantAction(() -> {
											intake.setPower(1);
											transfer.setPower(1);
										}),
										new SleepAction(dt) // shoot three artifacts for dt secs
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
						),
						new InstantAction(() -> {
							intake.setPower(0);
							transfer.setPower(0);
						})
				),
				intake, transfer, shooterLeft, shooterRight
		);
	}
}