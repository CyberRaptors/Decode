package org.firstinspires.ftc.teamcode.auton.odomless.opmodes.far;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odomless.runners.far.FarMoveOnlyRunner;

@Autonomous(name = "-Fm", group = "MoveOnly")
public class FarMoveOnly extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new FarMoveOnlyRunner().run(this);
	}
}
