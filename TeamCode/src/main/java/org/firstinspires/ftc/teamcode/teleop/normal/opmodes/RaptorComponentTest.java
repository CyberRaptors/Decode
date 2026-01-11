package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.RaptorRobot;
import org.firstinspires.ftc.teamcode.teleop.normal.runners.RaptorComponentTestRunner;

@TeleOp(name="Raptor Component Test", group="Linear Opmode")
public class RaptorComponentTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        new RaptorComponentTestRunner(new RaptorRobot(true), this) .run(this);
    }
}
