package com.example.connections.mysql

import com.example.connections.Generic_Actions
import com.example.connections.errors.DATABASE_CONNECTION_FAILED
import com.example.shared.Shared_Paths
import com.example.utils.DB_Table_Printer
import java.sql.Connection
import java.sql.*

class MySQL_Connection(db_name: String) :Generic_Actions{
    private var connection: Connection
    private var SQL_Statement : Statement
    private val DB_Name : String

    companion object{
        val url = Shared_Paths.DATABASE_URL
        val port = Shared_Paths.port
    }

    init{
        this.connection = create_Connection()
        this.SQL_Statement = create_Statement()
        this.DB_Name = db_name
    }

    private fun create_Connection(): Connection {
        var c : Connection
        try{
            c  = DriverManager.getConnection(
                "jdbc:mysql://$url:$port/?characterEncoding=utf8&useUnicode=true",
                "root",
                "password"
            )
            println("Connection created")
            return c
        }
        catch (e:SQLException){
            e.printStackTrace()
            throw DATABASE_CONNECTION_FAILED()
        }
    }

    private fun check_Connection(){
        try {
            if (connection.isClosed) {
                connection = create_Connection()
            }
        }
        catch (e: SQLException){
            e.printStackTrace()
            throw DATABASE_CONNECTION_FAILED()
        }
    }

    @Throws(SQLException::class)
    private fun resultSetToList(rs: ResultSet): MutableList<Map<String, String>> {
        val md = rs.metaData
        val columns = md.columnCount
        val rows: MutableList<Map<String, String>> = ArrayList()
        while (rs.next()) {
            val row: MutableMap<String, String> = HashMap(columns)
            for (i in 1..columns) {
                row[md.getColumnName(i)] = rs.getString(i)
            }
            rows.add(row)
        }
        return rows
    }

    private fun create_Statement(): Statement {
        check_Connection()
        return this.connection.createStatement()
    }

    private fun execute_Statement(query: String) {
        check_Connection()
        if (this.SQL_Statement.isClosed){
            this.SQL_Statement = create_Statement()
        }
        val preparedStatement = this.connection.prepareStatement(query)
        preparedStatement!!.executeUpdate()

    }

    override fun read(table: String, where: String){
        check_Connection()
        var result: ResultSet = this.SQL_Statement.executeQuery("SELECT * FROM ${this.DB_Name}.${table} WHERE ${where}")
        DB_Table_Printer.printResultSet(result)
    }

    override fun create(table: String, data: HashMap<String, String>){
        check_Connection()
        data.remove("id")
        execute_Statement("INSERT INTO ${this.DB_Name}.${table} (${data.keys.joinToString { it -> it }}) values (${data.values.joinToString { it -> "\'${it}\'" }});")
    }

    override fun update(table: String, data: HashMap<String, String>){
        check_Connection()
        val id = data.remove("id")
        execute_Statement("UPDATE ${this.DB_Name}.${table} SET ${data.keys.joinToString { it -> "$it = \'${data[it]}\'" }} WHERE id = $id")
    }

    override fun delete(table: String, id: Int){
        check_Connection()
        execute_Statement("DELETE FROM ${this.DB_Name}.${table} WHERE id = $id")
    }
    
    override fun get_Id(table: String, column: String, value: String): Int {
        check_Connection()
        val id: ResultSet = this.SQL_Statement.executeQuery("SELECT id FROM ${this.DB_Name}.${table} WHERE ${column} = '${value}'")
        if (id.next()) {
            val idNumber = id.getObject("id") as Int?
            return idNumber?: -1
        }
        id.close()
        return -1
    }

    override fun get_By_Id(table: String, id: Int): Map<String, String>? {
        check_Connection()
        val result: ResultSet = this.SQL_Statement.executeQuery("SELECT * FROM ${this.DB_Name}.${table} WHERE id = '${id}'")
        val map = resultSetToList(result)
        result.close()
        return map[0]
    }

    override fun get_All(table: String, where: String): List<Map<String,String>> {
        check_Connection()
        val result: ResultSet = this.SQL_Statement.executeQuery("SELECT * FROM ${this.DB_Name}.${table} WHERE ${where}")
        val map = resultSetToList(result)
        result.close()
        return map
    }
}