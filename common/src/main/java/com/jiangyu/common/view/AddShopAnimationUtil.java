package com.jiangyu.common.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AddShopAnimationUtil {
	private Context mContext;

	// 购物车父布局
	private RelativeLayout mshoppingCartRelativeLayout;
	// 购物车图片显示
	private ImageView mShoppingCartIv;
	// 贝塞尔曲线中间过程点坐标
	private float[] mCurrentPosition = new float[2];
	// 路径测量
	private PathMeasure mPathMeasure;

	/**
	 * 
	 * @param shoppingCartRelativeLayout
	 *            当前页面所在的RelativeLaout,用于addView
	 * @param shoppingCartIv
	 *            购物车图标
	 */
	public AddShopAnimationUtil(Context context, RelativeLayout shoppingCartRelativeLayout, ImageView shoppingCartIv) {
		mContext = context;
		mshoppingCartRelativeLayout = shoppingCartRelativeLayout;
		mShoppingCartIv = shoppingCartIv;
	}

	/**
	 * 
	 * @param mGoodsImg
	 *            添加商品的ImageView
	 * @param isLeft
	 *            在左边是顺时针旋转，右边逆时针
	 */
	public void addGoodsToCart(ImageView mGoodsImg, boolean isLeft) {
		// 创造出执行动画的主题mGoodsImg（这个图片就是执行动画的图片,从开始位置出发,经过一个抛物线（贝塞尔曲线）,移动到购物车里）
		final ImageView goods = new ImageView(mContext);
		goods.setImageDrawable(mGoodsImg.getDrawable());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mGoodsImg.getWidth(), mGoodsImg.getHeight());
		mshoppingCartRelativeLayout.addView(goods, params);

		// 得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
		int[] parentLocation = new int[2];
		mshoppingCartRelativeLayout.getLocationInWindow(parentLocation);

		// 得到商品图片的坐标（用于计算动画开始的坐标）
		int startLoc[] = new int[2];
		mGoodsImg.getLocationInWindow(startLoc);

		// 得到购物车图片的坐标(用于计算动画结束后的坐标)
		int endLoc[] = new int[2];
		mShoppingCartIv.getLocationInWindow(endLoc);

		// 开始掉落的商品的起始点：商品起始点-父布局起始点
		float startX = startLoc[0] - parentLocation[0];
		float startY = startLoc[1] - parentLocation[1];

		// 商品掉落后的终点坐标：购物车起始点-父布局起始点-图片缩放后的大小
		float toX = endLoc[0] - parentLocation[0] - mGoodsImg.getWidth() * 0.4f;
		float toY = endLoc[1] - parentLocation[1] - mGoodsImg.getHeight() * 0.4f;

		// 开始绘制贝塞尔曲线
		Path path = new Path();
		// 移动到起始点（贝塞尔曲线的起点）
		path.moveTo(startX, startY);
		// 使用二阶贝塞尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
		path.quadTo((startX + toX) / 2, startY, toX, toY);
		// mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，如果是true，path会形成一个闭环
		mPathMeasure = new PathMeasure(path, false);

		// 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());

		// 匀速线性插值器
		valueAnimator.setInterpolator(new LinearInterpolator());
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// 当插值计算进行时，获取中间的每个值，
				// 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
				float value = (Float) animation.getAnimatedValue();
				// 获取当前点坐标封装到mCurrentPosition
				// 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
				// mCurrentPosition此时就是中间距离点的坐标值
				mPathMeasure.getPosTan(value, mCurrentPosition, null);
				// 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
				goods.setTranslationX(mCurrentPosition[0]);
				goods.setTranslationY(mCurrentPosition[1]);
			}
		});
		ValueAnimator valueAnimator2;
		if (isLeft) {
			valueAnimator2 = ObjectAnimator.ofFloat(goods, "rotation", 1f, 180f);
		} else {
			valueAnimator2 = ObjectAnimator.ofFloat(goods, "rotation", 1f, -180f);
		}
		ValueAnimator valueAnimator3 = ObjectAnimator.ofFloat(goods, "scaleX", 1f, 0.3f);
		ValueAnimator valueAnimator4 = ObjectAnimator.ofFloat(goods, "scaleY", 1f, 0.3f);

		AnimatorSet set = new AnimatorSet();
		set.setInterpolator(new LinearInterpolator());
		set.setDuration(500);
		set.play(valueAnimator).with(valueAnimator2).with(valueAnimator3).with(valueAnimator4);
		set.start();

		// 动画结束后的处理
		valueAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// 购物车商品数量加1
				// 把执行动画的商品图片从父布局中移除
				mshoppingCartRelativeLayout.removeView(goods);
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}
		});
	}
}
