package app.pay.pandapro.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import app.pay.pandapro.BaseActivity
import app.pay.pandapro.R
import app.pay.pandapro.databinding.ActivityAirtelCmsBinding
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.cmsurl.CmsGenerateUrlResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson

class AirtelCmsActivity : BaseActivity<ActivityAirtelCmsBinding>() {
    private lateinit var userSession: UserSession
    private var urlToLoad=""
    override fun getViewBinding(): ActivityAirtelCmsBinding = ActivityAirtelCmsBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession= UserSession(this@AirtelCmsActivity)
        generateUrl();

    }

    private fun generateUrl() {
        binding.llNoData.visibility=View.GONE
        binding.webView.visibility=View.GONE
        binding.imageView.visibility=View.VISIBLE
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        val requestData= hashMapOf<String,Any?>()
        requestData["long"]=userSession.getData(Constant.M_LONG).toString()
        requestData["lat"]=userSession.getData(Constant.M_LAT).toString()
        requestData["user_id"]=token
        UtilMethods.getCmsUrl(this@AirtelCmsActivity,requestData,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:CmsGenerateUrlResponse=Gson().fromJson(message,CmsGenerateUrlResponse::class.java)
                if (!response.error){
                    if (response.data.redirecturl.isNotEmpty()){
                        urlToLoad=response.data.redirecturl.toString()
                        setUpWebView()
                    }else{
                        binding.llNoData.visibility=View.VISIBLE
                        binding.webView.visibility=View.GONE
                        binding.imageView.visibility=View.GONE
                    }

                }else{
                    binding.llNoData.visibility=View.VISIBLE
                    binding.webView.visibility=View.GONE
                    binding.imageView.visibility=View.GONE
                }
            }

            override fun fail(from: String) {
                binding.llNoData.visibility=View.VISIBLE
                binding.webView.visibility=View.GONE
                binding.imageView.visibility=View.GONE
            }
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            loadsImagesAutomatically = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
        }

        binding.webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Show progress bar when page starts loading
                binding.llNoData.visibility=View.GONE
                binding.webView.visibility=View.GONE
                binding.imageView.visibility=View.VISIBLE
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Hide progress bar when page finishes loading
                binding.llNoData.visibility=View.GONE
                binding.webView.visibility=View.VISIBLE
                binding.imageView.visibility=View.GONE
            }
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                // Display a custom screen indicating that the page failed to load
                binding.llNoData.visibility=View.VISIBLE
                binding.webView.visibility=View.GONE
                binding.imageView.visibility=View.GONE
            }
        }
        binding.webView.loadUrl(urlToLoad)
//        if (UtilMethods.isNetworkAvailable(this@AirtelCmsActivity)){
//
//        }else{
//
//        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            startActivity(Intent(this@AirtelCmsActivity, DashBoardActivity::class.java))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }

    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        startActivity(Intent(this@AirtelCmsActivity, DashBoardActivity::class.java))
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        return true
    }

    override fun onClick(v: View?) {


    }
}