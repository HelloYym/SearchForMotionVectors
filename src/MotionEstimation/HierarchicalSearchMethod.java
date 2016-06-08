package MotionEstimation;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HierarchicalSearchMethod extends MontionVectorsSearcher {

	private int K = 3;
	ArrayList<BufferedImage> targetFrameLevels = new ArrayList<BufferedImage>();
	ArrayList<BufferedImage> referenceFrameLevels = new ArrayList<BufferedImage>();

	public MontionVectorsSearcher otherMethodSearcher;

	public ArrayList<MotionVector> searchMotionVectors() {

		// 获得 K 个级别的缩放图片
		targetFrameLevels.add(targetFrame);
		referenceFrameLevels.add(referenceFrame);

		for (int l = 1; l <= K; l++) {
			BufferedImage preTargetFrame = targetFrameLevels.get(l - 1);
			BufferedImage preReferenceFrame = referenceFrameLevels.get(l - 1);
			targetFrameLevels.add(ImageOp.narrowImage(preTargetFrame, 2));
			referenceFrameLevels.add(ImageOp.narrowImage(preReferenceFrame, 2));

			System.out.println(targetFrameLevels.get(l).getHeight());
		}

		// 创建一个其它搜索方法，用于最低级别的搜索
//		otherMethodSearcher = new LogarithmicSearchMethod();
		otherMethodSearcher = new SequentialSearchMethod();
		otherMethodSearcher.setReferenceFrame(referenceFrameLevels.get(K));
		otherMethodSearcher.setTargetFrame(targetFrameLevels.get(K));
		otherMethodSearcher.searchWindowP /= (1 << K);
		otherMethodSearcher.macroBlockSize /= (1 << K);

		ArrayList<MotionVector> mvList = new ArrayList<MotionVector>();
		for (int x = 0; x < targetFrame.getWidth(); x += macroBlockSize)
			for (int y = 0; y < targetFrame.getHeight(); y += macroBlockSize)
				mvList.add(searchMotionVector(x, y));
		return mvList;
	}

	@Override
	public MotionVector searchMotionVector(int x, int y) {
		// System.out.println(x + " " + y);

		int k = K;
		int xk = x / (1 << k);
		int yk = y / (1 << k);

		MotionVector mv = otherMethodSearcher.searchMotionVector(xk, yk);

		int uk = mv.u;
		int vk = mv.v;
		
		boolean last = false;

		while (!last) {

			xk *= 2;
			yk *= 2;
			uk *= 2;
			vk *= 2;
			k = k - 1;
			
			int minMAD = Integer.MAX_VALUE;
			for (int i = mv.u - 1; i <= mv.u + 1; i++)
				for (int j = mv.v - 1; j <= mv.v + 1; j++) {
					int curMAD = calMAD(xk, yk, i, j, k);
					if (curMAD < minMAD) {
						minMAD = curMAD;
						uk = i;
						vk = j;
					}
				}

			if (k == 0) last = true;
			
			mv.u = uk;
			mv.v = vk;

		}

		return mv;
	}

	// 平均绝对误差
	public int calMAD(int x, int y, int i, int j, int level) {
		int mad = 0;
		int sum = 0;
		BufferedImage targetFrame = targetFrameLevels.get(level);
		BufferedImage referenceFrame = referenceFrameLevels.get(level);

		for (int k = 0; k < macroBlockSize / (1 << level); k++)
			for (int l = 0; l < macroBlockSize / (1 << level); l++)
				try {
					sum += Math.abs(targetFrame.getRGB(x + k, y + l)
							& 0xff - referenceFrame.getRGB(x + i + k, y + j + l) & 0xff);
				} catch (Exception e) {
					return Integer.MAX_VALUE;
				}
		mad = (int) (sum / Math.pow(macroBlockSize, 2));
		return mad;
	}

}