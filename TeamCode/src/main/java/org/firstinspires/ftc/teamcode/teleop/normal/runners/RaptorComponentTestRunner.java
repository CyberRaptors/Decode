package org.firstinspires.ftc.teamcode.teleop.normal.runners;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;
import lib8812.common.robot.IRobot;
import lib8812.common.teleop.ITeleOpRunner;

public class RaptorComponentTestRunner extends ITeleOpRunner {
    private final RaptorRobot robot;

    public RaptorComponentTestRunner(RaptorRobot robot, LinearOpMode opMode) {
        super();
        this.robot = robot;
    }

    @Override
    protected void internalRun() {
        // Drive base test
        telemetry.addLine("Testing drive base...");
        telemetry.update();
        robot.driverControl.applyDrivePower(0.5, 0.5, 0.5);
        sleep(5000);
        robot.driverControl.applyDrivePower(0, 0, 0);
        telemetry.addLine("Drive base test complete.");
        telemetry.update();

        // Intake test
        telemetry.addLine("Testing intake...");
        telemetry.update();
        robot.driverControl.setIntakePower(1.0);
        sleep(5000);
        robot.driverControl.setIntakePower(0);
        telemetry.addLine("Intake test complete.");
        telemetry.update();

        // Shooter test
        telemetry.addLine("Testing shooters...");
        telemetry.update();
        robot.driverControl.setShooterVelocity(robot.SHOOTER_VELO_FOR_CLOSE_SHOT);
        ElapsedTime timer = new ElapsedTime();
        while(opMode.opModeIsActive() && timer.seconds() < 5.0) {
            telemetry.addData("Shooter Left Velo", robot.shooterLeft.getVelocity());
            telemetry.addData("Shooter Right Velo", robot.shooterRight.getVelocity());
            telemetry.update();
            sleep(50);
        }
        robot.driverControl.setShooterVelocity(0);
        telemetry.addLine("Shooter test complete.");
        telemetry.update();

        // Rail test
        telemetry.addLine("Testing intake...");
        telemetry.update();
        robot.driverControl.setIntakePower(1.0);
        timer.reset();
        while(opMode.opModeIsActive() && timer.seconds() < 5.0) {
            telemetry.update();
            sleep(50);
        }
        robot.driverControl.setIntakePower(0);
        telemetry.addLine("Intake test complete.");
        telemetry.update();

        telemetry.addLine("Testing transfer...");
        telemetry.update();
        robot.driverControl.setTransferPower(1.0);
        timer.reset();
        while(opMode.opModeIsActive() && timer.seconds() < 5.0) {
            telemetry.update();
            sleep(50);
        }
        robot.driverControl.setTransferPower(0);
        telemetry.addLine("Intake test complete.");
        telemetry.update();

        telemetry.addLine("All tests complete.");
        telemetry.update();
        sleep(5000);
    }

    @Override
    protected IRobot getBot() {
        return robot;
    }
}
