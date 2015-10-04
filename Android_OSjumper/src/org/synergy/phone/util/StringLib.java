package org.synergy.phone.util;


/**
 * 스트링 라이브러리
 */
public class StringLib 
{
	/**
	 * 인자가 null이거나 길이가 0이면 
	 * @param strValue
	 * @return
	 */
	public static boolean isEmpty (String strValue)
	{
		return null == strValue || 0 == strValue.length() ? true : false;
	}
	
	/**
	 * 화면에 표시되는 최대 자릿수를 체크하고 초과할 경우 ".."으로 문자를 변경한다.
	 * @param 표시 문자열
	 * @return 변경 문자열
	 */
	public static String setMaxCharLength(String sCategory, int nMaxSize)
    {
    	if(sCategory.length()>nMaxSize)
    	{
    		sCategory = sCategory.substring(0, nMaxSize) +"..";
    	}
    	return sCategory;
    }
	
	public static String toString (boolean bValue)
	{
		return (true == bValue) ? "true" : "false";
	}
	
	public static boolean toString (String strValue)
	{
		return strValue.equals("true");
	}
}
