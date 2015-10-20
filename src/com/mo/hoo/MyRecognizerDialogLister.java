package com.mo.hoo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * 识别回调监听器
 */
public class MyRecognizerDialogLister implements RecognizerDialogListener{
	private Context context;
	public static  String text;
	public MyRecognizerDialogLister(Context context)
	{
		this.context = context;
		this.text="";
	}
	//自定义的结果回调函数，成功执行第一个方法，失败执行第二个方法
	@Override
	public void onResult(RecognizerResult results, boolean isLast) {
		text += JsonParser.parseIatResult(results.getResultString());
		
		//Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		MuTouActivity.mInputMsg.setText(text);
	}
	/**
	 * 识别回调错误.
	 */
	@Override
	public void onError(SpeechError error) {
		// TODO Auto-generated method stub
		int errorCoder = error.getErrorCode();
		switch (errorCoder) {
		case 10118:
			System.out.println("user don't speak anything");
			break;
		case 10204:
			System.out.println("can't connect to internet");
			break;
		default:
			break;
		}
	}
}
