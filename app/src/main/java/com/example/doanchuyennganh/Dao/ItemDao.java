package com.example.doanchuyennganh.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.doanchuyennganh.Models.Items;

import java.util.List;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM items ORDER BY id DESC")
    List<Items> getAllItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(Items items);

    @Delete
    void deleteItems(Items items);


}
