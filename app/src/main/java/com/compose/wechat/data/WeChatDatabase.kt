package com.compose.wechat.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.compose.wechat.entity.Friend
import com.compose.wechat.entity.HomeMessage
import com.compose.wechat.main.friends.data.FriendsDao
import com.compose.wechat.main.home.data.HomeMessageDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [HomeMessage::class, Friend::class], version = 3, exportSchema = false)
abstract class WeChatDatabase : RoomDatabase() {

    companion object {

        private const val startIndex = 10000

        @Volatile
        private var mInstance: WeChatDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope): WeChatDatabase {
            return mInstance ?: synchronized(this) {
                if (null == mInstance) {
                    mInstance = Room.databaseBuilder(
                        context.applicationContext,
                        WeChatDatabase::class.java,
                        "wechat.db"
                    ).addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("WeChatDatabase", "first init: ${mInstance}")
                            mInstance?.let {
                                scope.launch {
                                    it.friendsDao().saveAll(initialFriendsList())
                                }
                            }
                        }
                    }).addMigrations(object : Migration(1, 2) {
                        override fun migrate(database: SupportSQLiteDatabase) {
                            database.execSQL("ALTER TABLE Friend ADD route TEXT DEFAULT NULL")
                        }
                    }).addMigrations(object : Migration(2, 3) {
                        override fun migrate(database: SupportSQLiteDatabase) {
                            database.execSQL("UPDATE Friend set route = \"newFriends\" where id = 10000")
                            database.execSQL("UPDATE Friend set route = \"limitedFriends\" where id = 10001")
                            database.execSQL("UPDATE Friend set route = \"groupChatList\" where id = 10002")
                            database.execSQL("UPDATE Friend set route = \"labelList\" where id = 10003")
                            database.execSQL("UPDATE Friend set route = \"officialAccount\" where id = 10004")
                            database.execSQL("UPDATE Friend set route = \"companyWechat\" where id = 10005")
                        }
                    }).build()
                }
                mInstance!!
            }
        }

        private fun initialFriendsList() = mutableListOf(
            Friend(startIndex + 0, "", "新的朋友", '0'),
            Friend(startIndex + 1, "", "仅聊天的朋友", '0'),
            Friend(startIndex + 2, "", "群聊", '0'),
            Friend(startIndex + 3, "", "标签", '0'),
            Friend(startIndex + 4, "", "公众号", '0'),
            Friend(startIndex + 5, "", "企业微信联系人", '1'),
            Friend(startIndex + 6, "", "Android", '2'),
            Friend(startIndex + 7, "", "iOS", '2'),
            Friend(startIndex + 8, "", "Android", 'A'),
            Friend(startIndex + 9, "", "Flutter", 'F'),
            Friend(startIndex + 10, "", "Java", 'J'),
            Friend(startIndex + 11, "", "Kotlin", 'K'),
            Friend(startIndex + 12, "", "React", 'R'),
            Friend(startIndex + 13, "", "ReactNative", 'R'),
            Friend(startIndex + 14, "", "Vue", 'V')
        )
    }

    abstract fun homeMessageDao(): HomeMessageDao

    abstract fun friendsDao(): FriendsDao

}