package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.RedNearSixArtifactRunner;

@Autonomous(name = "Red Near SixArtifact Move")
public class RedNearSixArtifact extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedNearSixArtifactRunner().run(this);
	}
}
