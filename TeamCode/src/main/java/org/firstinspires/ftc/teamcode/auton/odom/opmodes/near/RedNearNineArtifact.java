package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.RedNearNineArtifactRunner;

@Autonomous(name = "Red Near NineArtifact Move")
public class RedNearNineArtifact extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedNearNineArtifactRunner().run(this);
	}
}
