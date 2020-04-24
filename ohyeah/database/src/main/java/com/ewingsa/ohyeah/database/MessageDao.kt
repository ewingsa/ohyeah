package com.ewingsa.ohyeah.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface MessageDao {

    @Query("""
        SELECT *, SUM(CASE WHEN read = 0 THEN 1 ELSE 0 END) as unread
        FROM sender
        JOIN message ON senderId = message.sender_id
        WHERE timestamp <= :timestamp
        GROUP BY sender.senderId
        ORDER BY MAX(timestamp) DESC
        """)
    fun getPreviousConversations(timestamp: Long): Observable<List<PreviewSenderMessage>>

    @Query("""
        SELECT *, 0 as unread
        FROM sender
        JOIN message ON senderId = message.sender_id
        WHERE timestamp > :timestamp
        GROUP BY sender.senderId
        ORDER BY MIN(timestamp)
        """)
    fun getFutureConversations(timestamp: Long): Observable<List<PreviewSenderMessage>>

    @Query("SELECT * FROM sender WHERE senderId = :id")
    fun getExistingConversation(id: Long): Maybe<Sender>

    @Query("SELECT * FROM sender WHERE name = :name LIMIT 1")
    fun getExistingConversation(name: String): Maybe<Sender>

    @Query("SELECT * FROM message WHERE messageId = :id LIMIT 1")
    fun findMessage(id: Long): Maybe<Message>

    @Query("SELECT * FROM message JOIN sender on senderId = message.sender_id WHERE messageId = :id LIMIT 1")
    fun findMessageWithPicture(id: Long): Maybe<SenderMessage>

    @Query("SELECT * FROM message WHERE sender_id = :senderId ORDER BY timestamp DESC")
    fun findMessages(senderId: Long): Observable<List<Message>>

    @Query("SELECT COUNT(messageId) < 1 FROM message WHERE sender_id = :senderId")
    fun isConversationEmpty(senderId: Long): Maybe<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConversation(sender: Sender): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: Message): Long

    @Query("UPDATE sender SET name = :name WHERE senderId = :senderId")
    fun updateConversationName(senderId: Long, name: String): Completable

    @Query("UPDATE sender SET photo_uri = :photoUri WHERE senderId = :senderId")
    fun updateConversationPhoto(senderId: Long, photoUri: String): Completable

    @Query("UPDATE message SET read = 1 WHERE sender_id = :senderId AND timestamp <= :timestamp")
    fun markReadMessages(senderId: Long, timestamp: Long): Completable

    @Update
    fun updateMessage(message: Message): Completable

    @Query("DELETE FROM sender WHERE senderId = :senderId")
    fun deleteSender(senderId: Long): Completable

    @Query("DELETE FROM message WHERE messageId = :messageId")
    fun deleteMessage(messageId: Long): Completable

    @Query("DELETE FROM message WHERE sender_id = :senderId")
    fun deleteMessages(senderId: Long): Completable
}
