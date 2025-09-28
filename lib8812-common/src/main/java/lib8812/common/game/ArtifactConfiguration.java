package lib8812.common.game;

import java.util.ArrayList;


public class ArtifactConfiguration {
	public static final ArtifactConfiguration PGP = new ArtifactConfiguration(new Artifact[] { Artifact.PURPLE, Artifact.GREEN, Artifact.PURPLE });
	public static final ArtifactConfiguration PPG = new ArtifactConfiguration(new Artifact[] { Artifact.PURPLE, Artifact.PURPLE, Artifact.GREEN });
	public static final ArtifactConfiguration GPP = new ArtifactConfiguration(new Artifact[] { Artifact.GREEN, Artifact.PURPLE, Artifact.PURPLE });


	private final ArrayList<Artifact> artifacts = new ArrayList<>();

	public ArtifactConfiguration() {}
	public ArtifactConfiguration(Artifact[] artifacts) {
		for (Artifact artifact : artifacts) {
			push(artifact);
		}
	}

	public ArtifactConfiguration copySelf() {
		ArtifactConfiguration copy = new ArtifactConfiguration();

		for (Artifact artifact : artifacts) {
			copy.push(artifact);
		}

		return copy;
	}

	public int numPurple() {
		int count = 0;

		for (Artifact artifact : artifacts) {
			if (artifact == Artifact.PURPLE) count++;
		}

		return count;
	}

	public int numGreen() {
		int count = 0;

		for (Artifact artifact : artifacts) {
			if (artifact == Artifact.GREEN) count++;
		}

		return count;
	}


	public Artifact first() {
		return artifacts.get(0);
	}

	public Artifact second() {
		return artifacts.get(1);
	}

	public Artifact third() {
		return artifacts.get(2);
	}

	public Artifact last() {
		return artifacts.get(artifacts.size() - 1);
	}

	public void rotateForwards() {
		if (artifacts.isEmpty()) return;

		Artifact first = artifacts.get(0);

		for (int i = 0; i < artifacts.size() - 1; i++) {
			artifacts.set(i, artifacts.get(i + 1));
		}

		artifacts.set(artifacts.size() - 1, first);
	}

	public void push(Artifact artifact) {
		if (artifacts.size() == 3) throw new IllegalStateException("ArtifactConfiguration is full, more artifacts violates game rules!");

		artifacts.add(artifact);
	}

	public void pop() {
		if (artifacts.isEmpty()) throw new IllegalStateException("ArtifactConfiguration is already empty!");

		artifacts.remove(artifacts.size() - 1);
	}

	public boolean isEmpty() {
		return artifacts.isEmpty();
	}

	public boolean equals(ArtifactConfiguration other) {
		return other.artifacts.equals(artifacts);
	}

	public int calculateForwardRotatesNeeded(ArtifactConfiguration other) {
		// impossible
		if (other.numGreen() != numGreen()) return -1;
		if (other.numPurple() != numPurple()) return -1;

		ArtifactConfiguration working = this.copySelf();

		for (int i = 0; i < 3; i++) {
			if (working.equals(other)) return i;

			working.rotateForwards();
		}

		return -1; // impossible to rotate to other configuration, this should be unreachable
	}
}
