package org.synergy.phone.util;

/**
 * 음성인식 서비스의 에러관련 코드 관리
 * @author Leedh
 */
public class ECODE
{
	/**
	 * 네트워크 관련 오류
	 */
	public static final int RECOGNITION_NETWORK_ERROR  		= 1000;
	
	/**
	 * 마이크 관련 오류 (타 어플리케이션에서 마이크를 사용하고 있는 경우, 해당 서비스가 이미 실행되어 마이크가 동작되고 있는 경우)
	 */
	public static final int RECOGNITION_MIC_ERROR      		= 1001;
	
	/**
	 * 서비스의 예기치 못한 문제가 발생 하였거나 호출 어플리케이션과의 연경에 문제가 발생한 경우)
	 */
	public static final int RECOGNITION_SERVICE_ERROR  		= 1002;
	
	/**
	 * 유효한 음성정보 파일을 구글서버에 전송 하였으나 구글 서버로 부터 일치하는 정보를 찾지 못했을 경우 발생하는 오류
	 */
	public static final int RECOGNITION_NOSEARCH_ERROR 		= 1003;
	
	/**
	 * 구글서버에 음성인식 요청을 하였지만 구글서버의 오류로 인해 음성인식에 오류가 발생하는 경우
	 */
	public static final int RECOGNITION_GOOGLE_SERVER_ERROR = 1004;
}
