package com.application.studyroom.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.application.studyroom.R
import com.application.studyroom.databinding.ItemAnnouncementBinding
import com.application.studyroom.domain.AnnouncementEntity
import com.application.studyroom.ui.adapter.AnnouncementAdapter.AnnouncementViewHolder
import com.application.studyroom.utils.formatTimestamp
import com.bumptech.glide.Glide

class AnnouncementAdapter : RecyclerView.Adapter<AnnouncementViewHolder>() {

    private val announcements = ArrayList<AnnouncementEntity>()

    fun update(list: List<AnnouncementEntity>) {
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

        fun bind(announcementEntity: AnnouncementEntity, position: Int) {
            binding.apply {
                // Show header only for the first item
                tvHeader.isVisible = position == 0

                announcementEntity.announcement.let {
                    // Bind announcement data
                    tvTitle.text = it.subject ?: "No Subject"
                    tvContent.text = it.description ?: "No Description"

                    // Format timestamp
                    it.timeStamp?.let { timestamp ->
                        tvTimestamp.text = timestamp.formatTimestamp()
                    } ?: run {
                        tvTimestamp.text = "Unknown Time"
                    }
                }

                announcementEntity.authorData.let {
                    it.profileUrl?.let {
                        Glide.with(ivAuthorImage).load(it.toUri()).placeholder(R.drawable.app_logo)
                            .into(ivAuthorImage)
                    }
                    tvAuthorName.text = it.name
                    tvAuthorPosition.text = it.email
                }


                // Set default author image (you can customize this based on your needs)
                // ivAuthorImage.setImageResource(R.drawable.ic_default_avatar)
            }
        }
    }
}
