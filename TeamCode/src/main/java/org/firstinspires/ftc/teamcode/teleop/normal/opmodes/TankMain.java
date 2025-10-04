package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.teleop.normal.runners.TankMainRunner;

@TeleOp(name="TankMain", group="Linear Opmode")
public class TankMain extends LinearOpMode {
    @Override
    public void runOpMode() {
        new TankMainRunner().run(this);
    }
}
