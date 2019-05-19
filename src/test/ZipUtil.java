package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 通过Java的Zip输入输出流实现压缩和解压文件
 * 
 * 
 */
public final class ZipUtil {
	private static final int BUFFER_SIZE = 1024;
	//解压到当前目录
	public static File unzipToCur(String filePath){
		int i = filePath.lastIndexOf(".");
		String dest = filePath.substring(0,i);
		boolean unzip = unzip(filePath, dest);
		if(unzip){
			return new File(dest);
		}
		return null;
	}
	/**
	 * 解压文件
	 * 
	 * @param filePath
	 *            压缩文件路径
	 */
	public static boolean unzip(String filePath,String destDirPath) {
		File source = new File(filePath);
		return unzip(source,destDirPath);
	}
	/**
	 * 解压文件
	 *
	 * @param filePath
	 *            压缩文件路径
	 */
	public static boolean unzip(File source,String  destDirPath) {
		long start = System.currentTimeMillis();
		if (!source.exists()) {
			System.out.println("文件不存在！");
			return false;
		}
		ZipFile zipFile = null;
			try {
				zipFile = new ZipFile(source);
				Enumeration<?> entries = zipFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();
					System.out.println("解压" + entry.getName());
					// 如果是文件夹，就创建个文件夹
					if (entry.isDirectory()) {
						String dirPath = destDirPath + File.separator + entry.getName();
						File dir = new File(dirPath);
						dir.mkdirs();
					} else {
						// 如果是文件，就先创建一个文件，然后用io流把内容copy过去
						File targetFile = new File(destDirPath + File.separator + entry.getName());
						// 保证这个文件的父文件夹必须要存在
						if(!targetFile.getParentFile().exists()){
							targetFile.getParentFile().mkdirs();
						}
						targetFile.createNewFile();
						// 将压缩文件内容写入到这个文件中
						InputStream is = zipFile.getInputStream(entry);
						FileOutputStream fos = new FileOutputStream(targetFile);
						int len;
						byte[] buf = new byte[BUFFER_SIZE];
						while ((len = is.read(buf)) != -1) {
							fos.write(buf, 0, len);
						}
						// 关流顺序，先打开的后关闭
						fos.close();
						is.close();
				}
				}
				long end = System.currentTimeMillis();
				System.out.println("解压完成，耗时：" + (end - start) +" ms");
				System.out.println("解压缩成功！");
				return true;
			} catch (Exception e) {
				System.out.println("解压缩异常！");
				return false;
			}finally {
				if(zipFile != null){
					try {
						zipFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		return dirFile.delete();
	}
	/**
	 * 删除单个文件
	 *
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String path) {

		File file = new File(path);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}
	public static void main(String[] args) {
		/*int i = "C:\\Users\\rsq0113\\Desktop\\移动充值\\S_PFM_Recharge_1.0.0.zip".lastIndexOf(File.separator);
		System.out.println(i);*/
		/*unzipToCur("C:\\Users\\rsq0113\\Desktop\\移动充值\\aa\\S_PFM_Recharge_1.0.0.zip");*/
		File file = new File("C:\\Users\\rsq0113\\Desktop\\移动充值\\aa\\S_PFM_Recharge_1.0.0.ear");
		System.out.println(file.getPath());
	}
	}