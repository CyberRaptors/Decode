package org.firstinspires.ftc.teamcode.auton.odomless.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odomless.runners.near.RedNearTripleShotRunner;

@Autonomous(name = "RN???m", group = "BlindTripleShot")
public class RedNearTripleShot extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedNearTripleShotRunner().run(this);
	}
}
