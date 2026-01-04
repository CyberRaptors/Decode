package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.BlueNearScaryRunner;

@Autonomous(name = "Blue Near [Scary]")
public class BlueNearScary extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueNearScaryRunner().run(this);
	}
}
