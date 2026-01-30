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

		public static final double APPROX_GOAL_HEIGHT_IN = 38.75;

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

		public static Pose2d GATE_CLEAR_START_POSE(boolean blue) {
			if (blue) return CommonPoses.BLUE_CLEAR_GATE_START_POSE;

			return CommonPoses.RED_CLEAR_GATE_START_POSE;
		}

		public static Pose2d GATE_CLEAR_END_POSE(boolean blue) {
			if (blue) return CommonPoses.BLUE_CLEAR_GATE_END_POSE;

			return CommonPoses.RED_CLEAR_GATE_END_POSE;
		}

		public static Pose2d MONSTER_GATE_CLEAR_POSE(boolean blue) {
			if (blue) return CommonPoses.BLUE_MONSTER_CLEAR_GATE_POSE;

			return CommonPoses.RED_MONSTER_CLEAR_GATE_POSE;

		}

		public static boolean isInNearShootingZone(Vector2d pos, double botRadius) {
			double extremeX = pos.x-botRadius;

			if (extremeX > 0) return false;

			double extremeY;

			if (pos.y > 0) { // red side of field
				extremeY = pos.y - botRadius;

			} else { // blue side of field
				extremeY = pos.y + botRadius;
			}

			return Math.abs(extremeX) >= Math.abs(extremeY);
		}

		public static boolean isInFarShootingZone(Vector2d pos, double botRadius) {
			double extremeX = pos.x+botRadius;

			if (extremeX < FieldConstants.TILE_LENGTH_IN*2) return false;

			double extremeY;

			if (pos.y > 0) { // red side of field
				extremeY = pos.y - botRadius;

			} else { // blue side of field
				extremeY = pos.y + botRadius;
			}

			return Math.abs(extremeX-FieldConstants.TILE_LENGTH_IN*2) >= Math.abs(extremeY);
		}

		public static boolean isInLegalShootingZone(Vector2d pos, double botRadius) {
			return isInNearShootingZone(pos, botRadius) || isInFarShootingZone(pos, botRadius);
		}

		public static String getShotZoneLabel(Vector2d pos, double botRadius) {
			if (isInNearShootingZone(pos, botRadius)) return "near";
			if (isInFarShootingZone(pos, botRadius)) return "far";

			return "none";
		}
	}
}
