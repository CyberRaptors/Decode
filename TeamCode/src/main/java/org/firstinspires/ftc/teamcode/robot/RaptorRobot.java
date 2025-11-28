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

import org.firstinspires.ftc.teamcode.InteropFields;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.MecanumDrive;

public class RaptorRobot extends IMecanumRobot {
	public RaptorRobot(boolean blueTeam) {
		onBlueTeam = blueTeam;

		if (blueTeam) {
			LIMELIGHT_APRILTAG_INDEX = 1; // has blue goal point-of-interest tracking
		} else {
			LIMELIGHT_APRILTAG_INDEX = 2; // has red goal point-of-interest tracking
		}
	}

	public final int LIMELIGHT_APRILTAG_INDEX;

	public final double SHOOTER_VELO_FOR_CLOSE_SHOT = 0;
	public final double SHOOTER_VELO_FOR_MID_SHOT = 1;
	public final double SHOOTER_VELO_FOR_FAR_SHOT = 2;

	public final double[] SHOOTER_VELO_PRESETS = {
			SHOOTER_VELO_FOR_CLOSE_SHOT,
			SHOOTER_VELO_FOR_MID_SHOT,
			SHOOTER_VELO_FOR_FAR_SHOT
	};

	public MecanumDrive drive;

	public DcMotor intake;
	public DcMotor transfer;
	public CRServo transferHelperOne;
	public CRServo transferHelperTwo;

	public DcMotorEx shooterLeft;
	public DcMotorEx shooterRight;
	public Limelight3A limelight;

	public final boolean onBlueTeam;

	@Override
	protected void postInit(HardwareMap hardwareMap) {
		if (InteropFields.lastKnownPose != null) {
			drive = new MecanumDrive(hardwareMap, InteropFields.lastKnownPose);

			InteropFields.lastKnownPose = null;
		} else {
			drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
		}

		shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);

		shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		limelight.setPollRateHz(50);
		limelight.start();
	}

	// calculates optimal shooter velocity from distance using linear regression
	public double calculateV0ForV2Shooter(double distance) {
		return SHOOTER_VELO_FOR_MID_SHOT;
	}

	public String getShooterVelocityPresetLabel(double velo) {
		if (velo == SHOOTER_VELO_FOR_CLOSE_SHOT) return "close";
		if (velo == SHOOTER_VELO_FOR_MID_SHOT) return "mid";
		if (velo == SHOOTER_VELO_FOR_FAR_SHOT) return "far";

		return "custom";
	}

	public final RaptorRobot.LockingControl driverControl = new RaptorRobot.LockingControl();

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
			useAndRelease(intake, () -> intake.setPower(power));
		}

		public void setTransferPower(double mainPower, double assistPower) {
			if (use(transfer, transferHelperOne, transferHelperTwo)) {
				transfer.setPower(mainPower);

				transferHelperOne.setPower(assistPower);
				transferHelperOne.setPower(assistPower);

				release(transfer, transferHelperOne, transferHelperTwo);
			}
		}

		public void setTransferPower(double power) {
			setTransferPower(power, power);
		}

		public void setShooterVelocity(double velo) {
			if (use(shooterLeft, shooterRight)) {
				shooterLeft.setVelocity(velo);
				shooterRight.setVelocity(velo);

				release(shooterLeft, shooterRight);
			}
		}

		public void setShooterOff() {
			if (use(shooterLeft, shooterRight)) {
				// use setPower because setVelocity may cause the shooter to exhibit brake behavior, which draws more power (we want the shooter to coast to a stop)
				shooterLeft.setPower(0);
				shooterRight.setPower(0);

				release(shooterLeft, shooterRight);
			}
		}
	}
}
