package org.firstinspires.ftc.teamcode.auton.odomless.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odomless.runners.near.RedNearMoveOnlyRunner;

@Autonomous(name = "RNM", group = "MoveOnly")
public class RedNearMoveOnly extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new RedNearMoveOnlyRunner().run(this);
	}
}
