package com.androidonlineshop.androidonlineshop.db.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.util.List;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class CartWithItems {

    @Embedded
    public CartEntity cart;

    @Relation(parentColumn = "id", entityColumn = "cartid", entity = ItemEntity.class)
    public List<ItemEntity> items;
}
