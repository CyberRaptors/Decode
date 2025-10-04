package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.LimelightOnlyRunner;

@TeleOp(name="LimelightOnly", group="Linear Opmode")
public class LimelightOnly extends LinearOpMode {
    @Override
    public void runOpMode() {
        new LimelightOnlyRunner().run(this);
    }
}