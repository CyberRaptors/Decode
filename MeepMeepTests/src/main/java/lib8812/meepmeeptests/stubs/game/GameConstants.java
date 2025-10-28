package lib8812.meepmeeptests.stubs.game;

public class GameConstants {
	public static class DECODE {
		public static final int RED_GOAL_APRILTAG_ID = 24;
		public static final int BLUE_GOAL_APRILTAG_ID = 20;

		public static final int MOTIF_GPP_APRILTAG_ID = 21;
		public static final int MOTIF_PGP_APRILTAG_ID = 22;
		public static final int MOTIF_PPG_APRILTAG_ID = 23;

		public static int GOAL_APRILTAG_ID(String alliance) {
			if (alliance.equals("blue")) return GOAL_APRILTAG_ID(true);
			if (alliance.equals("red")) return GOAL_APRILTAG_ID(false);

			throw new IllegalArgumentException("Invalid alliance specified!");
		}

		public static int GOAL_APRILTAG_ID(boolean blue) {
			if (blue) return BLUE_GOAL_APRILTAG_ID;

			return RED_GOAL_APRILTAG_ID;
		}

		public static ArtifactConfiguration getMotifFromAprilTagId(int id) {
			if (id == MOTIF_GPP_APRILTAG_ID) return ArtifactConfiguration.GPP;
			if (id == MOTIF_PGP_APRILTAG_ID) return ArtifactConfiguration.PGP;
			if (id == MOTIF_PPG_APRILTAG_ID) return ArtifactConfiguration.PPG;

			throw new IllegalArgumentException("AprilTag id does not represent a motif!");
		}
	}
}
