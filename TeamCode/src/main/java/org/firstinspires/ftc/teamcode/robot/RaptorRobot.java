package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.game.ArtifactConfiguration;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.hardwarewrappers.BinaryClaw;
import lib8812.common.rr.SparkFunOTOSDrive;

public class RaptorRobot extends IMecanumRobot {
	public final double REJECT_GATE_CLOSED = 0;
	public final double REJECT_GATE_OPEN = 0;

	public final double INTAKE_RIGHT_POWER_MULTIPLIER = 0.7;

	public final double RAIL_DRIVE_THREE_MIN_POS = 0.551;
	public final double RAIL_DRIVE_THREE_MAX_POS = 0.89;

	public final ArtifactConfiguration artifacts = new ArtifactConfiguration();

	public CRServo intakeLeft;
	public CRServo intakeRight;
	public BinaryClaw rejectGate; // this is not actually a claw but has the same open/close verbs so we stylize it as a BinaryClaw (which is really a LabeledPositionServo)
	public SparkFunOTOSDrive drive;

	public DcMotor railDriveOne;
	public CRServo railDriveTwo;
	public Servo railDriveThree;

	public DcMotor shooterLeft;
	public DcMotor shooterRight;

	@Override
	protected void postInit(HardwareMap hardwareMap) {
		drive = new SparkFunOTOSDrive(hardwareMap, new Pose2d(0, 0, 0));
		rejectGate = new BinaryClaw(
				hardwareMap.get(Servo.class, "rejectGate"),
				REJECT_GATE_OPEN,
				REJECT_GATE_CLOSED
		);

		intakeLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
		railDriveThree.setPosition(RAIL_DRIVE_THREE_MAX_POS);
	}

	public final LockingControl driverControl = new LockingControl();

	public class LockingControl {
		public void applyDrivePower(double forwardPower, double strafePower, double angularPower) {
			useAndRelease(drive, () -> drive.setDrivePowers(new PoseVelocity2d(
					new Vector2d(
							forwardPower,
							strafePower
					),
					angularPower
			)));
		}

		public void setIntakePower(double power) {
			if (use(intakeLeft, intakeRight)) {
				intakeLeft.setPower(power);
				intakeRight.setPower(power*INTAKE_RIGHT_POWER_MULTIPLIER);

				release(intakeLeft, intakeRight);
			}
		}

		public void setShooterPower(double power) {
			if (use(shooterLeft, shooterRight)) {
				shooterLeft.setPower(power);
				shooterRight.setPower(power);

				release(shooterLeft, shooterRight);
			}
		}

		public void setRailDriveOnePower(double power) {
			useAndRelease(railDriveOne, () -> railDriveOne.setPower(power));
		}

		public void setRailDriveTwoPower(double power) {
			useAndRelease(railDriveTwo, () -> railDriveTwo.setPower(power));
		}

		public void setRailDriveThreePosition(double position) {
			double boundedPosition = Math.max(
					RAIL_DRIVE_THREE_MIN_POS,
					Math.min(
							RAIL_DRIVE_THREE_MAX_POS,
							position
					)
			);

			useAndRelease(railDriveThree, () -> {
				railDriveThree.setPosition(boundedPosition);
			});
		}
	}
}
