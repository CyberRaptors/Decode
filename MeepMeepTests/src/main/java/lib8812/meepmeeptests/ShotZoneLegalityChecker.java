package lib8812.meepmeeptests;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.ui.WindowFrame;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import lib8812.meepmeeptests.stubs.ActionableRaptorRobotStub;

public class ShotZoneLegalityChecker {
	public static void run(MeepMeep meepMeep, RoadRunnerBotEntity botEntity, DriveShim drive, boolean onBlueTeam) {
		Canvas canvas = meepMeep.getCanvas();
		WindowFrame frame = meepMeep.getWindowFrame();

		ActionableRaptorRobotStub bot = new ActionableRaptorRobotStub();

		final boolean[] forward = new boolean[1];
		final boolean[] backward = new boolean[1];
		final boolean[] left = new boolean[1];
		final boolean[] right = new boolean[1];

		canvas.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent keyEvent) { }

			@Override
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
					forward[0] = true;
				} else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
					backward[0] = true;
				} else if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
					left[0] = true;
				} else if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
					right[0] = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_W) {
					forward[0] = false;
				} else if (keyEvent.getKeyCode() == KeyEvent.VK_S) {
					backward[0] = false;
				} else if (keyEvent.getKeyCode() == KeyEvent.VK_A) {
					left[0] = false;
				} else if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
					right[0] = false;
				}
			}
		});

		while (true) {
			frame.setTitle(
					bot.canShootFromCurrentPosition(drive, onBlueTeam) ? "can shoot from current position" : "cannot shoot from current pos"
			);

			Vector2d target = drive.getPoseEstimate().position;

			if (forward[0]) {
				target = target.plus(new Vector2d(0, 0.25));
			}

			if (backward[0]) {
				target = target.plus(new Vector2d(0, -0.25));
			}

			if (left[0]) {
				target = target.plus(new Vector2d(-0.25, 0));
			}

			if (right[0]) {
				target = target.plus(new Vector2d(0.25, 0));
			}

			Action action = drive.actionBuilder(drive.getPoseEstimate())
					.strafeTo(target)
					.build();

			botEntity.runAction(action);
		}
	}
}
