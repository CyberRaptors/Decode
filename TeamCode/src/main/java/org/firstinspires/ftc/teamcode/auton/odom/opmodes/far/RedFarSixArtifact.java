package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.RedFarSixArtifactRunner;

@Autonomous(name = "Red Far SixArtifact Move")
public class RedFarSixArtifact extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedFarSixArtifactRunner().run(this);
	}
}
