package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.FalconOutreachRunner;

@TeleOp(name="FalconOutreach", group="Linear Opmode")
public class FalconOutreach extends LinearOpMode {
    @Override
    public void runOpMode() {
        new FalconOutreachRunner().run(this);
    }
}
