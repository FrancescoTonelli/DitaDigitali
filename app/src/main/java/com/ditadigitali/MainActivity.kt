package com.ditadigitali

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.ditadigitali.R

class MainActivity : AppCompatActivity() {
    private val url = "http://10.42.0.1:8080/index.html"
    private val retryDelayMs = 1_000L
    private val handler = Handler(Looper.getMainLooper())
    private var isRetryScheduled = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemUI()
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webview).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    handler.removeCallbacksAndMessages(null)
                    isRetryScheduled = false
                }

                override fun onReceivedError(
                    view: WebView,
                    request: WebResourceRequest,
                    error: WebResourceError
                ) {
                    if (request.isForMainFrame) {
                        showOfflineMessage(view)
                        isRetryScheduled = true
                        scheduleRetry(view)
                    }
                }

                override fun onReceivedHttpError(
                    view: WebView,
                    request: WebResourceRequest,
                    errorResponse: WebResourceResponse
                ) {
                    if (request.isForMainFrame) {
                        showOfflineMessage(view)
                        isRetryScheduled = true
                        scheduleRetry(view)
                    }
                }
            }
        }

        webView.loadUrl(url)
    }

    private fun scheduleRetry(view: WebView) {
        if (isRetryScheduled) {
            handler.postDelayed({
                view.loadUrl(url)
            }, retryDelayMs)
        }
    }

    private fun showOfflineMessage(view: WebView) {
        val html = """
            <html>
              <body style="display:flex;justify-content:center;align-items:center;height:100vh;
                           margin:0;font-family:sans-serif;background:#fafafa;">
                <div style="text-align:center;">
                  <h2>Connessione assente</h2>
                  <p>Riprovo tra pochi secondi…</p>
                </div>
              </body>
            </html>
        """.trimIndent()
        view.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}
