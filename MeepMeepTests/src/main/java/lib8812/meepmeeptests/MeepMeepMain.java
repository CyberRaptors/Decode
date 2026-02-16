package lib8812.meepmeeptests;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import lib8812.meepmeeptests.odom.runners.far.MeepMeepBlueFarFrightening;

public class MeepMeepMain {
	public static void main(String[] args) {
		MeepMeep meepMeep = new MeepMeep(600);

		RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
				// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
				.setConstraints(60, 60, Math.PI, Math.PI, 9.582759209747389)
				.build();

		DriveShim drive = myBot.getDrive();

		myBot.runAction(
				MeepMeepBlueFarFrightening.run(drive)
//				new SequentialAction(
//						MeepMeepBlueNearTame.run(drive),
//						MeepMeepBlueNear.run(drive),
//						MeepMeepBlueNearScary.run(drive),
//						MeepMeepBlueNearTerrifying.run(drive),
//						MeepMeepBlueNearMonster.run(drive),
//
//						MeepMeepBlueFarTame.run(drive),
//						MeepMeepBlueFar.run(drive),
//						MeepMeepBlueFarScary.run(drive),
//						MeepMeepBlueFarTerrifying.run(drive),
//						MeepMeepBlueFarMonster.run(drive)
//				)
		);

		meepMeep
				.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
				.setDarkMode(true)
				.setBackgroundAlpha(0.95f)
				.addEntity(myBot);

//		WindowFrame windowFrame = meepMeep.getWindowFrame();
//
//		windowFrame.setLocation(
//				-windowFrame.getWidth(),
//				375
//		);

		meepMeep.start();

//		ShotZoneLegalityChecker.run(meepMeep, myBot, drive, true);
	}
}