package org.firstinspires.ftc.teamcode.auton.odomless.runners.right;

import org.firstinspires.ftc.teamcode.robot.FalconRobot;

import lib8812.common.auton.OdomlessUtil;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class FalconOutreachRightParkRunner extends ITeleOpRunner {
		final FalconRobot bot = new FalconRobot();
		final OdomlessUtil util = new OdomlessUtil(bot, this::sleep);

		protected IMecanumRobot getBot() {
			return bot;
		}

		protected void internalRun() {
			util.init();
			util.moveSync(500, -0.25);
			util.strafeSync(750, -1);
		}
}
