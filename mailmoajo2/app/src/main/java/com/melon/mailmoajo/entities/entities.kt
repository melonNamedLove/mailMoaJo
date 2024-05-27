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
    @ColumnInfo(name = "gmail") var gmail: String?,
    @ColumnInfo(name = "outlook") var outlook: String?,
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