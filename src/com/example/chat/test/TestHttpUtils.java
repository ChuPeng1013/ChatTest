package com.example.chat.test;

import com.example.chat.utils.HttpUtils;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestHttpUtils extends AndroidTestCase 
{
	public void testSendInfo()
	{
		String res = HttpUtils.doGet("给我讲个笑话");
		Log.d("TestHttpUtils", res);
	}
}
