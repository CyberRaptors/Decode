package org.firstinspires.ftc.teamcode.teleop.normal.runners;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.robot.LimelightOnlyBot;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import java.util.List;

import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class LimelightOnlyRunner extends ITeleOpRunner {
    LimelightOnlyBot bot = new LimelightOnlyBot();

    @Override
    protected void internalRun() {
        bot.limelight.pipelineSwitch(2);

        while (opModeIsActive()) {

            LLResult result = bot.limelight.getLatestResult();
            if (result != null && result.isValid()) {
                double tx = result.getTx(); // How far left or right the target is (degrees)
                double ty = result.getTy(); // How far up or down the target is (degrees)
                double ta = result.getTa(); // How big the target looks (0%-100% of the image)

                telemetry.addData("Target X", tx);
                telemetry.addData("Target Y", ty);
                telemetry.addData("Target Area", ta);
            } else {
                telemetry.addData("Limelight", "No Targets");
            }
            if (result != null && result.isValid()) {
                Pose3D botpose = result.getBotpose();
                if (botpose != null) {
                    double x = botpose.getPosition().x;
                    double y = botpose.getPosition().y;
                    telemetry.addData("MT1 Location", "(" + x + ", " + y + ")");
                }
            }
            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
            for (LLResultTypes.FiducialResult fiducial : fiducials) {
                int id = fiducial.getFiducialId(); // The ID number of the fiducial
                telemetry.addData("Fiducial", "id (%d)", id);
            }
        }
    }

    @Override
    protected IRobot getBot() {
        return bot;
    }
}
