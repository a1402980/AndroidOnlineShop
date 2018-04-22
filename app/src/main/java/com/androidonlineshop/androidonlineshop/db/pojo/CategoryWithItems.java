package com.androidonlineshop.androidonlineshop.db.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.util.List;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class CategoryWithItems {
    @Embedded
    public CategoryEntity category;

    @Relation(parentColumn = "id", entityColumn = "name", entity = ItemEntity.class)
    public List<ItemEntity> items;
}
