package com.ditadigitali.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ditadigitali.ui.theme.Primary
import com.ditadigitali.utils.NetworkUtil
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.unit.dp
import com.ditadigitali.ui.components.FloatingButton

class WebViewViewModel(application: Application) : AndroidViewModel(application) {

    var state by mutableStateOf<LoadState>(LoadState.Loading)
        private set

    fun load() {
        viewModelScope.launch {
            val ctx = getApplication<Application>().applicationContext
            state = LoadState.Loading
            if (NetworkUtil.isConnected(ctx)) {
                val url = NetworkUtil.getRequestUrl(ctx)
                state = LoadState.Success(url)
            } else {
                state = LoadState.Error("Nessuna connessione di rete")
            }
        }
    }
}

sealed class LoadState {
    data object Loading : LoadState()
    data class Success(val url: String) : LoadState()
    data class Error(val message: String) : LoadState()
}

@SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
@Composable
fun WebViewScreen(
    onError: (String) -> Unit,
    viewModel: WebViewViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state = viewModel.state

    LaunchedEffect(Unit) { viewModel.load() }

    when (state) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }
        }
        is LoadState.Success -> {
            val context = LocalContext.current
            val url = state.url

            val webView = remember { WebView(context) }
            var isLoading by remember { mutableStateOf(true) }

            DisposableEffect(Unit) {
                onDispose { webView.destroy() }
            }

            LaunchedEffect(url) {
                webView.settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    cacheMode = WebSettings.LOAD_NO_CACHE

                }
                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        isLoading = false
                    }
                    override fun onReceivedError(
                        view: WebView, request: WebResourceRequest, error: WebResourceError
                    ) {
                        onError(error.description?.toString() ?: "Errore sconosciuto")
                    }
                }
                webView.apply {
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
                webView.setOnTouchListener { _, event ->
                    event.pointerCount > 1
                }
                isLoading = true
                webView.loadUrl(url)
            }

            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(factory = { webView }, modifier = Modifier.fillMaxSize())

                if (!isLoading) {
                    FloatingButton(
                        onClick = {
                            isLoading = true
                            webView.reload()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        icon = Icons.Default.Refresh,
                        contentDescription = "Ricarica"
                    )
                }

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = Primary)
                    }
                }
            }
        }
        is LoadState.Error -> {
            LaunchedEffect(state.message) {
                onError(state.message)
            }
        }
    }
}
