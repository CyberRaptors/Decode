package lib8812.common.robot;

/**
 * Simple & undetailed info about a Limelight's mount position to enable rudimentary distance-from-target calculation
 */
public class SimpleLimelightInfo {
	public final double mountDegreesFromVertical;
	public final double lensInchesFromGround;

	public SimpleLimelightInfo(double mountDegreesFromVertical, double lensInchesFromGround) {
		this.mountDegreesFromVertical = mountDegreesFromVertical;
		this.lensInchesFromGround = lensInchesFromGround;
	}
}
