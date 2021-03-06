package xinyi.com.architecture.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.squareup.leakcanary.LeakCanary;

import okhttp3.OkHttpClient;
import xinyi.com.architecture.di.component.AppComponent;
import xinyi.com.architecture.di.component.DaggerAppComponent;
import xinyi.com.architecture.di.module.AppModule;

/**
 * Created by wxy on 2017/8/8.
 */

public class XinYiApplication extends Application {

	public  static XinYiApplication xinYiApplication;
	public static AppComponent appComponent;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		xinYiApplication=this;
		initOkgo();

		LeakCanary.install(this);
		BlockCanary.install(xinYiApplication, new BlockCanaryContext()).start();
	}

	public AppComponent getAppComponent() {
		if (appComponent==null) {
			appComponent = DaggerAppComponent.builder()
					.appModule(new AppModule(this))
					.build();

		}
		return appComponent;
	}

	/**
	 * okgo 初始化配置
	 */
	private void initOkgo() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		OkGo.getInstance().init(this)                           //必须调用初始化
				.setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
				.setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
				.setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
				.setRetryCount(2);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
	}
}
