package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;

import lib8812.common.actions.InitAndPredicateAction;
import lib8812.common.teleop.TeleOpUtils;
import lib8812.common.util.ActionCreator;
import lib8812.common.util.ZeroArgPredicate;

public class ActionableRaptorRobot extends RaptorRobot {
	public Action shootGeneric() {
		if (!use(shooterLeft, shooterRight, railDriveThree)) return null;

		return _shootGeneric();
	}

	public Action _shootGeneric() {
		double defaultVelocityTicksPerSec = SHOOTER_TICKS_PER_REV*60;

		ZeroArgPredicate motorsAreAtShootingVelocity = () -> (
				TeleOpUtils.isApproximatelyEqual(shooterRight.getVelocity(), defaultVelocityTicksPerSec, 100) &&
						TeleOpUtils.isApproximatelyEqual(shooterLeft.getVelocity(), defaultVelocityTicksPerSec, 100)
		);

		return new SequentialAction(
				new InitAndPredicateAction(
						() -> {
							shooterRight.setPower(defaultVelocityTicksPerSec);
							shooterLeft.setPower(defaultVelocityTicksPerSec);
						},
						motorsAreAtShootingVelocity
				),
				new InitAndPredicateAction(
						() -> railDriveThree.setPosition(FEEDER_SHOOT_POS),
						() -> !motorsAreAtShootingVelocity.run() // when the motor velocity decreases, we realize that the ball has shot (as of now, there is unfortunately no other way to confirm this truth)
				),
				new InstantAction(() -> {
					railDriveThree.setPosition(FEEDER_READY_POS);

					release(shooterLeft, shooterRight, railDriveThree);
				})
		);
	}

	public Action feedNext() {
		if (!use(railDriveTwo, railDriveThree)) return null;

		return _feedNext();
	}

	public Action _feedNext() {
		return new SequentialAction(
				new InstantAction(() -> {
					railDriveThree.setPosition(FEEDER_READY_POS);
					railDriveTwo.setPower(1);
				}),
				new SleepAction(1.5),
				new InstantAction(() ->
						release(railDriveTwo, railDriveThree)
				)
		);
	}

	public Action successiveShootGeneric(int ammunition) {
		return successiveShoot(ammunition, this::shootGeneric);
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
