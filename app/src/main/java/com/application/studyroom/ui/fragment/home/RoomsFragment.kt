package com.application.studyroom.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.application.studyroom.data.model.Room
import com.application.studyroom.databinding.FragmentRoomsBinding
import com.application.studyroom.domain.repository.RoomRepository
import com.application.studyroom.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment : Fragment() {

    @Inject
    lateinit var roomRepository: RoomRepository

    private var _binding: FragmentRoomsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        _binding?.btnAdd?.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                roomRepository.createRoom(Room("Jatin-123"))
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}