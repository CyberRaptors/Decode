package lib8812.common.game;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

public class GameConstants {
	public static class DECODE {
		public static final int RED_GOAL_APRILTAG_ID = 24;
		public static final int BLUE_GOAL_APRILTAG_ID = 20;

		public static final int MOTIF_GPP_APRILTAG_ID = 21;
		public static final int MOTIF_PGP_APRILTAG_ID = 22;
		public static final int MOTIF_PPG_APRILTAG_ID = 23;

		public static final double GOAL_APRILTAG_HEIGHT_IN = 38.75;

		public static int GOAL_APRILTAG_ID(boolean blue) {
			if (blue) return BLUE_GOAL_APRILTAG_ID;

			return RED_GOAL_APRILTAG_ID;
		}

		public static ArtifactConfiguration getMotifFromAprilTagId(int id) {
			if (id == MOTIF_GPP_APRILTAG_ID) return ArtifactConfiguration.GPP;
			if (id == MOTIF_PGP_APRILTAG_ID) return ArtifactConfiguration.PGP;
			if (id == MOTIF_PPG_APRILTAG_ID) return ArtifactConfiguration.PPG;

			return null;
		}

		public static Vector2d GOAL_POSITION(boolean blue) {
			if (blue) return CommonPoses.BLUE_GOAL_CORNER;

			return CommonPoses.RED_GOAL_CORNER;
		}
		public static Pose2d BASE_PARKING_POSE(boolean blue) {
			if (blue) return CommonPoses.BLUE_BASE_PARKING_POSE;

			return CommonPoses.RED_BASE_PARKING_POSE;
		}
	}
}
