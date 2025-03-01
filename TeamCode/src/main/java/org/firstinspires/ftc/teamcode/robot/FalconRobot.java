package org.firstinspires.ftc.teamcode.robot;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.MecanumDrive;

public class FalconRobot extends IMecanumRobot {
	public double PLANE_SHOT = 0.210  ;
	public double PLANE_READY = 0.515;

	public DcMotor leftFront;
	public DcMotor rightFront;
	public DcMotor leftBack;
	public DcMotor rightBack;

	public DcMotor lift;
	public CRServo intake;
	public Servo planeShooter;

	public MecanumDrive drive;

	public void postInit(HardwareMap hardwareMap) {
		drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

		leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
		leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
		rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
		rightBack.setDirection(DcMotorSimple.Direction.FORWARD);

		planeShooter.setPosition(PLANE_READY);
	}
}
