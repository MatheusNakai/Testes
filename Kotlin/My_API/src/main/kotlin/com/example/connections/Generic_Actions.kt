package com.example.connections

interface Generic_Actions {
    fun read(table: String, where: String = "True")

    fun create(table: String, data: HashMap<String, String>)

    fun update(table: String, data: HashMap<String, String>)

    fun delete(table: String, id: Int)

    fun get_Id(table: String, column: String, value: String): Int

    fun get_By_Id(table: String, id: Int): Map<String, String>?

    fun get_All(table: String, where: String = "True"): List<Map<String,String>>
}
