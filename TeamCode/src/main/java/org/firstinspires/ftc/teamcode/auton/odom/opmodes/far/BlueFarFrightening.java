package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.BlueFarFrighteningRunner;

@Autonomous(name = "Blue Far [Frightening]")
public class BlueFarFrightening extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueFarFrighteningRunner().run(this);
	}
}
