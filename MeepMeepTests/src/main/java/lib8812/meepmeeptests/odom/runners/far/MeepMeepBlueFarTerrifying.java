package lib8812.meepmeeptests.odom.runners.far;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.stubs.game.CommonPoses;

public class MeepMeepBlueFarTerrifying {
	static ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();
	static Action main;

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(CommonPoses.INITIAL_BLUE_FAR_POSE);

		// SimplyCompat

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_BLUE_FAR_POSE)
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.strafeToSplineHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE.position,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpikeAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.splineToSplineHeading(
						CommonPoses.BLUE_THIRD_SPIKE_START_POSE,
						CommonPoses.BLUE_THIRD_SPIKE_START_POSE.heading
				)
				.splineToSplineHeading(
						CommonPoses.BLUE_THIRD_SPIKE_END_POSE,
						CommonPoses.BLUE_THIRD_SPIKE_END_POSE.heading,
						bot.SPIKE_PICKUP_VEL_CONSTRAINT
				)
				.afterTime(1, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.splineToLinearHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFromLoadingZoneAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.strafeToLinearHeading(
						CommonPoses.BLUE_FAR_FRIGHTENING_PICKUP_FROM_LOADING_ZONE_POSE.position,
						CommonPoses.BLUE_FAR_FRIGHTENING_PICKUP_FROM_LOADING_ZONE_POSE.heading
				)
				.stopAndAdd(new SleepAction(2.5))
				.afterTime(1, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.strafeToLinearHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE.position,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFromSecretTunnelAndMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.strafeToLinearHeading(
						CommonPoses.BLUE_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE.position,
						CommonPoses.BLUE_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE.heading
				)
				.stopAndAdd(new SleepAction(2.5))
				.afterTime(1, bot.setIntakeAndTransferPower(0))
				.afterTime(0, bot.startShootersAsync(bot.SHOOTER_VELO_FOR_FAR_SHOT))
				.strafeToLinearHeading(
						CommonPoses.BLUE_FAR_SHOT_POSE.position,
						CommonPoses.BLUE_FAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFromSecretTunnelAndPark =  drive.actionBuilder(CommonPoses.BLUE_FAR_SHOT_POSE)
				.afterTime(0, bot.setIntakePower(1))
				.afterTime(0, bot.setTransferPower(0.15))
				.strafeToLinearHeading(
						CommonPoses.BLUE_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE.position,
						CommonPoses.BLUE_FAR_TERRIFYING_PICKUP_FROM_SECRET_TUNNEL_POSE.heading
				)
				.stopAndAdd(new SleepAction(2.5))
				.afterTime(1, bot.setIntakeAndTransferPower(0))
				.build();
		
		main = new SequentialAction(
				initialMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				pickupFirstSpikeAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				pickupFromLoadingZoneAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				pickupFromSecretTunnelAndMoveToShoot,
				bot.shootThree(bot.SHOOTER_VELO_FOR_FAR_SHOT),
				bot.disableShootersAsync(),

				pickupFromSecretTunnelAndPark
		);

		return main;
	}
}
