package com.application.studyroom.domain

import com.application.studyroom.data.model.Announcement
import com.application.studyroom.data.model.UserData

data class AnnouncementEntity(val authorData: UserData, val announcement: Announcement)