package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.RedFarTerrifyingRunner;

@Autonomous(name = "Red Far [Terrifying]")
public class RedFarTerrifying extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedFarTerrifyingRunner().run(this);
	}
}
