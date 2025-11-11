package org.firstinspires.ftc.teamcode.teleop.normal.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;
import org.firstinspires.ftc.teamcode.teleop.normal.runners.RaptorComponentTestRunner;

public class RaptorComponentTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        RaptorRobot robot = new RaptorRobot(true);
        robot.init(hardwareMap);

        RaptorComponentTestRunner testRunner = new RaptorComponentTestRunner(robot, this);

        telemetry.addLine("Press play to start the test");
        telemetry.update();
        waitForStart();

        if (isStopRequested()) return;
        testRunner.runTest(5.0);

        while(opModeIsActive()){
            idle();
        }
    }
}
