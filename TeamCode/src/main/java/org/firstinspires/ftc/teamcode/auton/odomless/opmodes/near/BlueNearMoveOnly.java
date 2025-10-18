package org.firstinspires.ftc.teamcode.auton.odomless.opmodes.near;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.auton.odomless.runners.near.BlueNearMoveOnlyRunner;

@Autonomous(name = "BNm", group = "MoveOnly")
public class BlueNearMoveOnly extends LinearOpMode {
	@Override
	public void runOpMode() throws InterruptedException {
		new BlueNearMoveOnlyRunner().run(this);
	}
}
