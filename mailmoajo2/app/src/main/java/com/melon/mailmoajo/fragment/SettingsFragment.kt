package com.melon.mailmoajo.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.melon.mailmoajo.GmailLoadActivity
import com.melon.mailmoajo.HomeActivity
import com.melon.mailmoajo.R
import com.melon.mailmoajo.databinding.FragmentSettingsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSettingsBinding.inflate(layoutInflater)

        binding.gmailLoadBtn.setOnClickListener{
            var i: Intent = Intent(context, GmailLoadActivity::class.java)
            startActivity(i)
        }

        return binding.root
    }

}