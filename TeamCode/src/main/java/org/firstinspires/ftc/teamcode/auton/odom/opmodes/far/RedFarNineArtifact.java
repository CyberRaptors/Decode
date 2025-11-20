package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.RedFarNineArtifactRunner;

@Autonomous(name = "Red Far NineArtifact Move")
public class RedFarNineArtifact extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedFarNineArtifactRunner().run(this);
	}
}
