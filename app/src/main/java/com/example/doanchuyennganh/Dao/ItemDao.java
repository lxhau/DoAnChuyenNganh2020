package com.example.doanchuyennganh.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.doanchuyennganh.Models.Items;

import java.util.List;

@Dao
public interface ItemDao {
    String s="";

    @Query("SELECT * FROM items ORDER BY id DESC")
    List<Items> getAllItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItems(Items items);

    @Query("DELETE FROM items")
    void deleteAll();

    @Query("SELECT * FROM items WHERE Title LIKE '%'||:input ||'%' OR DateCreated LIKE '%'||:input ||'%' OR LinkURL LIKE '%'||:input ||'%'")
    List<Items> selectItems(String input);

}
