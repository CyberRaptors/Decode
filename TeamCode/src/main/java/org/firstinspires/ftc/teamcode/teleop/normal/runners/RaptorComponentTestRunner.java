package org.firstinspires.ftc.teamcode.teleop.normal.runners;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.RaptorRobot;

public class RaptorComponentTestRunner {
    private final RaptorRobot robot;
    private final LinearOpMode opMode;
    private final Telemetry telemetry;

    public RaptorComponentTestRunner(RaptorRobot robot, LinearOpMode opMode) {
        this.robot = robot;
        this.opMode = opMode;
        this.telemetry = opMode.telemetry;
    }

    public void runTest(double durationSeconds) throws InterruptedException {
        telemetry.addLine(String.format("Running test for %.2f seconds", durationSeconds));
        telemetry.update();

        activateComponents();

        ElapsedTime timer = new ElapsedTime();

        while (opMode.opModeIsActive() && timer.seconds() < durationSeconds) {
            telemetry.addData("Time Remaining", "%.1f seconds", durationSeconds - timer.seconds());
            telemetry.addData("Shooter Left Velo", robot.shooterLeft.getVelocity());
            telemetry.addData("Shooter Right Velo", robot.shooterRight.getVelocity());
            telemetry.addData("Rail Feeder Pos", robot.railDriveThree.getPosition());
            telemetry.update();

            opMode.idle();
        }

        deactivateComponents();

        telemetry.addLine("Test Complete");
        telemetry.update();
    }

    private void activateComponents() {
        robot.driverControl.applyDrivePower(0.5, 0.5, 0.5);
        robot.driverControl.setIntakeGroupPower(1.0);
        robot.driverControl.setShooterVelocity(robot.SHOOTER_VELO_FOR_CLOSE_SHOT);
        robot.driverControl.setRailDriveTwoPower(1.0);
        robot.driverControl.setRailDriveThreePosition(robot.FEEDER_SHOOT_POS);
    }
    private void deactivateComponents() {
        robot.driverControl.applyDrivePower(0, 0, 0);
        robot.driverControl.setIntakeGroupPower(0);
        robot.driverControl.setShooterVelocity(0);
        robot.driverControl.setRailDriveTwoPower(0);
        robot.driverControl.setRailDriveThreePosition(robot.FEEDER_READY_POS);
        if (robot.limelight != null) {
            robot.limelight.close();
        }
    }
}
