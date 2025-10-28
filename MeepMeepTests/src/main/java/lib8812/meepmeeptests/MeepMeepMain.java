package lib8812.meepmeeptests;

import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import lib8812.meepmeeptests.odom.runners.near.MeepMeepBlueNear;

public class MeepMeepMain {
	public static void main(String[] args) {
		MeepMeep meepMeep = new MeepMeep(600);

		RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
				// Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
				.setConstraints(50, 40, Math.PI, Math.PI, 9.582759209747389)
				.build();

		DriveShim drive = myBot.getDrive();

		myBot.runAction(MeepMeepBlueNear.run(drive));

		meepMeep
				.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
				.setDarkMode(true)
				.setBackgroundAlpha(0.95f)
				.addEntity(myBot)
				.start();
	}
}