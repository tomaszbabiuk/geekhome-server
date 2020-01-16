package com.geekhome.common.configurable;

import com.geekhome.common.localization.Resource;

import java.util.List;

public interface Configurable {
    List<Field<?>> getFields();
    List<Class<Configurable>> attachableTo();
    Long getPersistenceId();

    Resource getNameRes();
    String getIconName();
}

/*
interface Configurable {

    fun getFields() : List<Field<*>>
    val attachableTo : List<KClass<out Configurable>>
    var persistenceId: Long

    @get:StringRes
    val addNewItemResId: Int

    @get:DrawableRes
    val iconResId: Int
}
*/
