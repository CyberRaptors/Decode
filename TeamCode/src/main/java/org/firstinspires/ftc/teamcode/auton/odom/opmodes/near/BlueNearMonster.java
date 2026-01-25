package org.firstinspires.ftc.teamcode.auton.odom.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.near.BlueNearMonsterRunner;

@Autonomous(name = "Blue Near [Monster]")
public class BlueNearMonster extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueNearMonsterRunner().run(this);
	}
}
