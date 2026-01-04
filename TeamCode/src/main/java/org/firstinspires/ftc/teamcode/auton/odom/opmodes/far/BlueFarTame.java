package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.BlueFarTameRunner;

@Autonomous(name = "Blue Far [Tame]")
public class BlueFarTame extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueFarTameRunner().run(this);
	}
}
