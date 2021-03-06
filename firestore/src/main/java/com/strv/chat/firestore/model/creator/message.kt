package com.strv.chat.firestore.model.creator

import com.strv.chat.core.data.entity.ID
import com.strv.chat.core.domain.model.IMessageModel
import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreImageDataEntity
import com.strv.chat.firestore.entity.FirestoreMessageEntity
import com.strv.chat.firestore.entity.IMAGE
import com.strv.chat.firestore.entity.IMAGE_URL
import com.strv.chat.firestore.entity.MESSAGE_TYPE
import com.strv.chat.firestore.entity.MeesageTypeEnum
import com.strv.chat.firestore.entity.SENDER_ID
import com.strv.chat.firestore.entity.messageType
import com.strv.chat.firestore.model.FirestoreImageMessageModel
import com.strv.chat.firestore.model.FirestoreImageModel
import com.strv.chat.firestore.model.FirestoreTextMessageModel
import strv.ktools.logE
import java.util.Date

internal object MessageModelCreator : Creator<IMessageModel, MessageModelConfiguration> {

    override val create: MessageModelConfiguration.() -> IMessageModel = {
        when (messageType(requireNotNull(message.messageType) { "$MESSAGE_TYPE must be specified" })) {
            MeesageTypeEnum.TEXT_TYPE -> FirestoreTextMessageModel(
                requireNotNull(message.id) { "$ID must me specified" },
                message.timestamp?.toDate() ?: Date(),
                requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
                message.data?.message ?: ""
            )
            MeesageTypeEnum.IMAGE_TYPE -> FirestoreImageMessageModel(
                requireNotNull(message.id) { "$ID must me specified" },
                message.timestamp?.toDate() ?: Date(),
                requireNotNull(message.senderId) { logE("$SENDER_ID must be specified") },
                imageModel(requireNotNull(message.data?.image) { logE("$IMAGE must be specified") })
            )
        }
    }

    private fun imageModel(data: FirestoreImageDataEntity) =
        FirestoreImageModel(
            requireNotNull(data.url) { logE("$IMAGE_URL must be specified") }
        )
}

internal class MessageModelConfiguration(
    val message: FirestoreMessageEntity
) : CreatorConfiguration