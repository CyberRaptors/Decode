package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.RedFarScaryRunner;

@Autonomous(name = "Red Far [Scary]")
public class RedFarScary extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedFarScaryRunner().run(this);
	}
}
