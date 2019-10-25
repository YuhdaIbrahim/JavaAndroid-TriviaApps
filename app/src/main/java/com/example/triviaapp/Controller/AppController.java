package com.example.triviaapp.Controller;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
  public static final String TAG  = AppController.class.getSimpleName();
  private static AppController instance;
  private RequestQueue requestQueue;

  public static synchronized AppController getInstance() {
//    if (instance == null) {
//      instance = new AppController();
//    }
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
  }

  public RequestQueue getRequestQueue() {
    if (requestQueue == null) {
      // getApplicationContext() is key, it keeps you from leaking the
      // Activity or BroadcastReceiver if someone passes one in.
      requestQueue = Volley.newRequestQueue(getApplicationContext());
    }
    return requestQueue;
  }

  public <T> void addToRequestQueue(Request<T> req,String tag) {
    req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
    getRequestQueue().add(req);
  }

  public <T> void addToRequestQueue(Request<T> req) {
    req.setTag(TAG);
    getRequestQueue().add(req);
  }

  public void cancelPendingRequest(Object tag){
    if (requestQueue != null){
      requestQueue.cancelAll(tag);
    }
  }
}
//  public ImageLoader getImageLoader() {
//    return imageLoader;
//  }
//  private ImageLoader imageLoader;
//  private static Context ctx;

//  private MySingleton(Context context) {
//    ctx = context;
//    requestQueue = getRequestQueue();
//
//    imageLoader = new ImageLoader(requestQueue,
//        new ImageLoader.ImageCache() {
//          private final LruCache<String, Bitmap>
//              cache = new LruCache<String, Bitmap>(20);
//
//          @Override
//          public Bitmap getBitmap(String url) {
//            return cache.get(url);
//          }
//
//          @Override
//          public void putBitmap(String url, Bitmap bitmap) {
//            cache.put(url, bitmap);
//          }
//        });
//  }
