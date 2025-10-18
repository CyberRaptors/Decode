package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;

import lib8812.common.util.ActionCreator;

public class ActionableRaptorRobot extends RaptorRobot {
	public Action shootGeneric(double power) {
		if (!use(shooterLeft, shooterRight, railDriveThree)) return null;

		return new SequentialAction(
				_shootGeneric(power),
				new InstantAction(() -> release(shooterLeft, shooterRight, railDriveThree))
		);
	}

	public Action _shootGeneric(double power) {
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
				new SleepAction(1.5)
		);
	}

	public Action successiveShootGeneric(int ammunition, int power) {
		return successiveShoot(ammunition, () -> _shootGeneric(power));
	}

	public Action successiveShoot(int ammunition, ActionCreator unlockedShootAction) {
		if (!use(railDriveTwo, railDriveThree, shooterLeft, shooterRight)) return null;

		Action[] shots = new Action[ammunition*2];

		for (int i = 0; i < ammunition; i++) {
			shots[i] = _feedNext();
			shots[i+1] = unlockedShootAction.run();
		}

		return new SequentialAction(shots);
	}
}
