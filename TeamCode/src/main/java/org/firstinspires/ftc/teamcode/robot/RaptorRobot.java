package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.game.ArtifactConfiguration;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.MecanumDrive;

public class RaptorRobot extends IMecanumRobot {
	public final int LIMELIGHT_APRILTAG_INDEX = 1;

	public final double SHOOTER_TICKS_PER_REV = 28;

	public final double SHOOTER_VELO_FOR_CLOSE_SHOT = 1180									;
	public final double SHOOTER_VELO_FOR_MID_SHOT = 1300;

	public final double SHOOTER_VELO_FOR_REJECT = 400;

	public final double RAIL_DRIVE_THREE_MIN_POS = 0.551;
	public final double RAIL_DRIVE_THREE_MAX_POS = 0.89;

	public final double FEEDER_READY_POS = RAIL_DRIVE_THREE_MAX_POS;
	public final double FEEDER_SHOOT_POS = RAIL_DRIVE_THREE_MIN_POS;

	public final ArtifactConfiguration artifacts = new ArtifactConfiguration();

	public CRServo intakeLeft;
	public CRServo intakeRight;
	public MecanumDrive drive;

	public DcMotor railDriveOne;
	public CRServo railDriveTwo;
	public Servo railDriveThree; // feeder

	public DcMotorEx shooterLeft;
	public DcMotorEx shooterRight;
	public Limelight3A limelight;

	@Override
	protected void postInit(HardwareMap hardwareMap) {
		drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
		intakeLeft.setDirection(DcMotorSimple.Direction.REVERSE);
		shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);
		railDriveThree.setPosition(FEEDER_READY_POS);

		shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		limelight.setPollRateHz(50);
		limelight.start();
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
				intakeRight.setPower(power);

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

		public void setShooterVelocity(double velo) {
			if (use(shooterLeft, shooterRight)) {
				shooterLeft.setVelocity(velo);
				shooterRight.setVelocity(velo);

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
