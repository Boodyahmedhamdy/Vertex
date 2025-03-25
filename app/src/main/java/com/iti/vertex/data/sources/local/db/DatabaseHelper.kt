package com.iti.vertex.data.sources.local.db

import android.content.Context

object DatabaseHelper {

    private fun getDatabase(context: Context): VertexDatabase = VertexDatabase.getInstance(context)

    fun getForecastDao(context: Context) = getDatabase(context).getForecastDao()

}