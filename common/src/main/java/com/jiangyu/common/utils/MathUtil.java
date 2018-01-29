package com.jiangyu.common.utils;

import android.graphics.Point;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * <p>Title: Math工具类 </p>
 * <p> Description:</p>
 * <p>1.byte转化成kb</p>
 * <p>2.byte转化成mb</p>
 * <p>3.求起始点坐标</p>
 * <p>4.获取不大于某个值的随机数</p>
 *
 */
public class MathUtil {
	public static DecimalFormat percentFormat = new DecimalFormat("##%");

	public static int byte2kb(int bytes) {
		if (bytes >= 1024) {
			return bytes / 1024;
		}
		return bytes;
	}

	public static int byte2mb(int bytes) {
		if (bytes >= 1024) {
			return bytes / 1024;
		}
		return bytes;
	}



	/**
	 * 计算圆上点旋转的真实坐标 已知圆心和圆上终点点,并知道经过的旋转度数,求起始点坐标
	 * 
	 * @param srcPoint
	 *            圆上已知点的坐标
	 * @param centerPoint
	 *            圆点
	 * @param degree
	 *            旋转的角度,以二维坐标系方式
	 * @return
	 */
	public static Point calcRealPoint(Point srcPoint, Point centerPoint, int degree, boolean isZeroPointAtTop) {
		Point result = new Point();
		double r = Math.hypot(srcPoint.x - centerPoint.x, srcPoint.y - centerPoint.y);
		double beta = calcAngle(srcPoint, centerPoint, isZeroPointAtTop);
		beta += degree;
		double desY = Math.abs(Math.sin(beta * Math.PI / 180) * r);
		double desX = Math.abs(Math.sqrt(r * r - desY * desY));
		beta = beta % 360;
		if (90 < beta && beta < 270) {
			// X为负
			desX = -1 * desX;
		} else {
			// X为正
		}
		if (0 <= beta && beta <= 180) {
			// 项点在上角时Y为负,在下角时Y为正
			desY = isZeroPointAtTop ? -desY : desY;
		} else {
			// 项点在上角时Y为正,在下角时Y为负
			desY = isZeroPointAtTop ? desY : -desY;
		}
		result.set(DataUtil.double2Int(desX + centerPoint.x), DataUtil.double2Int(desY + centerPoint.y));
		return result;
	}

	public static double calcAngle(Point srcPoint, Point centerPoint, boolean isZeroPointAtTop) {
		Point srcTempPoint = new Point(srcPoint.x - centerPoint.x, isZeroPointAtTop ? centerPoint.y - srcPoint.y : srcPoint.y - centerPoint.y);
		double r = Math.hypot(srcTempPoint.x, srcTempPoint.y);
		double beta = 0;
		if (srcTempPoint.x >= 0 && srcTempPoint.y >= 0) {
			// 第一象限
			beta = Math.round(Math.acos(srcTempPoint.x / r) * 180 / Math.PI);
		} else if (srcTempPoint.x < 0 && srcTempPoint.y >= 0) {
			// 第二象限
			beta = 180 - Math.round(Math.asin(srcTempPoint.y / r) * 180 / Math.PI);
		} else if (srcTempPoint.x < 0 && srcTempPoint.y < 0) {
			// 第三象限
			beta = 180 - Math.round(Math.asin(srcTempPoint.y / r) * 180 / Math.PI);
		} else if (srcTempPoint.x >= 0 && srcTempPoint.y < 0) {
			// 第四象限
			beta = Math.round(Math.asin(srcTempPoint.y / r) * 180 / Math.PI) + 360;
		}
		return beta;
	}
    /**
     * 获取小于最大随机数的任一随机数
     *
     * @param maxNum 最大随机数
     * @return 随机数
     */
    public static int getRandomNum(int maxNum) {
        if (maxNum < 0) {
            return 0;
        }
        return new Random().nextInt(maxNum);
    }
}
