package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.RedFarFrighteningRunner;

@Autonomous(name = "Red Far [Frightening]")
public class RedFarFrightening extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedFarFrighteningRunner().run(this);
	}
}
