package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import lib8812.common.robot.IRobot;

public class ServoTesterBot extends IRobot {
	final static String SERVO_NAME = "shooterGate";

	public Servo servo;

	@Override
	public void init(HardwareMap hardwareMap) {
		servo = loadDevice(hardwareMap, Servo.class, SERVO_NAME);
		servo.resetDeviceConfigurationForOpMode();

		servo.setPosition(0);
	}
}
