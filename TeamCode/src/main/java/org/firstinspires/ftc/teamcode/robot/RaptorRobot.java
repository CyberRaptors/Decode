package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.game.ArtifactConfiguration;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.hardwarewrappers.BinaryClaw;
import lib8812.common.rr.SparkFunOTOSDrive;

public class RaptorRobot extends IMecanumRobot {
	public final double REJECT_GATE_CLOSED = 0;
	public final double REJECT_GATE_OPEN = 0;

	public ArtifactConfiguration artifacts;

	public DcMotor intake;
	public CRServo centralTooth;
	public BinaryClaw rejectGate; // this is not actually a claw but has the same open/close verbs so we stylize it as a BinaryClaw (which is really a LabeledPositionServo)
	public SparkFunOTOSDrive drive;

	@Override
	protected void postInit(HardwareMap hardwareMap) {
		drive = new SparkFunOTOSDrive(hardwareMap, new Pose2d(0, 0, 0));
		rejectGate = new BinaryClaw(
				hardwareMap.get(Servo.class, "rejectGate"),
				REJECT_GATE_OPEN,
				REJECT_GATE_CLOSED
		);
	}
}
