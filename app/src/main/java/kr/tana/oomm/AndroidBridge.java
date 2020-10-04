package kr.tana.oomm;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017-08-03.
 */

public class AndroidBridge {
    private final Handler handler = new Handler();
    private WebView mWebView;
    private DBHelper dbHelper;
    private boolean newtwork;
    private Context context;

    // 생성자
    // 따로 사용할일 없으면 이거 안만들고 위의 변수도 안만들어도 됨.
    public AndroidBridge(WebView mWebView, DBHelper dbHelper, boolean newtwork,Context context) {
        this.mWebView = mWebView;
        this.dbHelper = dbHelper;
        this.newtwork = newtwork;
        this.context = context;
    }

    // DB데이터 가져오기
    @JavascriptInterface
    public void requestData() { // must be final
        handler.post(new Runnable() {
            public void run() {
                Log.d("HybridApp", "데이터 요청");
                String test = dbHelper.getResult();
                Log.d("HybridApp", test);
                mWebView.loadUrl("javascript:getAndroidData('" + test + "')");
            }
        });
    }



    //페이지 새로고침
    ////webView.reload();//웹뷰 새로고침

    //ID가져오기
    @JavascriptInterface
    public void refresh() { // must be final
        handler.post(new Runnable() {
            public void run() {
                mWebView.reload();
            }
        });
    }

    @JavascriptInterface
    public void addClipboard(final String url){
        handler.post(new Runnable() {
            public void run() {
                String gotourl = "http://oomm.kr/?idx="+url;

                Log.d("oomm",gotourl);
                ClipboardManager  clipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("imgsrc",gotourl);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context,"클립보드에 복사되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 기기의 공유 기능 작동함
    @JavascriptInterface
    public void doShare(final String arg1, final String arg2, final String arg3,final String arg4) {
        Log.d("oomm",arg1+arg2+arg3);

        new Handler().post(new Runnable() {
            public void run() {
                String url = arg4+arg2;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, arg1); // 제목
                shareIntent.putExtra(Intent.EXTRA_TEXT, url); // 내용
                context.startActivity(Intent.createChooser(shareIntent, arg3)); // 공유창 제목
            }
        });
    }



    //ID가져오기
    @JavascriptInterface
    public void getID() { // must be final
        handler.post(new Runnable() {
            public void run() {
                Log.d("HybridApp", "getid 데이터 요청");
                String userid = dbHelper.getID();
                Log.d("HybridApp", userid);
                if(userid != null && !"".equals(userid)){
                    mWebView.loadUrl("javascript:getAndroidData('" + userid + "',false)");
                }else{
                    mWebView.loadUrl("javascript:getAndroidData('" + userid + "',true)");
                }
            }
        });
    }



    @JavascriptInterface
    public void getLang() {
        handler.post(new Runnable() {
            public void run() {
                Log.d("HybridApp", "getLang 데이터 요청");
                String lang = dbHelper.getLang();
                Log.d("HybridApp", lang);
                mWebView.loadUrl("javascript:getAndroidLang('" + lang + "')");
            }
        });
    }

    @JavascriptInterface
    public void setLang(final String lang)
    {
        handler.post(new Runnable() {
            public void run() {
                Date d = new Date();

                String s = d.toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                dbHelper.setLang(sdf.format(d), lang);
                Log.d("HybridApp", "Lang 저장");
            }
        });
    }

    @JavascriptInterface
    public void setLangIfFirst(final String lang)
    {
        handler.post(new Runnable() {
            public void run() {
                Date d = new Date();

                String s = d.toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                dbHelper.setLangIfFirst(sdf.format(d), "en");
                Log.d("HybridApp", "Lang 저장");
            }
        });
    }


    //main화면 진입시
    @JavascriptInterface
    public void setMain(final boolean ismain)
    {
        handler.post(new Runnable() {
            public void run() {
                Log.d("HybridApp", "메인화면 진입");
                MainActivity.ismain = ismain;
            }
        });
    }

    @JavascriptInterface
    public void deleteID() { // must be final
        handler.post(new Runnable() {
            public void run() {
                Log.d("HybridApp", "delete id ");
                dbHelper.deleteID();
            }
        });
    }


    @JavascriptInterface
    public void saveID(final String id)
    {
        handler.post(new Runnable() {
            public void run() {
                Date d = new Date();

                String s = d.toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                dbHelper.insertID(sdf.format(d), id);
                Log.d("HybridApp", "ID 저장");
            }
        });
    }



    // DB에 데이터 저장하기
    @JavascriptInterface
    public void saveData(final String item, final int num) { // must be final
        handler.post(new Runnable() {
            public void run() {
                Date d = new Date();

                String s = d.toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                dbHelper.insert(sdf.format(d), item, num);
                Log.d("HybridApp", "데이터 저장");
                String test  =  dbHelper.getResult();
                mWebView.loadUrl("javascript:getAndroidData('"+test+"')");
            }
        });
    }
}

