package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.RaptorMainRunner;

@TeleOp(name="RaptorMain [Red]", group="Linear Opmode")
public class RaptorMainRed extends LinearOpMode {
    @Override
    public void runOpMode() {
        new RaptorMainRunner(false).run(this);
    }
}