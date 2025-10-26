package org.firstinspires.ftc.teamcode.robot;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import lib8812.common.robot.IMecanumRobot;
import lib8812.common.rr.MecanumDrive;

public class LimelightOnlyBot extends IMecanumRobot {
    public Limelight3A limelight;
    public MecanumDrive drive;

    public void postInit(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(1);
        limelight.start();

        drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
    }
}
