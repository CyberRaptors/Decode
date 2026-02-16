package org.firstinspires.ftc.teamcode.auton.odom.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odom.runners.far.RedFarRunner;

@Autonomous(name = "Red Far [Reg]")
public class RedFar extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedFarRunner().run(this);
	}
}
