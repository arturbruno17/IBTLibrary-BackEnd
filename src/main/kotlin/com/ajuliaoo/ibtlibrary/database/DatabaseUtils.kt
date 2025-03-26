package com.ajuliaoo.ibtlibrary.database

import org.jetbrains.exposed.sql.*

infix fun <T : String?> Expression<T>.ilike(pattern: String) = ilike(LikePattern(pattern))

infix fun <T : String?> Expression<T>.ilike(pattern: LikePattern): ILikeEscapeOp =
    ILikeEscapeOp(this, stringParam(pattern.pattern), true, pattern.escapeChar)

class ILikeEscapeOp(expr1: Expression<*>, expr2: Expression<*>, ilike: Boolean, val escapeChar: Char?) :
    ComparisonOp(expr1, expr2, if (ilike) "ILIKE" else "NOT ILIKE") {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) {
        super.toQueryBuilder(queryBuilder)
        if (escapeChar != null) {
            with(queryBuilder) {
                +" ESCAPE "
                +stringParam(escapeChar.toString())
            }
        }
    }
}