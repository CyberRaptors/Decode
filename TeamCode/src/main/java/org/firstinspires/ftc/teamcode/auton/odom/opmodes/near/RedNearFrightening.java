package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.RedNearFrighteningRunner;

@Autonomous(name = "Red Near [Frightening]")
public class RedNearFrightening extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedNearFrighteningRunner().run(this);
	}
}
