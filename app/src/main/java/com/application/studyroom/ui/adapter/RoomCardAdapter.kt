package com.application.studyroom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.application.studyroom.data.model.Room
import com.application.studyroom.data.model.RoomItem
import com.application.studyroom.databinding.ItemRoomCardBinding
import com.application.studyroom.ui.adapter.RoomCardAdapter.CardViewHolder
import com.application.studyroom.utils.setData
import com.application.studyroom.utils.setLoading

class RoomCardAdapter(val onRoomClick: (Room) -> Unit) : RecyclerView.Adapter<CardViewHolder>() {
    private val rooms = ArrayList<RoomItem>()
    fun update(list: List<RoomItem>) {
        rooms.clear()
        rooms.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {
        return CardViewHolder(
            ItemRoomCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: CardViewHolder,
        position: Int
    ) {
        holder.bind(rooms[position], onRoomClick)
    }

    override fun getItemCount() = rooms.size

    class CardViewHolder(val binding: ItemRoomCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(roomItem: RoomItem, onRoomClick: (Room) -> Unit) {
            binding.apply {
                roomItem.room.also {
                    cardHeading.setData(it?.name.toString(), roomItem.isLoading)
                    cardContent.setData(it?.name.toString(), roomItem.isLoading)
                    ivRoom.setLoading(roomItem.isLoading)
                }
                ivMenu.isVisible = !roomItem.isLoading
                cvRoom.strokeWidth = if (roomItem.isLoading) 0 else 3
                roomItem.room?.let { room ->
                    cvRoom.setOnClickListener { onRoomClick.invoke(room) }
                }
            }
        }
    }
}