package org.synergy.mouse.parser;

import java.util.HashMap;

public class PaserManager 
{
    private boolean boolctrl = false;
    private HashMap<Integer, String> m_hmEnglishList = new HashMap<Integer, String>();
    private String m_strClipBoardMessage = "";
    
    public void setClipBoardMessage(String strClipBoardMessage)
    {
    	if(true == strClipBoardMessage.contains(" "))
    		strClipBoardMessage = strClipBoardMessage.replaceAll(" ", "%s");
    	
    	this.m_strClipBoardMessage = strClipBoardMessage;
    }

    /**
     * 기본 키보드 Event Number를 Text로 변환 하는 파서 함수
     */
    public void initPaserManager()
    {
    	m_hmEnglishList.put(2, "1");
    	m_hmEnglishList.put(3, "2");
    	m_hmEnglishList.put(4, "3");
    	m_hmEnglishList.put(5, "4");
    	m_hmEnglishList.put(6, "5");
    	m_hmEnglishList.put(7, "6");
    	m_hmEnglishList.put(8, "7");
    	m_hmEnglishList.put(9, "8");
    	m_hmEnglishList.put(10, "9");
    	m_hmEnglishList.put(11, "0");
    	m_hmEnglishList.put(12, "-");
    	m_hmEnglishList.put(13, "=");
    	m_hmEnglishList.put(43, "\\");
        m_hmEnglishList.put(16, "q");
        m_hmEnglishList.put(17, "w");
        m_hmEnglishList.put(18, "e");
        m_hmEnglishList.put(19, "r");
        m_hmEnglishList.put(20, "t");
        m_hmEnglishList.put(21, "y");
        m_hmEnglishList.put(22, "u");
        m_hmEnglishList.put(23, "i");
        m_hmEnglishList.put(24, "o");
        m_hmEnglishList.put(25, "p");
        m_hmEnglishList.put(26, "[");
        m_hmEnglishList.put(27, "]");
        m_hmEnglishList.put(30, "a");
        m_hmEnglishList.put(31, "s");
        m_hmEnglishList.put(32, "d");
        m_hmEnglishList.put(33, "f");
        m_hmEnglishList.put(34, "g");
        m_hmEnglishList.put(35, "h");
        m_hmEnglishList.put(36, "j");
        m_hmEnglishList.put(37, "k");
        m_hmEnglishList.put(38, "l");
        m_hmEnglishList.put(39, ");");
        m_hmEnglishList.put(40, "'");
        m_hmEnglishList.put(41, "`");
        m_hmEnglishList.put(44, "z");
        m_hmEnglishList.put(45, "x");
        m_hmEnglishList.put(46, "c");
        m_hmEnglishList.put(47, "v");
        m_hmEnglishList.put(48, "b");
        m_hmEnglishList.put(49, "n");
        m_hmEnglishList.put(50, "m");
        m_hmEnglishList.put(51, ",");
        m_hmEnglishList.put(52, ".");
        m_hmEnglishList.put(53, "/");
        m_hmEnglishList.put(55, "*");
        m_hmEnglishList.put(71, "7");
        m_hmEnglishList.put(72, "8");
        m_hmEnglishList.put(73, "9");
        m_hmEnglishList.put(74, "-");
        m_hmEnglishList.put(75, "4");
        m_hmEnglishList.put(76, "5");
        m_hmEnglishList.put(77, "6");
        m_hmEnglishList.put(78, "+");
        m_hmEnglishList.put(79, "1");
        m_hmEnglishList.put(80, "2");
        m_hmEnglishList.put(81, "3");
        m_hmEnglishList.put(82, "0");
        m_hmEnglishList.put(83, ".");
        m_hmEnglishList.put(309, "/");
    	m_hmEnglishList.put(14, "KEYCODE_BACK");
        m_hmEnglishList.put(15, "KEYCODE_TAB");
    	m_hmEnglishList.put(28, "KEYCODE_ENTER");
        m_hmEnglishList.put(57, "KEYCODE_SPACE");
        m_hmEnglishList.put(59, "KEYCODE_APP_SWITCH");
        m_hmEnglishList.put(327, "KEYCODE_HOME");
        m_hmEnglishList.put(339, "KEYCODE_DEL");
    }
    
    /**
     * Ctrl+V 또는 C와 같이 특수 기능을 사용할 수 있는 이벤트 파서를 별도로 관리
     * @param strMessage
     * @return 변환 키 타입
     */
    public String getPaserManager(String strMessage)
    {
    	String strKeyText = "";
    	int nIndex = Integer.parseInt(strMessage);
    	
        if(nIndex == 29)
        {
        	boolctrl = true;
        }
        else
        {
        	strKeyText = m_hmEnglishList.get(nIndex);
        	if(boolctrl == true) 
        		strKeyText = m_strClipBoardMessage;
        	boolctrl = false;
        }
        
    	return strKeyText;
    }
}
