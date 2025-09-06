package org.firstinspires.ftc.teamcode.auton.odomless.opmodes.right;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.auton.odomless.runners.right.FalconOutreachRightParkRunner;

@TeleOp(name="FalconOutreachRightPark", group="Linear Opmode")
public class FalconOutreachRightPark extends LinearOpMode {
	@Override
	public void runOpMode() {
		new FalconOutreachRightParkRunner().run(this);
	}
}
