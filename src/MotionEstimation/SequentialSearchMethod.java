package MotionEstimation;

public class SequentialSearchMethod extends MontionVectorsSearcher {

	@Override
	public MotionVector searchMotionVector(int x, int y) {
		
		int minMAD = calMAD(x, y, 0, 0);
		MotionVector mv = new MotionVector();
		
		for (int i = -searchWindowP; i < searchWindowP; i++)
			for (int j = -searchWindowP; j<searchWindowP; j++) {
				int curMAD = calMAD(x, y, i, j);
				if (curMAD < minMAD) {
					minMAD = curMAD;
					mv.u = i;
					mv.v = j;
				}
			}

		return mv;
	}

}