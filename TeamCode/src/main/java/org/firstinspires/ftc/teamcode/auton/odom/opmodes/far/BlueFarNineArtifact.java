package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.BlueFarNineArtifactRunner;

@Autonomous(name = "Blue Far NineArtifact Move")
public class BlueFarNineArtifact extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueFarNineArtifactRunner().run(this);
	}
}
