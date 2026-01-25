package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.BlueFarTerrifyingRunner;

@Autonomous(name = "Blue Far [Terrifying]")
public class BlueFarTerrifying extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueFarTerrifyingRunner().run(this);
	}
}
