package com.bignerdranch.android.eventfinder.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.eventfinder.databinding.FragmentEventPageBinding

class EventPageFragment : Fragment() {
    private val args: EventPageFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventPageBinding.inflate(
            inflater,
            container,
            false
        )

        binding.apply {
            progressBar.max = 100

            webView.apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(args.eventPageUri.toString())

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(
                        webView: WebView,
                        newProgress: Int
                    ) {
                        if (newProgress == 100) {
                            progressBar.visibility = View.GONE
                        } else {
                            progressBar.visibility = View.VISIBLE
                            progressBar.progress = newProgress
                        }
                    }
                }
            }
        }

        return binding.root
    }
}