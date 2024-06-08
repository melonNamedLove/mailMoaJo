package entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.reflect.KClass


//@Parcelize
//@JsonClass(generateAdapter = true)

@Entity
data class contacts(

    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "mail_1") var mail_1: String?,
    @ColumnInfo(name = "mail_2") var mail_2: String?,
    @ColumnInfo(name = "mail_3") var mail_3: String?,
    ){@PrimaryKey(autoGenerate = true) var nId: Int = 0}
@Entity
data class orderedMailFolders(

    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "id") var folderid: String,
){@PrimaryKey(autoGenerate = true) var nId: Int = 0}

@Entity
data class mails(
    @ColumnInfo(name = "receivedTime") var receivedTime: String,
    @ColumnInfo(name = "sender") var sender: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "mailfolderid") var mailfolderid: Int
){@PrimaryKey(autoGenerate = true) var nId: Int = 0
}

@Entity
data class Gmails(
    @ColumnInfo(name = "receivedTime") var receivedTime: String,
    @ColumnInfo(name = "sender") var sender: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "mailfolderid") var mailfolderid: Int
){@PrimaryKey(autoGenerate = true) var nId: Int = 0
}

@Entity
data class OutlookMails(
    @ColumnInfo(name = "receivedTime") var receivedTime: String,
    @ColumnInfo(name = "sender") var sender: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "mailfolderid") var mailfolderid: Int
){@PrimaryKey(autoGenerate = true) var nId: Int = 0
}