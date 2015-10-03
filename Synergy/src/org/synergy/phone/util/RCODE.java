package org.synergy.phone.util;

/**
 * 음성인식 관련 동작 코드 관리
 * @author Leedh
 */
public class RCODE 
{
	/**
	 * 음성인식 동작으로 부터 발생된 문자열 값
	 */
	public static final int RECOGNITION_SERVICE_WORD   = 0;
	
	/**
	 * TTS를 발생시켜 외부로 음성을 표시 하기 위한 값
	 */
	public static final int RECOGNITION_SERVICE_SPEECH = 1;
	
	/**
	 * 음성인식 서비스 시작동작을 요청하기 위한 값
	 */
	public static final int RECOGNITION_SERVICE_START  = 2;
	
	/**
	 * 음성인식 서비스 중지동작을 요청하기 위한 값
	 */
	public static final int RECOGNITION_SERVICE_STOP   = 3;

	/**
	 * 음성인식 서비스 거절동작을 요청하기 위한 값
	 */
	public static final int RECOGNITION_SERVICE_REJECT = 4;
}