package com.jiangyu.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * <p>Title: 图片工具类 </p>
 * <p> Description:</p>
 * <p>1.将图片压缩到某个尺寸</p>
 * <p>2.用最省内存的方式读取本地sd卡图片</p>
 * <p>3.字节数组转化成bitmap</p>
 * <p>4.bitmap转化成字节数组</p>
 * <p>5.Drawable 转 bitmap</p>
 * <p>6.图片缩放到指定宽高</p>
 * <p>7.将图片压缩到指定大小</p>
 * <p>8.缓存图片文件到SD卡</p>
 * <p>9.图片去色,返回灰度图片</p>
 * <p>10.把图片变成圆角</p>
 * <p>11.回收图片资源</p>
 *
 */
public class BitmapUtil {
	/**
	 * 将图片压缩到某个尺寸
	 * 
	 * @param path
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path, int size) throws IOException {
		// 取得图片
		BufferedInputStream temp = new BufferedInputStream(new FileInputStream(path));
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
		options.inJustDecodeBounds = true;
		// 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
		BitmapFactory.decodeStream(temp, null, options);
		// 关闭流
		temp.close();
		// 生成压缩的图片
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			// 这一步是根据要设置的大小，使宽和高都能满足
			if ((options.outWidth >> i <= size) && (options.outHeight >> i <= size)) {
				// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
				temp = new BufferedInputStream(new FileInputStream(path));
				// 这个参数表示 新生成的图片为原始图片的几分之一。
				options.inSampleSize = (int) Math.pow(2.0D, i);
				// 这里之前设置为了true，所以要改为false，否则就创建不出图片
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(temp, null, options);
				temp.close();
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static Bitmap convertToBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Config.ARGB_8888;
		// 返回为空
		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	/**
	 * 用最省内存的方式读取本地sd卡图片（注：使用次方法的图片在内存不够的时候会被自动回收,如果图片正被activity使用，activity也会被回收
	 * ）
	 *
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Bitmap readBitMapByLowMemory(String filePath) throws FileNotFoundException {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		FileInputStream is = new FileInputStream(filePath);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * Byte转BMP
	 *
	 * @param bytes
	 * @param opts
	 * @return
	 */
	public static Bitmap bytes2Bitmap(byte[] bytes, BitmapFactory.Options opts) {
		if (bytes != null) {
			if (opts != null) {
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
			} else {
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}
		}
		return null;
	}

	/**
	 * BMP转Byte
	 *
	 * @param bm
	 * @return
	 */
	public static byte[] bitmap2Bytes(Bitmap bm, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bm.recycle();
		}
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Drawable 转 bitmap
	 *
	 * @param drawable
	 *
	 * @return
	 */
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		} else if (drawable instanceof NinePatchDrawable) {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
					: Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * 图片缩放
	 *
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		Bitmap newbmp = null;
		try {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidth = ((float) width / w);
			float scaleHeight = ((float) height / h);
			matrix.postScale(scaleWidth, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newbmp;
	}

	/**
	 * 图片缩放
	 *
	 * @param bitmap
	 * @param width
	 * @param canZoomIn
	 *            是否允许放大
	 * @return
	 */
	public static Bitmap zoomBitmapWithWidth(Bitmap bitmap, int width, boolean canZoomIn) {
		Bitmap result = null;
		try {
			int w = bitmap.getWidth();
			if (!canZoomIn && w < width) {
				result = bitmap;
			} else {
				Matrix matrix = new Matrix();
				float scale = (float) width / w;
				matrix.postScale(scale, scale);
				result = Bitmap.createBitmap(bitmap, 0, 0, w, bitmap.getHeight(), matrix, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 图片缩放
	 *
	 * @param bitmap
	 * @param maxsize
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int maxsize) {
		Bitmap newbmp = null;
		try {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidth = 1;
			float scaleHeight = 1;
			if (w > maxsize || h > maxsize) {
				if (w > h) {
					scaleWidth = ((float) maxsize / w);
					scaleHeight = scaleWidth;
				} else {
					scaleHeight = ((float) maxsize / h);
					scaleWidth = scaleHeight;
				}
			} else {
				return bitmap;
			}
			matrix.postScale(scaleWidth, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!bitmap.isRecycled()) {
			// 如果没有回收
			bitmap.recycle();
		}
		return newbmp;
	}

	/**
	 * 将图片压缩到指定大小
	 *
	 * @param originalBitmap
	 * @param size
	 * @return
	 */
	public static Bitmap revitionImageSize(Bitmap originalBitmap, int maxSize) {
		if (originalBitmap == null) {
			return null;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int options = 100;
			originalBitmap.compress(CompressFormat.JPEG, options, baos);
			while (baos.toByteArray().length / 1024 > maxSize) {
				baos.reset();
				if (options <= 0)
					break;
				originalBitmap.compress(CompressFormat.JPEG, options, baos);
				options -= 10;
			}
			// 再次压缩尺寸
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
			return bitmap;
		} catch (OutOfMemoryError oom) {
		} finally {
			if (originalBitmap != null && !originalBitmap.isRecycled()) {
				originalBitmap.recycle();
				originalBitmap = null;
			}
		}
		return null;
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 *
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 缓存图片文件到SD卡(PNG格式)
	 *
	 * @param bitmap
	 * @param filename
	 * @return 保存成功与否
	 */
	public static boolean saveBitmap(Bitmap bitmap, String filename) {
		return saveBitmap(bitmap, filename, CompressFormat.PNG);
	}

	/**
	 * 缓存图片文件到SD卡
	 *
	 * @param bitmap
	 * @param filename
	 * @param expandedName
	 *            扩展名，参见Bitmap.CompressFormat
	 * @return 保存成功与否
	 */
	public static boolean saveBitmap(Bitmap bitmap, String filename, CompressFormat format) {
		if (bitmap != null) {
			File file = new File(filename);
			file.getParentFile().mkdirs();
			FileOutputStream fileOut = null;
			try {
				fileOut = new FileOutputStream(filename);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			bitmap.compress(format, 80, fileOut);
			try {
				fileOut.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fileOut.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 图片去色,返回灰度图片
	 *
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	 
	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels, int width, int height) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels, width, height);
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @param width
	 *            输出的图片宽度
	 * @param height
	 *            输出的图片的高度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels, int width, int height) {
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		Rect rect2 = new Rect(0, 0, width, height);
		RectF rectF = new RectF(rect2);
		float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect2, paint);
		return output;
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(Resources.getSystem(), toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable, int pixels, int width, int height) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(Resources.getSystem(), toRoundCorner(bitmap, pixels, width, height));
		return bitmapDrawable;
	}

	/**
	 * 回收bitmap(在调用后若返回的是true,建议将此bitmap设置为null)
	 * 
	 * @param bitmap
	 * @return
	 */
	public static boolean recycleBitmap(Bitmap bitmap) {
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
			// System.gc();
			return true;
		}
		return false;
	}
	
	
}
