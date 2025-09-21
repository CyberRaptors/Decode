package org.firstinspires.ftc.teamcode.robot;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import lib8812.common.robot.IMecanumRobot;

public class LimelightOnlyBot extends IMecanumRobot {
    public Limelight3A limelight;

    public void postInit() {
        limelight.setPollRateHz(100);
        limelight.start();
    }
}
