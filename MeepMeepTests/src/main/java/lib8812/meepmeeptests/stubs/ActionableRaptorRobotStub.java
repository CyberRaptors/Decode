package lib8812.meepmeeptests.stubs;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.MinMax;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.VelConstraint;

public class ActionableRaptorRobotStub {
	public final double MAX_VELO_FOR_SPIKE_PICKUP = 20;
	public final VelConstraint SPIKE_PICKUP_VEL_CONSTRAINT = (a, b, c) -> MAX_VELO_FOR_SPIKE_PICKUP;

	public final double MAX_VELO_FOR_MONSTER_LZ_PICKUP = 60;
	public final VelConstraint MONSTER_LZ_PICKUP_VEL_CONSTRAINT = (a, b, c) -> MAX_VELO_FOR_MONSTER_LZ_PICKUP;


	public final MinMax ACCEL_FOR_MONSTER_LZ_PICKUP = new MinMax(-80, 80);
	public final AccelConstraint MONSTER_LZ_PICKUP_ACCEL_CONSTRAINT = (a, b, c) -> ACCEL_FOR_MONSTER_LZ_PICKUP;

	public final double SHOOTER_VELO_FOR_SHORT_PARK_SHOT = 0;
	public final double SHOOTER_VELO_FOR_CLOSE_SHOT = 0;
	public final double SHOOTER_VELO_FOR_CLOSE_MONSTER_SHOT = 0;
	public final double SHOOTER_VELO_FOR_FAR_SHOT = 0;

	public Action setIntakePower(double power) { return new SleepAction(0); }

	public Action setTransferPower(double power) { return new SleepAction(0); }

	public Action setIntakeAndTransferPower(double power) { return new SleepAction(0); }

	public Action startShootersAsync(double velo) { return new SleepAction(0); }

	public Action disableShootersAsync() { return new SleepAction(0); }

	public Action shootThree(double velo) { return new SleepAction(3.1); }

	public Action shootTwo(double velo) { return new SleepAction(2.1); }
}
