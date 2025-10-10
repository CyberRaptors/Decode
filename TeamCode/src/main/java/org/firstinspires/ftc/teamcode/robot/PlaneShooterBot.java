package org.firstinspires.ftc.teamcode.robot;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import lib8812.common.robot.IMecanumRobot;
import lib8812.common.robot.hardwarewrappers.BinaryClaw;
import lib8812.common.rr.SparkFunOTOSDrive;

public class PlaneShooterBot extends IMecanumRobot {
    public SparkFunOTOSDrive drive;
    public BinaryClaw planeShooter;
    public final double SHOOT = 0.5;
    public final double REST = 0.0;

    @Override
    public void postInit(HardwareMap hardwareMap) {
        drive = new SparkFunOTOSDrive(hardwareMap, new Pose2d(0, 0, 0));
        planeShooter = new BinaryClaw(
                hardwareMap.get(Servo.class, "planeShooter"),
                SHOOT,
                REST
        );
    }
}
