package org.firstinspires.ftc.teamcode.auton.odomless.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odomless.runners.near.BlueNearTripleShotRunner;

@Autonomous(name = "BN???m", group = "BlindTripleShot")
public class BlueNearTripleShot extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueNearTripleShotRunner().run(this);
	}
}
