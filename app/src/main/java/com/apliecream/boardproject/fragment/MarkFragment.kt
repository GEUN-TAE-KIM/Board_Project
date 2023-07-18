package com.apliecream.boardproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.apliecream.boardproject.R
import com.apliecream.boardproject.databinding.FragmentMarkBinding

class MarkFragment : Fragment() {

    private lateinit var binding: FragmentMarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mark, container, false)

        binding.homeTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_markFragment_to_homeFragment)

        }

        binding.boardTap.setOnClickListener {

            it.findNavController().navigate(R.id.action_markFragment_to_boardFragment)

        }

        val webView : WebView = binding.root.findViewById(R.id.storeWebView)
        webView.loadUrl("https://www.naver.com")

        return binding.root
    }
}