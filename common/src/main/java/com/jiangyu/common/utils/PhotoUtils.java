package com.jiangyu.common.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;


import com.jiangyu.common.base.BaseApplication;

import java.io.File;
import java.io.IOException;

public class PhotoUtils{
	private static final int THUMB_WIDTH = 500;
	
	public static final int TO_TAKE_PHOTO_CODE = 1000;
	public static final int TO_LOCAL_PHOTO_CODE = 1001;
	/**
	 * 头像最大分辨率
	 */
	public final static int AVATAR_MAX_RESOLUTION = 128;
	
	public final static int TO_CUT_AVATAR = 1002;
	
	
	private static final String photoSp = "Funtalkcamera";
	/**
	 * 拍照并获得保存照片路径
	 */
	public static void takePhoto(Activity mActivity , String photoPath , String bucketName) {
		if (FileUtils.existSDCard()) {
			File tempFile = new File(photoPath);
			Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
			
			long curTime = System.currentTimeMillis();
			String strCurTime = String.valueOf(curTime);
			
			SharedPreferencesUtil.setValue(mActivity, Media.DISPLAY_NAME, FileUtils.getFileName(photoPath, false) , photoSp);
			SharedPreferencesUtil.setValue(mActivity, Media.DATA, photoPath , photoSp);
			SharedPreferencesUtil.setValue(mActivity, Media.TITLE, FileUtils.getFileName(photoPath) , photoSp);
			SharedPreferencesUtil.setValue(mActivity, Media.DATE_TAKEN, strCurTime , photoSp);
			SharedPreferencesUtil.setValue(mActivity, Media.DATE_MODIFIED, strCurTime , photoSp);
			SharedPreferencesUtil.setValue(mActivity, Media.DATE_ADDED, strCurTime , photoSp);
			SharedPreferencesUtil.setValue(mActivity, Media.BUCKET_DISPLAY_NAME, bucketName , photoSp);
			
			mActivity.startActivityForResult(takePhotoIntent,
					TO_TAKE_PHOTO_CODE);
		} else {
			ToastUtil.showShort("找不到sd卡");;
		}
	}
	
	/**
	 * 获取本地图片 <br>
	 */
	public static void getLocalPhoto(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK,
				Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");
		activity.startActivityForResult(
				Intent.createChooser(intent, "Pick any photo"),
				TO_LOCAL_PHOTO_CODE);
	}

	/**
	 * 图片裁剪
	 * @param context
	 * @param uri 图片的Url
	 */
	public static void startPhotoZoom(Activity activity , Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", AVATAR_MAX_RESOLUTION);
		intent.putExtra("outputY", AVATAR_MAX_RESOLUTION);
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		activity.startActivityForResult(intent, TO_CUT_AVATAR);
	}

	/**
	 * 单照片拍摄回来后，返回路径
	 * @param context
	 * @param saveToBucket  是否存入相册，true为是
	 * @return  拍照后存储的路径
	 */
	public static MImageItem whenPictureTaked (Context context , boolean saveToBucket)
	{

		MImageItem item = saveToBucket(context);

		int imgId = 0;
		Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null, "_ID desc", null);
		if(cursor != null && cursor.moveToFirst())
		{
			imgId = cursor.getInt(cursor.getColumnIndex(Media._ID));
			item.imgId = imgId+"";
			cursor.close();
		}

		if(!saveToBucket && item != null) //如果不存入相册的话，就删除
		{
			context.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, Media._ID + " = " + item.imgId, null);
		}else
		{
			File file = new File(item.sourcePath);
			if(file.exists())
			{
				Bitmap thumbBit = null;
				try {
					thumbBit = BitmapUtil.revitionImageSize(item.sourcePath, THUMB_WIDTH);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String thumbPath = null;
				if(thumbBit != null)
				{
					String rootP = FileUtils.getSdcardDir()+"/DCIM/.thumbnails";
					FileUtils.checkOrMakeDirs(rootP);
					thumbPath = rootP+"/"+item.title+"thumb.jpg";
					BitmapUtil.saveBitmap(thumbBit, thumbPath);
				}

				if(thumbPath != null)
				{
					item.thumbPath = thumbPath;
					ContentValues cv = new ContentValues();
					cv.put(Thumbnails.IMAGE_ID, imgId+"");
					cv.put(Thumbnails.DATA, thumbPath);
					context.getContentResolver().insert(Thumbnails.EXTERNAL_CONTENT_URI, cv);
				}
			}
		}
		return item;
	}

	/**
	 * 将图片存入相册
	 * @param context
	 */
	public static MImageItem saveToBucket (Context context)
	{
		synchronized (BaseApplication.getInstance()) {
			ContentValues takePhotoValues = new ContentValues();

			String addDate = SharedPreferencesUtil.getValue(context, Media.DATE_ADDED, photoSp);
			String title = SharedPreferencesUtil.getValue(context, Media.TITLE, photoSp);

			if(StringUtil.isEmpty(addDate) || StringUtil.isEmpty(title))
			{
				return null;
			}

			Cursor cursor = context.getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null , Media.DATE_ADDED + " = " + addDate + " and " + Media.TITLE + " = " + title, null, null);
			if(cursor != null)
			{
				if(cursor.getCount() > 0)
				{
					cursor.close();
					return null;
				}else
				{
					cursor.close();
				}
			}

			String imgPath = SharedPreferencesUtil.getValue(context, Media.DATA, photoSp);
			String imgTitle = SharedPreferencesUtil.getValue(context, Media.TITLE, photoSp);
			int _ID = 0;

			takePhotoValues.put(Media.DISPLAY_NAME, SharedPreferencesUtil.getValue(context, Media.DISPLAY_NAME, photoSp));
			takePhotoValues.put(Media.DATA, imgPath);
			takePhotoValues.put(Media.TITLE, imgTitle);
			takePhotoValues.put(Media.DATE_TAKEN, SharedPreferencesUtil.getValue(context, Media.DATE_TAKEN, photoSp));
			takePhotoValues.put(Media.DATE_MODIFIED, SharedPreferencesUtil.getValue(context, Media.DATE_MODIFIED, photoSp));
			takePhotoValues.put(Media.DATE_ADDED, SharedPreferencesUtil.getValue(context, Media.DATE_ADDED, photoSp));
			takePhotoValues.put(Media.BUCKET_DISPLAY_NAME, SharedPreferencesUtil.getValue(context, Media.BUCKET_DISPLAY_NAME, photoSp));

			context.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, takePhotoValues);
			
			SharedPreferences sp = context.getSharedPreferences(photoSp, Context.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.clear();
			edit.commit();
			
			MImageItem item = new MImageItem();
			item.sourcePath = imgPath;
			item.thumbPath = "";
			item.title = title;
			item.imgId = _ID+"";
			
			return item;
		}
	}
	
	public static class MImageItem
	{
		public String imgId;
		public String sourcePath;
		public String thumbPath;
		public String title;
		public long modifyTime;
	}
}
