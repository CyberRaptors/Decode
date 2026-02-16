package lib8812.common.game;

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
	final static double BASE_DEGREES_FOR_NEAR_SHOT = 225;
	final static double BASE_DEGREES_FOR_NEAR_MONSTER_SHOT = 215;
	final static double BASE_DEGREES_FOR_NEAR_SHORT_PARK_SHOT = 252;
	final static double BASE_DEGREES_FOR_FAR_PARK = 225;
	final static double BASE_DEGREES_FOR_MID_SHOT = 217.5;
	final static double BASE_DEGREES_FOR_FAR_SHOT = 201.5; // previously 203
	final static double BASE_DEGREES_FOR_FAR_MONSTER_SHOT = 205;
	final static double BASE_DEGREES_FOR_SPIKE_INTAKE = 270;
	final static double BASE_DEGREES_FOR_GATE_OPEN = 90; // facing backwards into the gate
	final static double BASE_DEGREES_FOR_QUICK_GATE_OPEN = 270; // facing forwards into the gate
	final static double BASE_DEGREES_FOR_MONSTER_GATE_OPEN = 240;
	final static double BASE_DEGREES_FOR_FRIGHTENING_LOADING_ZONE_INTAKE = 290;
	final static double BASE_DEGREES_FOR_TERRIFYING_SECRET_TUNNEL_INTAKE = 210;
	final static double BASE_DEGREES_FOR_MONSTER_LOADING_ZONE_INTAKE = 290;

	public static final Pose2d INITIAL_BLUE_NEAR_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*1.5 - 3.75, -FieldConstants.TILE_LENGTH_IN*2 - 7, Math.toRadians(BASE_DEGREES_FOR_INITIAL_NEAR_POSE));
	public static final Pose2d BLUE_NEAR_SHOT_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN + 5, -FieldConstants.TILE_LENGTH_IN + 5, Math.toRadians(BASE_DEGREES_FOR_NEAR_SHOT));
	public static final Pose2d BLUE_NEAR_MONSTER_SHOT_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*0.25, -FieldConstants.TILE_LENGTH_IN*0.5 - 5, Math.toRadians(BASE_DEGREES_FOR_NEAR_MONSTER_SHOT));
	public static final Pose2d BLUE_FIRST_SPIKE_START_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN - 4, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_FIRST_SPIKE_END_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN*2 - 6, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_SECOND_SPIKE_START_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN - 4, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_SECOND_SPIKE_END_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN*2 - 4, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_NEAR_PARK_POSE = new Pose2d(-4, -FieldConstants.TILE_LENGTH_IN*1.5 - 2, Math.toRadians(BASE_DEGREES_FOR_GATE_OPEN));
	public static final Pose2d BLUE_NEAR_SHORT_PARK_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*2, -FieldConstants.TILE_LENGTH_IN*0.75 + 3, Math.toRadians(BASE_DEGREES_FOR_NEAR_SHORT_PARK_SHOT));

	public static final Pose2d BLUE_QUICK_CLEAR_GATE_END_POSE = new Pose2d(3, -FieldConstants.TILE_LENGTH_IN*2 - 10, Math.toRadians(BASE_DEGREES_FOR_QUICK_GATE_OPEN));

	public static final Pose2d BLUE_CLEAR_GATE_START_POSE = new Pose2d(-4, -FieldConstants.TILE_LENGTH_IN*2, Math.toRadians(BASE_DEGREES_FOR_GATE_OPEN));
	public static final Pose2d BLUE_CLEAR_GATE_END_POSE = new Pose2d(-4, -FieldConstants.TILE_LENGTH_IN*2 - 10, Math.toRadians(BASE_DEGREES_FOR_GATE_OPEN));

	public static final Pose2d BLUE_CLEAR_GATE_FROM_FAR_START_POSE = new Pose2d(7, -FieldConstants.TILE_LENGTH_IN*2, Math.toRadians(BASE_DEGREES_FOR_GATE_OPEN));
	public static final Pose2d BLUE_CLEAR_GATE_FROM_FAR_END_POSE = new Pose2d(7, -FieldConstants.TILE_LENGTH_IN*2 - 10, Math.toRadians(BASE_DEGREES_FOR_GATE_OPEN));

	public static final Pose2d BLUE_MONSTER_CLEAR_GATE_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*0.5, -FieldConstants.TILE_LENGTH_IN*2.5, Math.toRadians(BASE_DEGREES_FOR_MONSTER_GATE_OPEN));

	public static final Pose2d INITIAL_BLUE_FAR_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*2.5 + 2, -FieldConstants.TILE_LENGTH_IN*0.5, Math.toRadians(180));
	public static final Pose2d BLUE_FAR_SHOT_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*2.25, -FieldConstants.TILE_LENGTH_IN*0.5 - 4, Math.toRadians(BASE_DEGREES_FOR_FAR_SHOT));
	public static final Pose2d BLUE_THIRD_SPIKE_START_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*1.5, -FieldConstants.TILE_LENGTH_IN - 4, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_THIRD_SPIKE_END_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*1.5, -FieldConstants.TILE_LENGTH_IN*2 - 4, Math.toRadians(BASE_DEGREES_FOR_SPIKE_INTAKE));
	public static final Pose2d BLUE_FAR_PARK_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*2, -FieldConstants.TILE_LENGTH_IN, Math.toRadians(BASE_DEGREES_FOR_FAR_PARK));

	public static final Pose2d BLUE_MID_SHOT_POSE = new Pose2d(-FieldConstants.TILE_LENGTH_IN*0.25 + 3, -FieldConstants.TILE_LENGTH_IN*0.5 - 2, Math.toRadians(BASE_DEGREES_FOR_MID_SHOT));

	public static final Pose2d BLUE_FAR_FRIGHTENING_PICKUP_FROM_LOADING_ZONE_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*2.5 + 2, -FieldConstants.TILE_LENGTH_IN*2.5 + 1, Math.toRadians(BASE_DEGREES_FOR_FRIGHTENING_LOADING_ZONE_INTAKE));
	// x was previously 2.5l - 1 and is now 2.51 + 2
	public static final Pose2d BLUE_FAR_TERRIFYING_PICKUP_FROM_LOADING_ZONE_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*2.5 + 2, -FieldConstants.TILE_LENGTH_IN*2.5 + 1, Math.toRadians(BASE_DEGREES_FOR_FRIGHTENING_LOADING_ZONE_INTAKE));
	public static final Pose2d BLUE_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*1.5, -FieldConstants.TILE_LENGTH_IN*2.5 - 2, Math.toRadians(BASE_DEGREES_FOR_TERRIFYING_SECRET_TUNNEL_INTAKE));
	public static final Pose2d BLUE_FAR_MONSTER_PICKUP_FROM_LOADING_ZONE_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*2.25, -FieldConstants.TILE_LENGTH_IN*2.5 + 2, Math.toRadians(BASE_DEGREES_FOR_MONSTER_LOADING_ZONE_INTAKE));

	public static final Pose2d BLUE_FAR_MONSTER_SHOT_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*2, -FieldConstants.TILE_LENGTH_IN*0.5, Math.toRadians(BASE_DEGREES_FOR_FAR_MONSTER_SHOT));


	public static final Pose2d INITIAL_RED_NEAR_POSE = allianceReflect(INITIAL_BLUE_NEAR_POSE);
	public static final Pose2d RED_NEAR_SHOT_POSE = allianceReflect(BLUE_NEAR_SHOT_POSE);
	public static final Pose2d RED_FIRST_SPIKE_START_POSE = allianceReflect(BLUE_FIRST_SPIKE_START_POSE);
	public static final Pose2d RED_FIRST_SPIKE_END_POSE = allianceReflect(BLUE_FIRST_SPIKE_END_POSE);
	public static final Pose2d RED_SECOND_SPIKE_START_POSE = allianceReflect(BLUE_SECOND_SPIKE_START_POSE);
	public static final Pose2d RED_SECOND_SPIKE_END_POSE = allianceReflect(BLUE_SECOND_SPIKE_END_POSE);
	public static final Pose2d RED_NEAR_PARK_POSE = allianceReflect(BLUE_NEAR_PARK_POSE);
	public static final Pose2d RED_NEAR_SHORT_PARK_POSE = allianceReflect(BLUE_NEAR_SHORT_PARK_POSE);

	public static final Pose2d RED_CLEAR_GATE_START_POSE = allianceReflect(BLUE_CLEAR_GATE_START_POSE);
	public static final Pose2d RED_CLEAR_GATE_END_POSE = allianceReflect(BLUE_CLEAR_GATE_END_POSE);

	public static final Pose2d INITIAL_RED_FAR_POSE = allianceReflect(INITIAL_BLUE_FAR_POSE);
	public static final Pose2d RED_FAR_SHOT_POSE = allianceReflect(BLUE_FAR_SHOT_POSE);
	public static final Pose2d RED_THIRD_SPIKE_START_POSE = allianceReflect(BLUE_THIRD_SPIKE_START_POSE);
	public static final Pose2d RED_THIRD_SPIKE_END_POSE = allianceReflect(BLUE_THIRD_SPIKE_END_POSE);
	public static final Pose2d RED_FAR_PARK_POSE = allianceReflect(BLUE_FAR_PARK_POSE);
	public static final Pose2d RED_MID_SHOT_POSE = allianceReflect(BLUE_MID_SHOT_POSE);


	public static final Pose2d RED_CLEAR_GATE_FROM_FAR_START_POSE = allianceReflect(BLUE_CLEAR_GATE_FROM_FAR_START_POSE);
	public static final Pose2d RED_CLEAR_GATE_FROM_FAR_END_POSE = allianceReflect(BLUE_CLEAR_GATE_FROM_FAR_END_POSE);


	public static final Pose2d RED_MONSTER_CLEAR_GATE_POSE = allianceReflect(BLUE_MONSTER_CLEAR_GATE_POSE);

	public static final Pose2d RED_FAR_FRIGHTENING_PICKUP_FROM_LOADING_ZONE_POSE = allianceReflect(BLUE_FAR_FRIGHTENING_PICKUP_FROM_LOADING_ZONE_POSE);
	// x was previously 2.5l - 1 and is now 2.51 + 2
	public static final Pose2d RED_FAR_TERRIFYING_PICKUP_FROM_LOADING_ZONE_POSE = allianceReflect(BLUE_FAR_TERRIFYING_PICKUP_FROM_LOADING_ZONE_POSE);
	public static final Pose2d RED_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE = allianceReflect(BLUE_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE);
	public static final Pose2d RED_FAR_MONSTER_PICKUP_FROM_LOADING_ZONE_POSE = allianceReflect(BLUE_FAR_MONSTER_PICKUP_FROM_LOADING_ZONE_POSE);



	public static final Pose2d BLUE_BASE_PARKING_POSE = new Pose2d(FieldConstants.TILE_LENGTH_IN*1.5, FieldConstants.TILE_LENGTH_IN*1.5, Math.PI);
	public static final Pose2d RED_BASE_PARKING_POSE = allianceReflect(BLUE_BASE_PARKING_POSE);

	public static final Vector2d BLUE_GOAL_CORNER = new Vector2d(-FieldConstants.TILE_LENGTH_IN*3, -FieldConstants.TILE_LENGTH_IN*3);
	public static final Vector2d RED_GOAL_CORNER = allianceReflect(BLUE_GOAL_CORNER);
}