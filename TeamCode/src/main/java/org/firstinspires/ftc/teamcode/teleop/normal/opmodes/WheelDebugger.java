package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.WheelDebuggerRunner;

@TeleOp(name="WheelDebugger", group="Linear Opmode")
public class WheelDebugger extends LinearOpMode {
    @Override
    public void runOpMode() {
        new WheelDebuggerRunner().run(this);
    }

}
