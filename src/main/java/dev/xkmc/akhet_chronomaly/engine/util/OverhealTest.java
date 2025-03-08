package dev.xkmc.akhet_chronomaly.engine.util;

public class OverhealTest {

	private float overheal;

	public OverhealTest(float overheal) {
		this.overheal = overheal;
	}

	public float consume() {
		float ans = overheal;
		overheal = 0;
		return ans;
	}

}
