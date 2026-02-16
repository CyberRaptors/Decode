package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.BlueNearFrighteningRunner;

@Autonomous(name = "Blue Near [Frightening]")
public class BlueNearFrightening extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueNearFrighteningRunner().run(this);
	}
}
