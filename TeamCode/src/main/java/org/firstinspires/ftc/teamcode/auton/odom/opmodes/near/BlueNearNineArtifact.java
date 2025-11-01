package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.BlueNearNineArtifactRunner;

@Autonomous(name = "Blue Near NineArtifact Move", group = "MoveOnly")
public class BlueNearNineArtifact extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueNearNineArtifactRunner().run(this);
	}
}
