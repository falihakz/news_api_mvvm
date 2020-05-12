package com.example.falihmandiritestapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.falihmandiritestapp.database.entity.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    fun getAll(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg article: Article)

    @Query("DELETE FROM articles")
    fun deleteAll()

    @Query("SELECT * FROM articles WHERE title LIKE '%' || :keyword || '%' OR description LIKE '%' || :keyword || '%' ")
    fun findArticleByKeyword(keyword: String): List<Article>
}