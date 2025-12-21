package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.InteropFields;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.hardwarewrappers.BinaryClaw;
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

	public final double SHOOTER_VELO_FOR_CLOSE_AUTO_SHOT = 1420;

	public final double SHOOTER_VELO_FOR_CLOSE_SHOT = 1440;
	public final double SHOOTER_VELO_FOR_MID_SHOT = 1530;
	public final double SHOOTER_VELO_FOR_FAR_SHOT = 1850;

	public final double[] SHOOTER_VELO_PRESETS = {
			SHOOTER_VELO_FOR_CLOSE_SHOT,
			SHOOTER_VELO_FOR_MID_SHOT,
			SHOOTER_VELO_FOR_FAR_SHOT
	};

	public final double SHOOTER_GATE_OPEN_POS = 0.0;
	public final double SHOOTER_GATE_CLOSED_POS = 0.218;

	public MecanumDrive drive;

	public DcMotor intakeAndTransfer;
	public BinaryClaw shooterGate;

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

		shooterLeft.setDirection(DcMotorSimple.Direction.REVERSE);

		shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		limelight.setPollRateHz(50);
		limelight.start();

		shooterGate = new BinaryClaw(
				hardwareMap.get(Servo.class, "shooterGate"),
				SHOOTER_GATE_OPEN_POS,
				SHOOTER_GATE_CLOSED_POS
		);

		shooterGate.inner.setDirection(Servo.Direction.REVERSE);
		shooterGate.open();
	}

	// calculates optimal shooter velocity from distance using linear regression
	public double calculateV0ForV2Shooter(double distance) {
		double m = 6.25541;
		double b = 933.44041;

		return m*distance + b;
	}

	public String getShooterVelocityPresetLabel(double velo) {
		if (velo == SHOOTER_VELO_FOR_CLOSE_SHOT) return "close";
		if (velo == SHOOTER_VELO_FOR_MID_SHOT) return "mid";
		if (velo == SHOOTER_VELO_FOR_FAR_SHOT) return "far";

		return "custom";
	}

	public boolean isInsideShootingZone() { return false; }

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

		public void setIntakeAndTransferPower(double power) {
			useAndRelease(intakeAndTransfer, () -> intakeAndTransfer.setPower(power));
		}

		public void toggleShooterGate() {
			useAndRelease(shooterGate, () -> shooterGate.toggle());
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
