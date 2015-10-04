package org.synergy.phone.util;

/**
 * 음성통화 서비스 관련 코드 관리
 * @author Leedh
 */
public class PCODE
{
	/**
	 * 전화 통화 요청
	 */
	public static final int RECOGNITION_REQUEST_CALL  = 6000;
	public static final String RECOGNITION_REQUEST_CALL_S  = "RECOGNITION_REQUEST_CALL";
	
	/**
	 * 전화 통화 요청 종료
	 */
	public static final int RECOGNITION_REQUEST_END   = 6001;
	public static final String RECOGNITION_REQUEST_END_S  = "RECOGNITION_REQUEST_END";
	
	/**
	 * 전화 통화 시작
	 */
	public static final int RECOGNITION_CALL_START	  = 6002;
	public static final String RECOGNITION_CALL_START_S  = "RECOGNITION_CALL_START";
	
	/**
	 * 전화 통화 종료
	 */
	public static final int RECOGNITION_CALL_END	  = 6003;
	public static final String RECOGNITION_CALL_END_S  = "RECOGNITION_CALL_END";
	
	/**
	 * 전화 통화 기능 ON
	 */
	public static final int RECOGNITION_VOICE_CALL_ON = 6004;
	
	/**
	 * 전화 통화 기능 OFF
	 */
	public static final int RECOGNITION_VOICE_CALL_OFF = 6005;
	
	/**
	 * 전화 통화 요청 수락
	 */
	public static final int RECOGNITION_RECEIVE_CALL   = 6006;
	public static final String RECOGNITION_RECEIVE_CALL_S  = "RECOGNITION_RECEIVE_CALL";
	
	/**
	 * 전화 통화 요청 거절
	 */
	public static final int RECOGNITION_REJECT_CALL    = 6007;
	public static final String RECOGNITION_REJECT_CALL_S  = "RECOGNITION_REJECT_CALL";
	
	/**
	 * 전화 알람
	 */
	public static final int RECOGNITION_REQUEST_ALARM  = 6008;

	/**
	 * 서버로부터 전화 수신
	 */
	public static final int RECOGNITION_CALL_START_FROM_SERVER = 6009;
	public static final String RECOGNITION_CALL_START_FROM_SERVER_S  = "RECOGNITION_CALL_START_FROM_SERVER";
	

	/**
	 * 서버로부터 전화 수신
	 */
	public static final int RECOGNITION_CALL_REJECT_FROM_SERVER = 6010;
	public static final String RECOGNITION_CALL_REJECT_FROM_SERVER_S  = "RECOGNITION_CALL_REJECT_FROM_SERVER";
	
}
