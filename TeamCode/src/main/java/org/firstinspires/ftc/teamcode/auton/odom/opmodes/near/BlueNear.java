package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.BlueNearRunner;

@Autonomous(name = "Blue Near [Reg]")
public class BlueNear extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueNearRunner().run(this);
	}
}
