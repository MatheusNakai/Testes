package com.example.connections.mysql

import com.example.connections.Generic_Actions
import com.example.connections.errors.DATABASE_CONNECTION_FAILED
import com.example.shared.Shared_Paths
import io.ktor.network.sockets.Connection
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
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

    private fun create_Connection(): Connection{
        try{
            val c  = DriverManager.getConnection(
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

    private fun checkConnection(){
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
        checkConnection()
        return this.connection.create_Statement()
    }

    private fun executeStatement(query: String) {
        checkConnection()
        if (this.SQL_Statement.isClosed){
            this.SQL_Statement = create_Statement()
        }
        val preparedStatement = this.connection.prepare_Statement(query)
        preparedStatement!!.executeUpdate()

    }

    override fun read(table: String, where: String) {
        TODO("Not yet implemented")
    }

    override fun create(table: String, data: HashMap<String, String>) {
        TODO("Not yet implemented")
    }

    override fun update(table: String, data: HashMap<String, String>) {
        TODO("Not yet implemented")
    }

    override fun delete(table: String, id: Int) {
        TODO("Not yet implemented")
    }

    override fun getId(table: String, column: String, value: String): Int {
        TODO("Not yet implemented")
    }

    override fun getById(table: String, id: Int): Map<String, String>? {
        TODO("Not yet implemented")
    }

    override fun getAll(table: String, where: String): List<Map<String, String>> {
        TODO("Not yet implemented")
    }
}