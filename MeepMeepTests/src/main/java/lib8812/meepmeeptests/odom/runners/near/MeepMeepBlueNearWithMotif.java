package lib8812.meepmeeptests.odom.runners.near;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.noahbres.meepmeep.roadrunner.DriveShim;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;
import lib8812.meepmeeptests.stubs.CommonPoses;
import lib8812.meepmeeptests.stubs.game.ArtifactConfiguration;

public class MeepMeepBlueNearWithMotif {
	static ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();

	public static Action run(DriveShim drive) {
		drive.setPoseEstimate(CommonPoses.INITIAL_BLUE_NEAR_POSE_FOR_MOTIF_AUTO);

		Action initialMoveToShoot = drive.actionBuilder(CommonPoses.INITIAL_BLUE_NEAR_POSE_FOR_MOTIF_AUTO)
				.strafeToConstantHeading(
						CommonPoses.BLUE_NEAR_MOTIF_READ_POS.position
				)
				.afterDisp(5, bot.storeMotif())
				.afterDisp(5, bot.sortToMotif())
				.splineToSplineHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE, CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.build();

		Action pickupFirstSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
						.strafeToSplineHeading(
								CommonPoses.BLUE_FIRST_SPIKE_START_POSE.position,
								CommonPoses.BLUE_FIRST_SPIKE_START_POSE.heading
						)
						.build(),
				bot.setIntakeGroupPower(1),
				drive.actionBuilder(CommonPoses.BLUE_FIRST_SPIKE_START_POSE)
						.afterDisp(5, bot.setRailDriveTwoPower(1))
						.strafeToSplineHeading(
								CommonPoses.BLUE_FIRST_SPIKE_END_POSE.position,
								CommonPoses.BLUE_FIRST_SPIKE_END_POSE.heading,
								bot.SPIKE_PICKUP_VEL_CONSTRAINT
						)
						.build(),
				bot.setIntakeGroupPower(0),
				new SleepAction(0.5),
				bot.setRailDriveTwoPower(0)
		);

		Action secondMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_FIRST_SPIKE_END_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE.position,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.afterDisp(0, bot.sortToMotif())
				.build();

		Action pickupSecondSpike = new SequentialAction(
				drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
						.strafeToSplineHeading(
								CommonPoses.BLUE_SECOND_SPIKE_START_POSE.position,
								CommonPoses.BLUE_SECOND_SPIKE_START_POSE.heading
						)
						.build(),
				bot.setIntakeGroupPower(1),
				drive.actionBuilder(CommonPoses.BLUE_SECOND_SPIKE_START_POSE)
						.afterDisp(5, bot.setRailDriveTwoPower(1))
						.strafeToSplineHeading(
								CommonPoses.BLUE_SECOND_SPIKE_END_POSE.position,
								CommonPoses.BLUE_SECOND_SPIKE_END_POSE.heading,
								bot.SPIKE_PICKUP_VEL_CONSTRAINT
						)
						.build(),
				bot.setIntakeGroupPower(0),
				new SleepAction(0.5),
				bot.setRailDriveTwoPower(0)
		);

		Action thirdMoveToShoot = drive.actionBuilder(CommonPoses.BLUE_SECOND_SPIKE_END_POSE)
				.strafeToSplineHeading(
						CommonPoses.BLUE_NEAR_SHOT_POSE.position,
						CommonPoses.BLUE_NEAR_SHOT_POSE.heading
				)
				.afterDisp(0, bot.sortToMotif())
				.build();

		Action park = drive.actionBuilder(CommonPoses.BLUE_NEAR_SHOT_POSE)
				.strafeTo(CommonPoses.BLUE_NEAR_PARK_POS)
				.build();

		Action main = new SequentialAction(
				initialMoveToShoot,
				bot.successiveShootWithVelo(2, bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				pickupFirstSpike,
				bot.setInternalArtifactConfig(ArtifactConfiguration.PPG),
				secondMoveToShoot,
				bot.successiveShootWithVelo(3, bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				pickupSecondSpike,
				bot.setInternalArtifactConfig(ArtifactConfiguration.PGP),
				thirdMoveToShoot,
				bot.successiveShootWithVelo(3, bot.SHOOTER_VELO_FOR_CLOSE_SHOT),
				park
		);

		return main;
	}
}
