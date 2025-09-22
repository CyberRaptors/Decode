package lib8812.common.field;

public class GameConstants {
	public static class DECODE {
		public static final int RED_GOAL_APRILTAG_ID = 0;
		public static final int BLUE_GOAL_APRILTAG_ID = 0;
		public static final int MOTIF_APRILTAG_ID = 0;

		public static int GOAL_APRILTAG_ID(String alliance) {
			if (alliance.equals("blue")) return GOAL_APRILTAG_ID(true);
			if (alliance.equals("red")) return GOAL_APRILTAG_ID(false);

			throw new IllegalArgumentException("Invalid alliance specified!");
		}

		public static int GOAL_APRILTAG_ID(boolean blue) {
			if (blue) return BLUE_GOAL_APRILTAG_ID;

			return RED_GOAL_APRILTAG_ID;
		}
	}
}
