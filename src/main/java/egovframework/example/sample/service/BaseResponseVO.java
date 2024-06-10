package egovframework.example.sample.service;


import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * @Package Name	: mdds.cmm.exception
 * @Class Name		: BaseResponseVO.java
 * @Description		: 기본 응답 VO
 * @Author			: LJY
 * @Since			: 2020. 10. 13. 오후 1:55:25
 */
@SuppressWarnings("serial")
public class BaseResponseVO implements Serializable {

    @Override
	public String toString() {
		return "BaseResponseVO [rspnsCode=" + rspnsCode + ", rspnsMssage=" + rspnsMssage + "]";
	}

	@NotNull
    private String rspnsCode = "";

    @NotNull
    private String rspnsMssage;

	public String getRspnsCode() {
		return rspnsCode;
	}

	public void setRspnsCode(String rspnsCode) {
		this.rspnsCode = rspnsCode;
	}

	public String getRspnsMssage() {
		return rspnsMssage;
	}

	public void setRspnsMssage(String rspnsMssage) {
		this.rspnsMssage = rspnsMssage;
	}
    
}
