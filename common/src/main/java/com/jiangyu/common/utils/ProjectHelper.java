package com.jiangyu.common.utils;

import android.content.Context;
import android.view.ViewParent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ProjectHelper {
	/**
	 * 解压assets的zip压缩文件到指定目录

	 * @throws IOException
	 */
	public static void unZip(Context context, String assetName, String outputDirectory, boolean isReWrite) throws IOException {
		// 创建解压目标目录
		File file = new File(outputDirectory);
		// 如果目标目录不存在，则创建
		if (!file.exists()) {
			file.mkdirs();
		}
		// 打开压缩文件
		InputStream inputStream = context.getAssets().open(assetName);
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		// 读取一个进入点
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		// 使用1Mbuffer
		byte[] buffer = new byte[1024 * 1024];
		// 解压时字节计数
		int count = 0;
		// 如果进入点为空说明已经遍历完所有压缩包中文件和目录
		while (zipEntry != null) {
			// 如果是一个目录
			if (zipEntry.isDirectory()) {
				file = new File(outputDirectory + File.separator + zipEntry.getName());
				// 文件需要覆盖或者是文件不存在
				if (isReWrite || !file.exists()) {
					file.mkdir();
				}
			} else {
				// 如果是文件
				file = new File(outputDirectory + File.separator + zipEntry.getName());
				// 文件需要覆盖或者文件不存在，则解压文件
				if (isReWrite || !file.exists()) {
					file.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					while ((count = zipInputStream.read(buffer)) > 0) {
						fileOutputStream.write(buffer, 0, count);
					}
					fileOutputStream.close();
				}
			}
			// 定位到下一个文件入口
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
	}

	/**
	 * 将网络html保存到本地
	 * 
	 * @param url
	 *            html网络地址
	 * @param path
	 */
	public static void saveHtmlToSdcard(String url, String path) {
		try {
			URL myUrl = new URL(url);
			try {
				HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(5 * 1000);
				InputStream inputStream = connection.getInputStream();
				OutputStream outStream = new FileOutputStream(new File(path));
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 改变父空间触摸事件拦截状态
	 * 
	 * @param parentView
	 * @param isDisallow
	 */
	public static void changeParentDisallowInterceptState(ViewParent parentView, boolean isDisallow) {
		if (parentView == null) {
			return;
		}
		if (parentView.getParent() == null) {
			return;
		}
		// 改变触摸拦截状态
		parentView.requestDisallowInterceptTouchEvent(isDisallow);
		changeParentDisallowInterceptState(parentView.getParent(), isDisallow);
	}
}
