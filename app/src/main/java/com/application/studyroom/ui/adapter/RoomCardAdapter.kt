package com.application.studyroom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.studyroom.data.model.Room
import com.application.studyroom.databinding.ItemRoomCardBinding
import com.application.studyroom.ui.adapter.RoomCardAdapter.CardViewHolder

class RoomCardAdapter() : RecyclerView.Adapter<CardViewHolder>() {
    private val rooms = ArrayList<Room>()
    fun update(list: List<Room>) {
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
        holder.bind(rooms[position])
    }

    override fun getItemCount() = rooms.size

    class CardViewHolder(val binding: ItemRoomCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Room) {
            binding.apply {
                cardHeading.text = room.name
                cardContent.text = room.createdByName
            }
        }
    }
}