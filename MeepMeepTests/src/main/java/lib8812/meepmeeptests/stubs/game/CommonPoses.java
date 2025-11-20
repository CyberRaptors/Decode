package lib8812.meepmeeptests.stubs.game;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

public class CommonPoses {
	static Pose2d allianceReflect(Pose2d pose) {
		return new Pose2d(
				allianceReflect(pose.position),
				pose.heading.inverse()
		);
	}

	static Vector2d allianceReflect(Vector2d vec) {
		return new Vector2d(vec.x, -vec.y);
	}

	final static double BASE_DEGREES_FOR_INITIAL_NEAR_POSE = 270;
	final static double BASE_DEGREES_FOR_NEAR_SHOT = 230;
	final static double BASE_DEGREES_FOR_FAR_SHOT = 205;
	final static double BASE_DEGREES_FOR_NEAR_PARK = 90;
	final static double BASE_DEGREES_FOR_NEAR_MOTIF_READ = 143.6;
	final static double BASE_DEGREES_FOR_FAR_MOTIF_READ = 180;
	final static double BASE_DEGREES_FOR_SPIKE_INTAKE = 270;


	public static final Pose2d INITIAL_BLUE_NEAR_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*1.5 - 3.75, -FieldConstants.TILE_LENGTH_IN*2 - 7, Math.toRadians(BASE_DEGREES_FOR_INITIAL_NEAR_POSE));
	public static final Pose2d BLUE_NEAR_SHOT_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN, -FieldConstants.TILE_LENGTH_IN, Math.toRadians(BASE_DEGREES_FOR_NEAR_SHOT));
	public static final Pose2d BLUE_FIRST_SPIKE_START_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN - 4, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_FIRST_SPIKE_END_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN*2 - 6, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_SECOND_SPIKE_START_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN - 4, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_SECOND_SPIKE_END_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN*2 - 1, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_NEAR_PARK_POSE = new Pose2d(0, -FieldConstants.TILE_LENGTH_IN*2 - 3, Math.toRadians(BASE_DEGREES_FOR_NEAR_PARK));

	public static final Pose2d INITIAL_BLUE_NEAR_POSE_FOR_MOTIF_AUTO = new Pose2d(-FieldConstants.TILE_LENGTH_IN*2 - 0.2, -FieldConstants.TILE_LENGTH_IN*2 - 3.5, Math.toRadians(BASE_DEGREES_FOR_NEAR_MOTIF_READ));
	public static final Pose2d BLUE_NEAR_MOTIF_READ_POS = new Pose2d(-FieldConstants.TILE_LENGTH_IN*1.2, -FieldConstants.TILE_LENGTH_IN*1.2, Math.toRadians(BASE_DEGREES_FOR_NEAR_MOTIF_READ));

	public static final Pose2d INITIAL_BLUE_FAR_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*2.5 + 2, -FieldConstants.TILE_LENGTH_IN*0.5, Math.toRadians(180));
	public static final Pose2d BLUE_FAR_SHOT_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*2, -FieldConstants.TILE_LENGTH_IN*0.5 - 2, Math.toRadians(BASE_DEGREES_FOR_FAR_SHOT));
	public static final Pose2d BLUE_THIRD_SPIKE_START_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*1.5, -FieldConstants.TILE_LENGTH_IN-5, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_THIRD_SPIKE_END_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*1.5, -FieldConstants.TILE_LENGTH_IN*2, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Vector2d BLUE_FAR_PARK_POS = new Vector2d(FieldConstants.TILE_LENGTH_IN*2, -FieldConstants.TILE_LENGTH_IN);

	public static final Pose2d INITIAL_RED_NEAR_POSE = allianceReflect(INITIAL_BLUE_NEAR_POSE);
	public static final Pose2d RED_NEAR_SHOT_POSE = allianceReflect(BLUE_NEAR_SHOT_POSE);
	public static final Pose2d RED_FIRST_SPIKE_START_POSE = allianceReflect(BLUE_FIRST_SPIKE_START_POSE);
	public static final Pose2d RED_FIRST_SPIKE_END_POSE = allianceReflect(BLUE_FIRST_SPIKE_END_POSE);
	public static final Pose2d RED_SECOND_SPIKE_START_POSE = allianceReflect(BLUE_SECOND_SPIKE_START_POSE);
	public static final Pose2d RED_SECOND_SPIKE_END_POSE = allianceReflect(BLUE_SECOND_SPIKE_END_POSE);
	public static final Pose2d RED_NEAR_PARK_POSE = allianceReflect(BLUE_NEAR_PARK_POSE);

	public static final Pose2d INITIAL_RED_FAR_POSE = allianceReflect(INITIAL_BLUE_FAR_POSE);
	public static final Pose2d RED_FAR_SHOT_POSE = allianceReflect(BLUE_FAR_SHOT_POSE);
	public static final Pose2d RED_THIRD_SPIKE_START_POSE = allianceReflect(BLUE_THIRD_SPIKE_START_POSE);
	public static final Pose2d RED_THIRD_SPIKE_END_POSE = allianceReflect(BLUE_THIRD_SPIKE_END_POSE);
	public static final Vector2d RED_FAR_PARK_POS = allianceReflect(BLUE_FAR_PARK_POS);

	public static final Pose2d BLUE_BASE_PARKING_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*1.5+3, FieldConstants.TILE_LENGTH_IN*1.5-3, Math.PI);
	public static final Pose2d RED_BASE_PARKING_POSE = allianceReflect(BLUE_BASE_PARKING_POSE);

	public static final Vector2d BLUE_GOAL_CORNER = new Vector2d(-FieldConstants.TILE_LENGTH_IN*3, FieldConstants.TILE_LENGTH_IN*3);
	public static final Vector2d RED_GOAL_CORNER = allianceReflect(BLUE_GOAL_CORNER);
}