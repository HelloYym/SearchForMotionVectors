package MotionEstimation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class MontionVectorsSearcher {
	protected BufferedImage targetFrame;
	protected BufferedImage referenceFrame;
	public int macroBlockSize = 16;
	public int searchWindowP = 15;

	public ArrayList<MotionVector> searchMotionVectors() {
		ArrayList<MotionVector> mvList = new ArrayList<MotionVector>();
		for (int x = 0; x < targetFrame.getWidth(); x += macroBlockSize)
			for (int y = 0; y < targetFrame.getHeight(); y += macroBlockSize) {
				MotionVector mv = searchMotionVector(x, y);
				System.out.println(x + " " + y + " " + mv.u + " " + mv.v);
				mvList.add(mv);
			}

		return mvList;
	}

	public abstract MotionVector searchMotionVector(int x, int y);

	// 平均绝对误差
	public int calMAD(int x, int y, int i, int j) {
		int mad = 0;
		int sum = 0;
		for (int k = 0; k < macroBlockSize; k++)
			for (int l = 0; l < macroBlockSize; l++)
				try {
					sum += Math.abs((targetFrame.getRGB(x + k, y + l) & 0xff)
							- (referenceFrame.getRGB(x + i + k, y + j + l) & 0xff));
				} catch (Exception e) {
					return Integer.MAX_VALUE;
				}
		mad = (int) (sum / Math.pow(macroBlockSize, 2));
		return mad;
	}

	public void setTargetFrame(BufferedImage targetFrame) {
		this.targetFrame = ImageOp.getGrayPicture(targetFrame);
	}

	public void setReferenceFrame(BufferedImage referenceFrame) {
		this.referenceFrame = ImageOp.getGrayPicture(referenceFrame);
	}
}
