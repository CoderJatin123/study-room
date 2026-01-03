package com.application.studyroom.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.studyroom.data.model.Room
import com.application.studyroom.data.model.RoomItem
import com.application.studyroom.data.model.toJson
import com.application.studyroom.data.model.toRoomItem
import com.application.studyroom.databinding.FragmentRoomsBinding
import com.application.studyroom.domain.repository.RoomRepository
import com.application.studyroom.ui.activity.AnnouncementsViewActivity
import com.application.studyroom.ui.adapter.RoomCardAdapter
import com.application.studyroom.ui.state.UiState
import com.application.studyroom.ui.viewmodel.HomeViewModel
import com.application.studyroom.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment : Fragment() {

    @Inject
    lateinit var roomRepository: RoomRepository

    lateinit var roomsAdapter: RoomCardAdapter

    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentRoomsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        roomsAdapter = RoomCardAdapter {
            requireActivity().startNewActivity<AnnouncementsViewActivity>{
                putExtra("room",it.toJson())
            }
        }
        binding = FragmentRoomsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeData()
    }

    private fun setUpRecyclerView() {
        binding.recRooms.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = roomsAdapter
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.roomsState.collectLatest {
                when (it) {
                    is UiState.Error -> {}
                    UiState.Initial -> {}

                    UiState.Loading -> {
                        roomsAdapter.update(List(2) { RoomItem(true, null) })
                    }

                    is UiState.Success<List<Room>> -> {
                        roomsAdapter.update(it.data.map { it.toRoomItem() })
                    }
                }
            }
        }
    }
}