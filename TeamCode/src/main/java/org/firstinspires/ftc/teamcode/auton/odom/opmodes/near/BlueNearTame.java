package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.BlueNearTameRunner;

@Autonomous(name = "Blue Near [Tame]")
public class BlueNearTame extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueNearTameRunner().run(this);
	}
}
