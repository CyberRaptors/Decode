package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.BlueFarRunner;

@Autonomous(name = "Blue Far [Reg]")
public class BlueFar extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueFarRunner().run(this);
	}
}
