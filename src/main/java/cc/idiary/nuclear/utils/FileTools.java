package cc.idiary.nuclear.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Neo
 *
 */
public class FileTools {

	/**
	 * 保存图片，并判断尺寸是否符合要求
	 * 
	 * @param is
	 * @param path
	 * @param maxWidth
	 * @param maxHeight
	 * @throws IOException
	 */
	public static void saveImage(InputStream is, String path, String name,String formatName,
			int maxWidth, int maxHeight) throws IOException {
		BufferedImage bImage = ImageIO.read(is);
		if (maxWidth < bImage.getWidth()) {
//			throw new IOException("the width is larger than maxWidth");
			throw new IOException("宽度过长");
		}
		if (maxHeight < bImage.getHeight()) {
//			throw new IOException("the height is larger than maxHeight");
			throw new IOException("高度过长");
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();  
		ImageIO.write(bImage, formatName, os);  
		InputStream is2 = new ByteArrayInputStream(os.toByteArray());  
		saveFile(is2, path, name, true);
	}

	/**
	 * 将文件以原始文件名存储到相对网站根路径的制定路径
	 * 
	 * @param path
	 * @param mFile
	 * @return
	 * @throws IOException
	 */
	public static void saveFile(MultipartFile mFile, String path) throws IOException {

		if (mFile.isEmpty()) {
			throw new IOException("MultipartFile have no file.");
		}
		path = path.trim();
		String fileName = mFile.getOriginalFilename();
		InputStream is = mFile.getInputStream();
		saveFile(is, path, fileName, true);
	}

	/**
	 * 存储文件到指定路径
	 * 
	 * @param is
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static void saveFile(InputStream is, String path, String name,
			boolean mkdir) throws IOException {

		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}

		if (mkdir) {
			File fp = new File(path);
			if (!fp.exists()) {
				fp.mkdirs();
			}
		}

		FileOutputStream fos = new FileOutputStream(path + "/" + name);
		byte[] bs = new byte[1024];
		int len = 0;
		while ((len = is.read(bs)) != -1) {
			fos.write(bs, 0, len);
		}
		fos.flush();
		fos.close();
		is.close();
	}
}
