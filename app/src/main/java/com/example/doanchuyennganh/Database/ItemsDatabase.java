package com.example.doanchuyennganh.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.doanchuyennganh.Dao.ItemDao;
import com.example.doanchuyennganh.Models.Items;

@Database(entities= Items.class, version = 1, exportSchema = false)
public abstract class ItemsDatabase extends RoomDatabase {
    private static  ItemsDatabase ItemsDatabase;

    public static synchronized ItemsDatabase getDatabase(Context context){
        if(ItemsDatabase==null){
            ItemsDatabase= Room.databaseBuilder(
                    context,
                    ItemsDatabase.class,
                    "items_db"
            ).build();
        }

        return ItemsDatabase;
    }

    public abstract ItemDao noteDao();
}
