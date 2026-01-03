package com.application.studyroom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.application.studyroom.data.model.Announcement
import com.application.studyroom.databinding.ItemAnnouncementBinding
import com.application.studyroom.ui.adapter.AnnouncementAdapter.AnnouncementViewHolder
import com.application.studyroom.utils.formatTimestamp

class AnnouncementAdapter : RecyclerView.Adapter<AnnouncementViewHolder>() {

    private val announcements = ArrayList<Announcement>()

    fun update(list: List<Announcement>) {
        announcements.clear()
        announcements.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnnouncementViewHolder {
        return AnnouncementViewHolder(
            ItemAnnouncementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: AnnouncementViewHolder,
        position: Int
    ) {
        holder.bind(announcements[position], position)
    }

    override fun getItemCount() = announcements.size

    class AnnouncementViewHolder(private val binding: ItemAnnouncementBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(announcement: Announcement, position: Int) {
            binding.apply {
                // Show header only for the first item
                tvHeader.isVisible = position == 0

                // Bind announcement data
                tvTitle.text = announcement.subject ?: "No Subject"
                tvContent.text = announcement.description ?: "No Description"

                // Format timestamp
                announcement.timeStamp?.let { timestamp ->
                    tvTimestamp.text = timestamp.formatTimestamp()
                } ?: run {
                    tvTimestamp.text = "Unknown Time"
                }

                // Set default author image (you can customize this based on your needs)
                // ivAuthorImage.setImageResource(R.drawable.ic_default_avatar)
            }
        }
    }
}
