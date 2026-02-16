package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.RedNearScaryRunner;

@Autonomous(name = "Red Near [Scary]")
public class RedNearScary extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedNearScaryRunner().run(this);
	}
}
