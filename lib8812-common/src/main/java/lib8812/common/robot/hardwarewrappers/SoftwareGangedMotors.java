package lib8812.common.robot.hardwarewrappers;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class SoftwareGangedMotors implements DcMotor {
	final DcMotor[] motors;

	public SoftwareGangedMotors(DcMotor... motors) {
		this.motors = motors;
	}


	@Override
	public MotorConfigurationType getMotorType() {
		return motors[0].getMotorType();
	}

	@Override
	public void setMotorType(MotorConfigurationType motorType) {
		for (DcMotor motor : motors) {
			motor.setMotorType(motorType);
		}
	}

	@Override
	public DcMotorController getController() {
		return motors[0].getController();
	}

	@Override
	public int getPortNumber() {
		return motors[0].getPortNumber();
	}

	@Override
	public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
		for (DcMotor motor : motors) {
			motor.setZeroPowerBehavior(zeroPowerBehavior);
		}
	}

	@Override
	public ZeroPowerBehavior getZeroPowerBehavior() {
		return motors[0].getZeroPowerBehavior();
	}

	@Override
	public void setPowerFloat() {
		for (DcMotor motor : motors) {
			motor.setPowerFloat();
		}
	}

	@Override
	public boolean getPowerFloat() {
		return motors[0].getPowerFloat();
	}

	@Override
	public void setTargetPosition(int position) {
		for (DcMotor motor : motors) {
			motor.setTargetPosition(position);
		}
	}

	@Override
	public int getTargetPosition() {
		return motors[0].getTargetPosition();
	}

	@Override
	public boolean isBusy() {
		return motors[0].isBusy();
	}

	@Override
	public int getCurrentPosition() {
		return motors[0].getCurrentPosition();
	}

	@Override
	public void setMode(RunMode mode) {
		for (DcMotor motor : motors) {
			motor.setMode(mode);
		}
	}

	@Override
	public RunMode getMode() {
		return motors[0].getMode();
	}

	@Override
	public void setDirection(Direction direction) {
		for (DcMotor motor : motors) {
			motor.setDirection(direction);
		}
	}

	@Override
	public Direction getDirection() {
		return motors[0].getDirection();
	}

	@Override
	public void setPower(double power) {
		for (DcMotor motor : motors) {
			motor.setPower(power);
		}
	}

	@Override
	public double getPower() {
		return motors[0].getPower();
	}

	@Override
	public Manufacturer getManufacturer() {
		return motors[0].getManufacturer();
	}

	@Override
	public String getDeviceName() {
		return motors[0].getDeviceName();
	}

	@Override
	public String getConnectionInfo() {
		return motors[0].getConnectionInfo();
	}

	@Override
	public int getVersion() {
		return motors[0].getVersion();
	}

	@Override
	public void resetDeviceConfigurationForOpMode() {
		for (DcMotor motor : motors) {
			motor.resetDeviceConfigurationForOpMode();
		}
	}

	@Override
	public void close() {
		for (DcMotor motor : motors) {
			motor.close();
		}
	}
}
