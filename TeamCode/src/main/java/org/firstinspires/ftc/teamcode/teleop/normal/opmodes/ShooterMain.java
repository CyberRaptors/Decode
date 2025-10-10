package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.teleop.normal.runners.ShooterMainRunner;

@TeleOp(name="ShooterMain", group="Linear Opmode")
public class ShooterMain extends LinearOpMode {
    @Override
    public void runOpMode() {
        new ShooterMainRunner().run(this);
    }
}
