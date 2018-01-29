package com.jiangyu.common.utils;

import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

public class ClickSpanUtils {
	
	public static void setSpan (TextView widget , SpannableString sps ,  String keyStr , MClickSpan mClickSpan)
	{
		if(TextUtils.isEmpty(keyStr) || TextUtils.isEmpty(sps))
		{
			return;
		}
		String text = sps.toString();
		if(!text.contains(keyStr))
		{
			return;
		}
		int start = text.indexOf(keyStr);
		int end = start + keyStr.length();
		
		sps.setSpan(mClickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		widget.setText(sps);
		widget.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	
	public static class MClickSpan extends ClickableSpan{

		private OnTextViewClickListener onClick;
		
		public MClickSpan(OnTextViewClickListener onClick) {
			super();
			this.onClick = onClick;
		}
	
		@Override
		public void onClick(View widget) {
			SpannableString sps = (SpannableString) ((TextView)widget).getText();
			if(sps == null)
			{
				return;
			}
			Selection.removeSelection(sps);
			if(onClick != null)
			{
				onClick.clickTextView(widget);
			}
		}
	
		@Override
		public void updateDrawState(TextPaint ds) {
			if(onClick != null)
			{
				onClick.setStyle(ds);
			}
		}
	}
	
	public static interface OnTextViewClickListener
	{
		public void clickTextView(View view);
		public void setStyle(TextPaint tp);
	}

}
