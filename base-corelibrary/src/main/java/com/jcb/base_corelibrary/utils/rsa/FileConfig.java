
package com.jcb.base_corelibrary.utils.rsa;

import android.content.Context;


import com.jcb.base_corelibrary.BCHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 描述：配置文件通用管理方法
 * 
 * @author dgd
 */
public class FileConfig {


	/**
	 * 获取文件字节缓存
	 *
	 * @param name
	 *            文件名
	 * @return 返回字节
	 */
	public static byte[] getFileBuffer( String name) {
		InputStream in = openFileInputStream(name);
		if (in == null) {
			return null;
		}
		try {
			// 读取文件
			int length = in.available();
			if (length == 0) {
				return null;
			}
			byte[] buf = new byte[length];
			in.read(buf);
			// 返回
			return buf;
		} catch (IOException e) {
			// do nothing
		} finally {
			// 关闭流，释放资源
			try {
				in.close();
			} catch (IOException e) {
				// do nothing
			}
			in = null;
		}
		return null;
	}
	/**
	 * 获取文件输入流
	 *
	 * @param filename
	 *            文件名
	 */
	public static InputStream openFileInputStream(
			String filename) {
		Context context = BCHelper.getInstance().getContext();
		filename = checkFilePrefix(filename);
		InputStream is = null;
		if (context != null) {
			try {
				is = context.openFileInput(filename);
			} catch (FileNotFoundException e1) {
				is = openAssetsFileInputStream(filename);
			} catch (IllegalArgumentException e2) {
				try {
					is = context.getAssets().open(filename);
				} catch (FileNotFoundException e) {
					// TODO: handle exception
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
		}
		return is;
	}

	private static String checkFilePrefix(String filename) {
		if (filename.startsWith("/")) {
			filename = filename.substring(1);
		}
		return filename;
	}


	/**
	 * 读取默认assets目录下到default文件
	 *
	 * @param filename
	 * @return
	 */
	public static InputStream openAssetsFileInputStream(
			String filename) {
		try {
			return BCHelper.getInstance().getContext().getAssets().open(filename);
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
