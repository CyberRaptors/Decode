package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.BlueFarMonsterRunner;

@Autonomous(name = "Blue Far [Monster]")
public class BlueFarMonster extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueFarMonsterRunner().run(this);
	}
}
