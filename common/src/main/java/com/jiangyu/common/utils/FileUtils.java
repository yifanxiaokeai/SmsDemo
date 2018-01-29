package com.jiangyu.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.Comparator;

/**
 * <p>Title: File工具类 </p>
 * <p> Description:</p>
 * <p>1.判断SD卡是否存在</p>
 * <p>2.获取SD卡剩余空间</p>
 * <p>3.获取SD卡总容量</p>
 * <p>4.判断SD卡上文件是否存在</p>
 * <p>5.拷贝文件</p>
 * <p>6.写文件，保存到指定路径</p>
 * <p>7.删除文件</p>
 * <p>8. 删除文件夹下的文件</p>
 * <p>11.根据文件的最后修改时间进行排序</p>
 * <p>12.把文件转为byte[]</p>
 * <p>13.获取路径的文件名</p>
 * <p>14.取得文件名,无后缀</p>
 * <p>15.保存bmp文件到sd卡</p>
 */
public final class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    public static boolean existSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡剩余空间
     *
     * @return
     */
    public long getSDFreeSize() {
        if (!existSDCard())
            return 0;
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 获取SD卡总容量
     *
     * @return
     */
    public long getSDAllSize() {
        if (!existSDCard())
            return 0;
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        // return allBlocks * blockSize; //单位Byte
        // return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 判断SD卡上文件是否存在
     *
     * @param filepath
     * @return
     */
    public static boolean fileExists(String filepath) {
        File f = new File(filepath);
        return f.exists();
    }

    /**
     * 拷贝文件
     *
     * @param file
     * @param dir
     * @param fileName
     */
    public static void copy(File file, String dir, String fileName) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            write(in, dir, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写文件
     *
     * @param in
     * @param dir
     * @param fileName
     * @return
     */
    public static File write(InputStream in, String dir, String fileName) {
        if (in == null) {
            return null;
        }
        String absolutePath = dir;
        File f = new File(absolutePath);
        if (!f.exists()) {
            if (f.mkdirs()) {
                Log.d(TAG, "mkdirs error:" + absolutePath);
            }
        }
        File mf = new File(absolutePath + "/" + fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(mf);
            byte bt[] = new byte[512];
            int n = -1;
            while (true) {
                n = in.read(bt);
                if (n <= 0)
                    break;
                out.write(bt, 0, n);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            absolutePath = null;
        }
        return mf;
    }

    /**
     * Serialize the object
     */
    public static void write(Context context, String fileName, Object obj) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            out.writeObject(obj);
            out.flush();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException error, message:" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException error, message:" + e.getMessage());
        } finally {
            StreamUtil.closeSilently(out);
        }
    }

    /**
     * Deserialize the Object
     */
    public static Object read(Context context, String fileName) {
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(context.openFileInput(fileName));
            return input.readObject();
        } catch (StreamCorruptedException e) {
            Log.e(TAG, " error message:" + e.getMessage());
        } catch (FileNotFoundException e) {
            Log.e(TAG, " error message:" + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, " error message:" + e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, " error message:" + e.getMessage());
        } finally {
            StreamUtil.closeSilently(input);
        }
        return null;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String fileName) {
        boolean result = false;
        if (!TextUtils.isEmpty(fileName)) {
            try {
                File file = new File(fileName);
                if (file.exists()) {
                    result = file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 删除文件夹下的文件
     *
     * @param folderPath
     * @return
     */
    public static boolean deleteFilesInFolder(String folderPath) {
        boolean result = false;
        if (!TextUtils.isEmpty(folderPath)) {
            try {
                File path = new File(folderPath);
                if (path.exists() && path.isDirectory()) {
                    result = true;
                    File[] files = path.listFiles();
                    if (files != null && files.length > 0) {
                        for (int i = files.length - 1; i >= 0; i--) {
                            if (!files[i].delete() && result) {
                                result = false;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }


    /**
     * 根据文件的最后修改时间进行排序
     */
    static class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /**
     * 获取SD卡根目录
     *
     * @return
     */
    public static String getSdcardDir() {
//		if (Environment.getExternalStorageDirectory().canWrite()) {
//			return Environment.getExternalStorageDirectory().getPath();
//		} else {
//			return null;
//		}
        return Environment.getExternalStorageDirectory().toString();
    }


    /**
     * 修改文件的最后修改时间
     *
     * @param dir
     * @param fileName
     */
    public static void updateFileTime(String dir, String fileName) {
        File file = new File(dir, fileName);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 把文件转为byte[]
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        // 获取文件大小
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // 文件太大，无法读取
            is.close();
            throw new IOException("File is to large " + file.getName());
        }
        // 创建一个数据来保存文件数据
        byte[] bytes = new byte[(int) length];
        // 读取数据到byte数组中
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    /**
     * 取得文件名(从url或本地文件路径)
     *
     * @param path
     * @param ignorExtention 是否忽略后缀
     * @return
     */
    public static String getFileName(String path, boolean ignorExtention) {
        String result = path;
        if (!StringUtil.isEmpty(path)) {
            int startIndex = 0;
            if (result.contains("\\")) {
                startIndex = result.lastIndexOf("\\") + 1;
            } else if (result.contains("/")) {
                startIndex = result.lastIndexOf("/") + 1;
            }
            int endIndex = ignorExtention ? result.lastIndexOf(".") : result.length();
            result = result.substring(startIndex, endIndex);
        }
        return result;
    }

    /**
     * 取得文件名,无后缀
     *
     * @param file
     * @return
     */
    public static String getFileName(File file) {
        String result = "";
        try {
            result = file.getName().substring(0, file.getName().lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 取得文件名,无后缀
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        return getFileName(path, true);
    }

    /**
     * 取得文件后缀名
     *
     * @param file
     * @return
     */
    public static String getFileExtension(File file) {
        String result = "";
        try {
            result = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 取得文件后缀名
     *
     * @param file
     * @return
     */
    public static String getFileExtension(String path) {
        if (!StringUtil.isEmpty(path)) {
            if (path.contains(".")) {
                return path.substring(path.lastIndexOf(".") + 1);
            } else {
                return path;
            }
        } else {
            return "";
        }
    }

    /**
     * 保存bmp文件到sd卡
     *
     * @param bitmap
     * @param dir
     * @param fileName
     * @param savepath
     * @return
     */
    public static File save(final Bitmap bitmap, String dir, String fileName, String savepath) {
        if (bitmap == null)
            return null;
        String absolutePath = dir;
        File mf = new File(absolutePath + "/" + fileName);
        File f = new File(absolutePath);
        File f1 = new File(savepath + "/" + ".nomedia");
        if (!f.exists()) {
            if (f.mkdirs()) {
                Log.d(TAG, "mkdirs error:" + absolutePath);
            }
        }
        if (!f1.exists()) {
            try {
                f1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "mkfile error:" + savepath + "/" + ".nomedia");
            }
        }
        OutputStream outputStream = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, out);
        byte[] jpegData = out.toByteArray();
        try {
            outputStream = new FileOutputStream(mf);
            outputStream.write(jpegData);
            jpegData = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "FileNotFoundException:" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException:" + e.getMessage());
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }
        return mf;
    }

    /**
     * 保存bmp文件到sd卡
     *
     * @param bitmapByte
     * @param dir
     * @param fileName
     * @param savepath
     * @return
     */
    public static File save(byte[] bitmapByte, String dir, String fileName, String savepath) {
        if (bitmapByte == null)
            return null;
        String absolutePath = dir;
        File mf = new File(absolutePath + "/" + fileName);
        File f = new File(absolutePath);
        File f1 = new File(savepath + "/" + ".nomedia");
        if (!f.exists()) {
            if (f.mkdirs()) {
                Log.d(TAG, "mkdirs error:" + absolutePath);
            }
        }
        if (!f1.exists()) {
            try {
                f1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "mkfile error:" + savepath + "/" + ".nomedia");
            }
        }
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(mf);
            outputStream.write(bitmapByte);
            bitmapByte = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "FileNotFoundException:" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException:" + e.getMessage());
        }
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputStream = null;
        }
        return mf;
    }

    /**
     * 判断本地是否存在该文件夹，不存在则创建
     *
     * @param path
     */
    public static void checkOrMakeDirs(String path) {
        if (path == null) {
            return;
        }
        if (existSDCard()) {
            if (fileExists(path)) {
                return;
            } else {
                File file = new File(path);
                file.mkdirs();
            }
        } else {
            Log.e(TAG, "本地没有存在sdcard");
        }
    }

    /**
     * save bitmap to the sdcard
     * dir "/mnt/sdcard/temp/"
     * fileName "20111020163433.jpg"
     */
    public static File save(final Bitmap bitmap, String dir, String fileName) {
        if (bitmap == null)
            return null;

        String absolutePath = dir;

        File f = new File(absolutePath);
        if (!f.exists()) {
            if (!f.mkdirs()) {
            }
        }

        File mf = new File(absolutePath + "/" + fileName);
        OutputStream outputStream = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
        byte[] jpegData = out.toByteArray();

        try {
            outputStream = new FileOutputStream(mf);
            outputStream.write(jpegData);
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
        } finally {
            StreamUtil.closeSilently(outputStream);
        }
        return mf;
    }
}
