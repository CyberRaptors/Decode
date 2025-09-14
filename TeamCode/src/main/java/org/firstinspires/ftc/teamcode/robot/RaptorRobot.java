package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.SparkFunOTOSDrive;

public class RaptorRobot extends IMecanumRobot {
//	public DcMotor leftFlywheel;
//	public DcMotor rightFlywheel;
//
//	public DcMotor intake;
//
//	public DcMotor pipelineDriveOne;
//	public DcMotor pipelineDriveTwo;
//
//	public LabeledPositionServo cannonFeeder;
//
//	public double FEEDER_OPEN = 0;
//	public double FEEDER_CHAMBER_LOADED = 0;
//	public double FEEDER_FIRE_ROUND = 0;

	public SparkFunOTOSDrive drive;

	@Override
	protected void postInit(HardwareMap hardwareMap) {
//		intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//
//		cannonFeeder = new LabeledPositionServo(
//				loadDevice(hardwareMap, Servo.class, "cannonFeeder"),
//				new String[] { "open", "loaded", "push" },
//				new Double[] { FEEDER_OPEN, FEEDER_CHAMBER_LOADED, FEEDER_FIRE_ROUND }
//		);

		drive = new SparkFunOTOSDrive(hardwareMap, new Pose2d(0, 0, 0));
	}
}
