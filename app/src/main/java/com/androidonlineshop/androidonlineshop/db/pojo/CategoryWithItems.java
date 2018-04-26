package com.androidonlineshop.androidonlineshop.db.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

public class CategoryWithItems implements Serializable {
    @Embedded
    public CategoryEntity category;

    @Relation(parentColumn = "id", entityColumn = "categoryid", entity = ItemEntity.class)
    public List<ItemEntity> items;
}
