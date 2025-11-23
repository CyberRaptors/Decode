package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.BlueFarSixArtifactMidShotRunner;

@Autonomous(name = "Blue Far SixArtifact Move")
public class BlueFarSixArtifact extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueFarSixArtifactMidShotRunner().run(this);
	}
}
