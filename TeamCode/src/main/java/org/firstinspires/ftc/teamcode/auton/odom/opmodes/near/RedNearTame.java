package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.RedNearTameRunner;

@Autonomous(name = "Red Near [Tame]")
public class RedNearTame extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedNearTameRunner().run(this);
	}
}
