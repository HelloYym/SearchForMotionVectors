package SearchForMotionVectors;

import java.awt.image.BufferedImage;

public interface SearchMethod {
	public MotionVectors searchMotionVectors(BufferedImage targetFrame, BufferedImage referenceFrame);
}
