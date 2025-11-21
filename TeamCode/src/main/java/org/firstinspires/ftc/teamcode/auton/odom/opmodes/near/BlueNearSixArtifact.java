package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.BlueNearSixArtifactRunner;

@Autonomous(name = "Blue Near SixArtifact Move")
public class BlueNearSixArtifact extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueNearSixArtifactRunner().run(this);
	}
}
