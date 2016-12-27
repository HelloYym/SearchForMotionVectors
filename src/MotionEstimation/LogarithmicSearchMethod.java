package MotionEstimation;

import java.util.ArrayList;

public class LogarithmicSearchMethod extends MontionVectorsSearcher {

	
	@Override
	public MotionVector searchMotionVector(int x, int y) {

//		System.out.println(x + " " + y);

		int minMAD = calMAD(x, y, 0, 0);
		MotionVector mv = new MotionVector();

		int offset = (int) Math.ceil((double)searchWindowP/2);
		boolean last = false;
		
		int cx = 0;
		int cy = 0;
		
		while (!last) {
			if (x == 0 && y == 0)
			System.out.println("cx cy : " + cx + " " + cy);
			
			for (int i = -offset; i <= offset; i+=offset)
				for (int j = -offset; j <= offset; j+=offset) {
//					System.out.println("cur : " + (i + cx) + " " + (j + cy));
					int curMAD = calMAD(x, y, i + cx, j + cy);
					if (curMAD < minMAD) {
						minMAD = curMAD;
						mv.u = i + cx;
						mv.v = j + cy;
					}
				}
			if (offset == 1) last = true;
			offset = (int) Math.ceil((double)offset/2);
			
			cx = mv.u;
			cy = mv.v;
				
		}
		
		

		return mv;
	}

}