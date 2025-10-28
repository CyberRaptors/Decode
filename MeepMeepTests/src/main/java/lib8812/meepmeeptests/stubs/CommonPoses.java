package lib8812.meepmeeptests.stubs;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

import lib8812.meepmeeptests.stubs.game.FieldConstants;

public class CommonPoses {
	public static final Pose2d INITIAL_BLUE_NEAR_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*2 - 0.2, -FieldConstants.TILE_LENGTH_IN*2 - 3.5, Math.toRadians(233.6));
	public static final Pose2d BLUE_NEAR_SHOT_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN, -FieldConstants.TILE_LENGTH_IN, Math.toRadians(220));
	public static final Pose2d BLUE_FIRST_SPIKE_START_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN-5, Math.toRadians(270));
	public static final Pose2d BLUE_FIRST_SPIKE_END_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN*2, Math.toRadians(270));
	public static final Pose2d BLUE_SECOND_SPIKE_START_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN-5, Math.toRadians(270));
	public static final Pose2d BLUE_SECOND_SPIKE_END_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN*2, Math.toRadians(270));
	public static final Vector2d BLUE_NEAR_PARK_POS = new Vector2d(-FieldConstants.TILE_LENGTH_IN*2, -FieldConstants.TILE_LENGTH_IN);
}
