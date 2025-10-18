package org.firstinspires.ftc.teamcode.teleop.normal.runners;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.robot.LimelightOnlyBot;

import java.util.List;

import lib8812.common.actions.OnceAction;
import lib8812.common.game.GameConstants;
import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class LimelightOnlyRunner extends ITeleOpRunner {
    LimelightOnlyBot bot = new LimelightOnlyBot();

    public boolean LOCK_DRIVE = false;

    void alignToTarget(boolean blue) {
        if (LOCK_DRIVE) return;

        LOCK_DRIVE = true;

        int targetTagId = GameConstants.DECODE.GOAL_APRILTAG_ID(blue);

        actions.scheduleAll(new SequentialAction(
                new InstantAction(() -> {
                    bot.drive.setDrivePowers(
                            new PoseVelocity2d(
                                    new Vector2d(0, 0),
                                    0.5
                            )
                    );
                }),
                new OnceAction(() -> {
                    LLResult result = bot.limelight.getLatestResult();

                    if (result != null && result.isValid()) {
                        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                        for (LLResultTypes.FiducialResult fiducial : fiducials) {
                            if (fiducial.getFiducialId() == targetTagId) {
                                return true;
                            }
                        }
                    }

                    return false;
                }, new InstantAction(() -> {
                    bot.drive.setDrivePowers(
                            new PoseVelocity2d(
                                    new Vector2d(0, 0),
                                    0
                            )
                    );

                    LOCK_DRIVE = false;
                }))

        ));
    }

    @Override
    protected void internalRun() {
        keybinder.bind("x").of(gamepad1).to(() -> alignToTarget(false));
        keybinder.bind("b").of(gamepad1).to(() -> alignToTarget(true));

        while (opModeIsActive()) {

            LLResult result = bot.limelight.getLatestResult();

            if (result != null  &&  result.isValid()) {
                telemetry.addData("Limelight Result", "pipeline %d (%s)", result.getPipelineIndex(), result.getPipelineType());

                double tx = result.getTx();
                double ty = result.getTy();
                double ta = result.getTa();

                telemetry.addData("Target X", tx);
                telemetry.addData("Target Y", ty);
                telemetry.addData("Target Area", ta);

                Pose3D botpose = result.getBotpose();
                if (botpose != null) {
                    double x = botpose.getPosition().x;
                    double y = botpose.getPosition().y;
                    telemetry.addData("MT1 Location", "(" + x + ", " + y + ")");
                }

                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fiducial : fiducials) {
                    int id = fiducial.getFiducialId(); // The ID number of the fiducial
                    telemetry.addData("Fiducial", "id (%d)", id);
                }
            } else {
                telemetry.addData("Limelight", "No Targets");
            }

            LLStatus limelightStatus = bot.limelight.getStatus();

            telemetry.addData("Limelight", "%s, %s, %s", bot.limelight.isConnected() ? "connected" : "not connected", bot.limelight.isRunning() ? "running" : "not running", bot.limelight.getConnectionInfo().trim());
            telemetry.addData("Limelight Status", "name (%s), temp (%.2f °C)", limelightStatus.getName(), limelightStatus.getTemp());
            telemetry.update();

            keybinder.executeActions();
            actions.execute();
        }
    }

    @Override
    protected IRobot getBot() {
        return bot;
    }
}
