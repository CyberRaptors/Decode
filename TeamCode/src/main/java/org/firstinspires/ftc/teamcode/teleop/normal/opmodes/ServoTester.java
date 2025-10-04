package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.ServoTesterRunner;

@TeleOp(name="ServoTester", group="Linear Opmode")
public class ServoTester extends LinearOpMode {
    @Override
    public void runOpMode() {
        new ServoTesterRunner().run(this);
    }
}