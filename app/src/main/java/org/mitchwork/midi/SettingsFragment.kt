package org.mitchwork.midi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.mitchwork.midi.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var viewBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewBinding.setLifecycleOwner(this)
        return viewBinding.root
    }
}
