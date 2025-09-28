package lib8812.common.field;

public class GameConstants {
	public static class DECODE {
		public enum Motif
		{
			GPP,
			PGP,
			PPG
		}

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

		public static Motif getMotifFromAprilTagId(int id) {
			if (id > 23 || id < 21) throw new IllegalArgumentException("AprilTag id does not represent a motif!");

			return Motif.values()[id - 21];
		}
	}
}
