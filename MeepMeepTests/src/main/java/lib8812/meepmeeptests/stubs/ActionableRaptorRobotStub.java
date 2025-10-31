package lib8812.meepmeeptests.stubs;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.NullAction;
import com.acmerobotics.roadrunner.VelConstraint;

import lib8812.meepmeeptests.stubs.game.ArtifactConfiguration;

public class ActionableRaptorRobotStub {
	public final int LIMELIGHT_APRILTAG_INDEX = 1;

	public final double SHOOTER_TICKS_PER_REV = 28;

	public final double MAX_ROBOT_VELO_FOR_SPIKE_PICKUP = 7;
	public final VelConstraint SPIKE_PICKUP_VEL_CONSTRAINT = (pose2dDual, posePath, v) -> MAX_ROBOT_VELO_FOR_SPIKE_PICKUP;

	public final double SHOOTER_VELO_FOR_CLOSE_SHOT = 1180;
	public final double SHOOTER_VELO_FOR_MID_SHOT = 1300;
	public final double SHOOTER_VELO_FOR_FAR_SHOT = 0;

	public final double SHOOTER_VELO_FOR_REJECT = 400;

	public final double RAIL_DRIVE_THREE_MIN_POS = 0.551;
	public final double RAIL_DRIVE_THREE_MAX_POS = 0.89;

	public final double FEEDER_READY_POS = RAIL_DRIVE_THREE_MAX_POS;
	public final double FEEDER_SHOOT_POS = RAIL_DRIVE_THREE_MIN_POS;

	public ArtifactConfiguration artifactConfiguration = ArtifactConfiguration.PPG.copySelf();

	public ArtifactConfiguration storedMotif;

	public Action setRailDriveTwoPower(double power) { return new NullAction(); }

	public Action setIntakeGroupPower(double power) { return new NullAction(); }

	public Action shootWithVelo(double velo) {
		return new NullAction();
	}

	public Action _shootWithVelo(double velo) {
		return new NullAction();
	}

	public Action feedNext() {
		return new NullAction();
	}

	public Action _feedNext() {
		return new NullAction();
	}

	public Action successiveShootWithVelo(int ammunition, double velo) {
		return new NullAction();
	}

	public Action reject() {
		return new NullAction();
	}

	public Action limelightAlignToGoal() {
		return new NullAction();
	}

	public Action storeMotif() {
		return new NullAction();
	}

	public Action sortToMotif() {
		return new NullAction(); // return a OnceAction waiting for the motif to be stored with reasonable maxTries
	}

	public Action setInternalArtifactConfig(ArtifactConfiguration config) {
		return new InstantAction(() -> {
			artifactConfiguration = config.copySelf();
		});
	}
}
