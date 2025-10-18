package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;

public class ActionableRaptorRobot extends RaptorRobot {
	public Action shootWithPower(double power) {
		if (!use(shooterLeft, shooterRight, railDriveThree)) return null;

		return new SequentialAction(
				_shootWithPower(power),
				new InstantAction(() -> release(shooterLeft, shooterRight, railDriveThree))
		);
	}

	public Action _shootWithPower(double power) {
		return new SequentialAction(
				new InstantAction(
						() -> {
							shooterRight.setPower(power);
							shooterLeft.setPower(power);
						}
				),
				new SleepAction(0.3),
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
					railDriveTwo.setPower(1);
				}),
				new SleepAction(1.5),
				new InstantAction(() -> railDriveTwo.setPower(0))
		);
	}

	public Action successiveShootWithPower(int ammunition, double power) {
		if (!use(railDriveTwo, railDriveThree, shooterLeft, shooterRight)) return null;

		Action[] actionSequence = new Action[ammunition+2];

		for (int i = 0; i < ammunition; i++) {
			actionSequence[i+1] = new SequentialAction(
					// load
					new InstantAction(() -> {
						railDriveTwo.setPower(1);
					}),
					new SleepAction(1.5),
					new InstantAction(() -> railDriveTwo.setPower(0)),

					// shoot
					new InstantAction(
							() -> railDriveThree.setPosition(FEEDER_SHOOT_POS)
					),
					new SleepAction(0.6),
					new InstantAction(() -> {
						railDriveThree.setPosition(FEEDER_READY_POS);
					})
			);
		}

		actionSequence[0] = new InstantAction(() -> {
			railDriveThree.setPosition(FEEDER_READY_POS);
			shooterLeft.setPower(power);
			shooterRight.setPower(power);
		});

		actionSequence[actionSequence.length-1] = new InstantAction(() -> {
			shooterLeft.setPower(0);
			shooterRight.setPower(0);

			release(railDriveTwo, railDriveThree, shooterLeft, shooterRight);
		});

		return new SequentialAction(actionSequence);
	}
}
