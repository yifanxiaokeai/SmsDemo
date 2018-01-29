package com.jiangyu.common.utils;

/**
 * <p>Title:   调用系统命令回调实体     </p>
 * <p>Description:                     </p>
 */
public class CommandResult {

	public int resultCode;

	public String successMsg;

	public String errorMsg;

	CommandResult(int resultCode, String successMsg, String errorMsg) {
		this.resultCode = resultCode;
		this.successMsg = successMsg;
		this.errorMsg = errorMsg;
	}
}
