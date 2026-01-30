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

import lib8812.common.game.FieldConstants;
import lib8812.common.game.GameConstants;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.hardwarewrappers.BinaryClaw;
import lib8812.common.robot.hardwarewrappers.SoftwareGangedMotors;
import lib8812.common.robot.hardwarewrappers.VirtualServo;
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

	// s/2 for inscribed circle radius: we don't use 18/2 = 9 here because the wheels are actually closer to 17in
	// and we want to give a buffer to be safe when making shot zone calculations
	public final double RADIUS = 8.25;

	final double SHOOTER_MOTOR_TICKS_PER_REV = 28;
	final double TRANSFER_MOTOR_TICKS_PER_REV = 384.5;

	public final int LIMELIGHT_APRILTAG_INDEX;

	public final double SHOOTER_VELO_FOR_CLOSE_SHOT = 1640;
	public final double SHOOTER_VELO_FOR_MID_SHOT = 1720;
	public final double SHOOTER_VELO_FOR_FAR_SHOT = 1820;

	// auto-only (no preset)
	public final double SHOOTER_VELO_FOR_SHORT_PARK_SHOT = 1680;
	public final double SHOOTER_VELO_FOR_CLOSE_MONSTER_SHOT = SHOOTER_VELO_FOR_MID_SHOT+30;

	public final double TRANSFER_TO_SHOOTER_VELO_CANCEL_MULTIPLIER = SHOOTER_MOTOR_TICKS_PER_REV/TRANSFER_MOTOR_TICKS_PER_REV;
	public final double LINEAR_TO_SHOOTER_VELO_CANCEL_MULTIPLIER = 30;

	public final double[] SHOOTER_VELO_PRESETS = {
			SHOOTER_VELO_FOR_CLOSE_SHOT,
			SHOOTER_VELO_FOR_MID_SHOT,
			SHOOTER_VELO_FOR_FAR_SHOT
	};

	public final double SHOOTER_GATE_OPEN_POS = 0.0;
	public final double SHOOTER_GATE_CLOSED_POS = 0.218;

	public MecanumDrive drive;

	public DcMotor intake;
	public DcMotorEx transfer;
	public SoftwareGangedMotors intakeAndTransfer;
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
				new VirtualServo(), // disable shooterGate to reduce power draw
//				hardwareMap.get(Servo.class, "shooterGate"),
				SHOOTER_GATE_OPEN_POS,
				SHOOTER_GATE_CLOSED_POS
		);

		shooterGate.inner.setDirection(Servo.Direction.REVERSE);
		shooterGate.open();

		transfer.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		transfer.setDirection(DcMotorSimple.Direction.REVERSE);

		intakeAndTransfer = new SoftwareGangedMotors(intake, transfer);
	}

	// returns new shooter velo with transfer velo canceled out
	public double cancelTransferVelo(double shooterVelo, double transferVelo) {
		return shooterVelo - transferVelo*TRANSFER_TO_SHOOTER_VELO_CANCEL_MULTIPLIER;
	}

	// calculates optimal shooter velocity from distance using linear regression, TODO: params need to be updated (v4)
	public double calculateV0ForShooter(double distance) {
		double m = 2.60607;
		double b = 1446.19367;

		return m*distance + b;
	}

	public String getShooterVelocityPresetLabel(double velo) {
		if (velo == SHOOTER_VELO_FOR_CLOSE_SHOT) return "close";
		if (velo == SHOOTER_VELO_FOR_MID_SHOT) return "mid";
		if (velo == SHOOTER_VELO_FOR_FAR_SHOT) return "far";

		return "custom";
	}

	public boolean canShootFromCurrentPosition() {
		// we can shoot from anywhere IF we are not too close to the goal (threshold in inches below)

		double minimumShotDistance = 2*FieldConstants.TILE_LENGTH_IN;

		double distanceFromGoal = GameConstants.DECODE.GOAL_POSITION(onBlueTeam).minus(drive.localizer.getPose().position).norm();

		boolean farEnoughAway = distanceFromGoal >= minimumShotDistance;

		return farEnoughAway && GameConstants.DECODE.isInLegalShootingZone(drive.localizer.getPose().position, RADIUS);
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

		public void setTransferPower(double power) {
			useAndRelease(transfer, () -> transfer.setPower(power));
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
