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
	public RaptorRobot(boolean blueTeam) {
		onBlueTeam = blueTeam;
	}

	public final int LIMELIGHT_APRILTAG_INDEX = 1;

	public final double SHOOTER_VELO_FOR_CLOSE_SHOT = 1180;
	public final double SHOOTER_VELO_FOR_MID_SHOT = 1375;
	public final double SHOOTER_VELO_FOR_FAR_SHOT = 1565;
	public final double SHOOTER_VELO_FOR_CLOSE_PARALLEL_SHOT = 1280;

	public final double[] SHOOTER_VELO_PRESETS = {
			SHOOTER_VELO_FOR_CLOSE_SHOT,
			SHOOTER_VELO_FOR_MID_SHOT,
			SHOOTER_VELO_FOR_FAR_SHOT
	};

	public final String[] SHOOTER_VELO_PRESET_LABELS = {
			"close",
			"mid",
			"far"
	};

	public final double SHOOTER_VELO_FOR_REJECT = 400;

	public final double RAIL_DRIVE_THREE_MIN_POS = 0.450;
	public final double RAIL_DRIVE_THREE_MAX_POS = 0.842;

	public final double FEEDER_READY_POS = RAIL_DRIVE_THREE_MAX_POS;
	public final double FEEDER_SHOOT_POS = RAIL_DRIVE_THREE_MIN_POS;

	public ArtifactConfiguration artifactConfiguration = ArtifactConfiguration.PPG.copySelf(); // default auto starting config
	public ArtifactConfiguration storedMotif = null;

	public MecanumDrive drive;

	public DcMotor intakeAndRailDriveOne;
	public CRServo railDriveTwo;
	public Servo railDriveThree; // feeder

	public DcMotorEx shooterLeft;
	public DcMotorEx shooterRight;
	public Limelight3A limelight;

	public final boolean onBlueTeam;

	@Override
	protected void postInit(HardwareMap hardwareMap) {
		drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
		shooterRight.setDirection(DcMotorSimple.Direction.REVERSE);

		railDriveThree.setPosition(FEEDER_READY_POS);

		shooterLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		shooterRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

		limelight.setPollRateHz(50);
		limelight.start();
	}

	public double calculateV0ForV2Shooter(double dist) { // uses linear regression to calculate optimal shot velocity from distance
		// fitted to RaptorRobot v2 shooter, see regression plot for details (may use quadratic fit in the future)

		double m = 3.10947;
		double b = 1042.12244;

		return m*dist + b;
	}

	public String getShooterVelocityPresetLabel(double exactVelocity) {
		for (int i = 0; i < SHOOTER_VELO_PRESETS.length; i++) {
			if (SHOOTER_VELO_PRESETS[i] == exactVelocity) {
				return SHOOTER_VELO_PRESET_LABELS[i];
			}
		}

		// exactVelocity does not match any preset

		return "custom";
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

		public void setIntakeGroupPower(double power) {
			useAndRelease(intakeAndRailDriveOne, () -> intakeAndRailDriveOne.setPower(power));
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

			useAndRelease(railDriveThree, () -> railDriveThree.setPosition(boundedPosition));
		}
	}
}
