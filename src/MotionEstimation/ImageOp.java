package MotionEstimation;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageOp {
	public static BufferedImage getGrayPicture(BufferedImage originalImage) {
		BufferedImage grayImage;
		int imageWidth = originalImage.getWidth();
		int imageHeight = originalImage.getHeight();

		grayImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < imageWidth; i++) {
			for (int j = 0; j < imageHeight; j++) {
				int rgb = originalImage.getRGB(i, j);
				grayImage.setRGB(i, j, rgb);
			}
		}
		return grayImage;
	}

	public static void saveImage(BufferedImage image, String filename) throws IOException {
		File newFile = new File(filename);
		ImageIO.write(image, "jpg", newFile);
	}

	public static BufferedImage resizeImage(final BufferedImage originalImg, int width, int height) {
		// 获取一个宽、长是原来scale的图像实例
		Image image = originalImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);

		// 缩放图像
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g = img.createGraphics();
		g.drawImage(image, 0, 0, null); // 绘制缩小后的图
		g.dispose();

		return img;
	}

	public static BufferedImage narrowImage(BufferedImage originalImg, int scale) {

		int width = originalImg.getWidth();
		int height = originalImg.getHeight();

		// 获取一个宽、长是原来scale的图像实例
		Image image = originalImg.getScaledInstance(width / scale, height / scale, Image.SCALE_SMOOTH);

		// 缩放图像
		BufferedImage img = new BufferedImage(width / scale, height / scale, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g = img.createGraphics();
		g.drawImage(image, 0, 0, null); // 绘制缩小后的图
		g.dispose();

		return img;
	}
}
