package com.ajuliaoo.ibtlibrary.models

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

inline fun <reified T : Enum<T>> Table.postgresEnumeration(
    columnName: String
) = customEnumeration(columnName, T::class.simpleName!!,
    { value -> enumValueOf<T>(value as String) }, { PGEnum(T::class.simpleName!!, it) })

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}