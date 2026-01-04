package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.RedFarTameRunner;

@Autonomous(name = "Red Far [Tame]")
public class RedFarTame extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedFarTameRunner().run(this);
	}
}
