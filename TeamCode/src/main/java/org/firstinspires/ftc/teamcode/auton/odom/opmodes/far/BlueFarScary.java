package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.BlueFarScaryRunner;

@Autonomous(name = "Blue Far [Scary]")
public class BlueFarScary extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueFarScaryRunner().run(this);
	}
}
