package org.synergy.phone.util;


/**
 * ��Ʈ�� ���̺귯��
 */
public class StringLib 
{
	/**
	 * ���ڰ� null�̰ų� ���̰� 0�̸� 
	 * @param strValue
	 * @return
	 */
	public static boolean isEmpty (String strValue)
	{
		return null == strValue || 0 == strValue.length() ? true : false;
	}
	
	/**
	 * ȭ�鿡 ǥ�õǴ� �ִ� �ڸ����� üũ�ϰ� �ʰ��� ��� ".."���� ���ڸ� �����Ѵ�.
	 * @param ǥ�� ���ڿ�
	 * @return ���� ���ڿ�
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
