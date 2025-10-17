package org.firstinspires.ftc.teamcode.teleop.normal.runners;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.robot.PlaneShooterBot;
import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class ShooterMainRunner extends ITeleOpRunner {
    PlaneShooterBot bot = new PlaneShooterBot();

    protected void internalRun(){
        keybinder.bind("right_trigger").of(gamepad1).to(bot.planeShooter::toggle);

        while(opModeIsActive()){
            bot.drive.setDrivePowers(
                    new PoseVelocity2d(
                            new Vector2d(
                                    -gamepad1.inner.left_stick_y,
                                    -gamepad1.inner.left_stick_x
                            ),
                            -gamepad1.inner.right_stick_x
                    )
            );

            keybinder.executeActions();
            telemetry.addData("Plane Shooter State", bot.planeShooter.getStatus());
            telemetry.update();
        }
    }
    @Override
    protected IRobot getBot() { return bot; }
}
