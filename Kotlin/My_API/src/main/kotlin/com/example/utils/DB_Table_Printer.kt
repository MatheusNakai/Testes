package com.example.utils

import java.sql.*
import java.util.*

/*
Database Table Printer
Copyright (C) 2014  Hami Galip Torun

Email: hamitorun@e-fabrika.net
Project Home: https://github.com/htorun/dbtableprinter

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
This is my first Java program that does something more or less
useful. It is part of my effort to learn Java, how to use
an IDE (IntelliJ IDEA 13.1.15 in this case), how to apply an
open source license and how to use Git and GitHub (https://github.com)
for version control and publishing an open source software.

Hami
 */



/**
 * Just a utility to print rows from a given DB table or a
 * `ResultSet` to standard out, formatted to look
 * like a table with rows and columns with borders.
 *
 *
 * Stack Overflow website
 * ([stackoverflow.com](http://stackoverflow.com))
 * was the primary source of inspiration and help to put this
 * code together. Especially the questions and answers of
 * the following people were very useful:
 *
 *
 * Question:
 * [How to display or
 * print the contents of a database table as is](http://tinyurl.com/q7lbqeh)<br></br>
 * People: sky scraper
 *
 *
 * Question:
 * [System.out.println()
 * from database into a table](http://tinyurl.com/pbwgess)<br></br>
 * People: Simon Cottrill, Tony Toews, Costis Aivali, Riggy, corsiKa
 *
 *
 * Question:
 * [Simple way to repeat
 * a string in java](http://tinyurl.com/7x9qtyg)<br></br>
 * People: Everybody who contributed but especially user102008
 *
 */
object DB_Table_Printer{
    /**
     * Default maximum number of rows to query and print.
     */
    private const val DEFAULT_MAX_ROWS = 10

    /**
     * Default maximum width for text columns
     * (like a `VARCHAR`) column.
     */
    private const val DEFAULT_MAX_TEXT_COL_WIDTH = 150

    /**
     * Column type category for `CHAR`, `VARCHAR`
     * and similar text columns.
     */
    const val CATEGORY_STRING = 1

    /**
     * Column type category for `TINYINT`, `SMALLINT`,
     * `INT` and `BIGINT` columns.
     */
    const val CATEGORY_INTEGER = 2

    /**
     * Column type category for `REAL`, `DOUBLE`,
     * and `DECIMAL` columns.
     */
    const val CATEGORY_DOUBLE = 3

    /**
     * Column type category for date and time related columns like
     * `DATE`, `TIME`, `TIMESTAMP` etc.
     */
    const val CATEGORY_DATETIME = 4

    /**
     * Column type category for `BOOLEAN` columns.
     */
    const val CATEGORY_BOOLEAN = 5

    /**
     * Column type category for types for which the type name
     * will be printed instead of the content, like `BLOB`,
     * `BINARY`, `ARRAY` etc.
     */
    const val CATEGORY_OTHER = 0
    /**
     * Overloaded method that prints rows from table `tableName`
     * to standard out using the given database connection
     * `conn`. Total number of rows will be limited to
     * `maxRows` and
     * `maxStringColWidth` will be used to limit
     * the width of text columns (like a `VARCHAR` column).
     *
     * @param conn Database connection object (java.sql.Connection)
     * @param tableName Name of the database table
     * @param maxRows Number of max. rows to query and print
     * @param maxStringColWidth Max. width of text columns
     */
    /**
     * Overloaded method that prints rows from table `tableName`
     * to standard out using the given database connection
     * `conn`. Total number of rows will be limited to
     * [.DEFAULT_MAX_ROWS] and
     * [.DEFAULT_MAX_TEXT_COL_WIDTH] will be used to limit
     * the width of text columns (like a `VARCHAR` column).
     *
     * @param conn Database connection object (java.sql.Connection)
     * @param tableName Name of the database table
     */
    /**
     * Overloaded method that prints rows from table `tableName`
     * to standard out using the given database connection
     * `conn`. Total number of rows will be limited to
     * `maxRows` and
     * [.DEFAULT_MAX_TEXT_COL_WIDTH] will be used to limit
     * the width of text columns (like a `VARCHAR` column).
     *
     * @param conn Database connection object (java.sql.Connection)
     * @param tableName Name of the database table
     * @param maxRows Number of max. rows to query and print
     */
    @JvmOverloads
    fun printTable(
        conn: Connection?,
        tableName: String?,
        maxRows: Int = DEFAULT_MAX_ROWS,
        maxStringColWidth: Int = DEFAULT_MAX_TEXT_COL_WIDTH
    ) {
        var maxRows = maxRows
        if (conn == null) {
            System.err.println("DBTablePrinter Error: No connection to database (Connection is null)!")
            return
        }
        if (tableName == null) {
            System.err.println("DBTablePrinter Error: No table name (tableName is null)!")
            return
        }
        if (tableName.isEmpty()) {
            System.err.println("DBTablePrinter Error: Empty table name!")
            return
        }
        if (maxRows < 1) {
            System.err.println("DBTablePrinter Info: Invalid max. rows number. Using default!")
            maxRows = DEFAULT_MAX_ROWS
        }
        var stmt: Statement? = null
        var rs: ResultSet? = null
        try {
            if (conn.isClosed) {
                System.err.println("DBTablePrinter Error: Connection is closed!")
                return
            }
            val sqlSelectAll = "SELECT * FROM $tableName LIMIT $maxRows"
            stmt = conn.createStatement()
            rs = stmt.executeQuery(sqlSelectAll)
            printResultSet(rs, maxStringColWidth)
        } catch (e: SQLException) {
            System.err.println("SQL exception in DBTablePrinter. Message:")
            System.err.println(e.message)
        } finally {
            try {
                stmt?.close()
                rs?.close()
            } catch (ignore: SQLException) {
                // ignore
            }
        }
    }
    /**
     * Overloaded method to print rows of a [
 * ResultSet](http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html) to standard out using `maxStringColWidth`
     * to limit the width of text columns.
     *
     * @param rs The `ResultSet` to print
     * @param maxStringColWidth Max. width of text columns
     */
    /**
     * Overloaded method to print rows of a [
 * ResultSet](http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html) to standard out using [.DEFAULT_MAX_TEXT_COL_WIDTH]
     * to limit the width of text columns.
     *
     * @param rs The `ResultSet` to print
     */
    @JvmOverloads
    fun printResultSet(rs: ResultSet?, maxStringColWidth: Int = DEFAULT_MAX_TEXT_COL_WIDTH) {
        var maxStringColWidth = maxStringColWidth
        try {
            if (rs == null) {
                System.err.println("DBTablePrinter Error: Result set is null!")
                return
            }
            if (rs.isClosed) {
                System.err.println("DBTablePrinter Error: Result Set is closed!")
                return
            }
            if (maxStringColWidth < 1) {
                System.err.println("DBTablePrinter Info: Invalid max. varchar column width. Using default!")
                maxStringColWidth = DEFAULT_MAX_TEXT_COL_WIDTH
            }

            // Get the meta data object of this ResultSet.
            val rsmd: ResultSetMetaData = rs.metaData

            // Total number of columns in this ResultSet
            val columnCount = rsmd.columnCount

            // List of Column objects to store each columns of the ResultSet
            // and the String representation of their values.
            val columns: MutableList<Column> = ArrayList(columnCount)

            // List of table names. Can be more than one if it is a joined
            // table query
            val tableNames: MutableList<String> = ArrayList(columnCount)

            // Get the columns and their meta data.
            // NOTE: columnIndex for rsmd.getXXX methods STARTS AT 1 NOT 0
            for (i in 1..columnCount) {
                val c = Column(
                    rsmd.getColumnLabel(i),
                    rsmd.getColumnType(i), rsmd.getColumnTypeName(i)
                )
                c.width = c.label.length
                c.typeCategory = whichCategory(c.type)
                columns.add(c)
                if (!tableNames.contains(rsmd.getTableName(i))) {
                    tableNames.add(rsmd.getTableName(i))
                }
            }

            // Go through each row, get values of each column and adjust
            // column widths.
            var rowCount = 0
            while (rs.next()) {

                // NOTE: columnIndex for rs.getXXX methods STARTS AT 1 NOT 0
                for (i in 0 until columnCount) {
                    val c = columns[i]
                    var value: String
                    val category = c.typeCategory
                    value = if (category == CATEGORY_OTHER) {

                        // Use generic SQL type name instead of the actual value
                        // for column types BLOB, BINARY etc.
                        "(" + c.typeName + ")"
                    } else {
                        if (rs.getString(i + 1) == null) "NULL" else rs.getString(i + 1)
                    }
                    when (category) {
                        CATEGORY_DOUBLE ->
                            // For real numbers, format the string value to have 3 digits
                            // after the point. THIS IS TOTALLY ARBITRARY and can be
                            // improved to be CONFIGURABLE.
                            if (value != "NULL") {
                                val dValue = rs.getDouble(i + 1)
                                value = String.format("%.3f", dValue)
                            }
                        CATEGORY_STRING -> {

                            // Left justify the text columns
                            c.justifyLeft()

                            // and apply the width limit
                            if (value.length > maxStringColWidth) {
                                value = value.substring(0, maxStringColWidth - 3) + "..."
                            }
                        }
                    }

                    // Adjust the column width
                    c.width = if (value.length > c.width) value.length else c.width
                    c.addValue(value)
                } // END of for loop columnCount
                rowCount++
            } // END of while (rs.next)

            /*
            At this point we have gone through meta data, get the
            columns and created all Column objects, iterated over the
            ResultSet rows, populated the column values and adjusted
            the column widths.

            We cannot start printing just yet because we have to prepare
            a row separator String.
             */

            // For the fun of it, I will use StringBuilder
            val strToPrint = StringBuilder()
            val rowSeparator = StringBuilder()

            /*
            Prepare column labels to print as well as the row separator.
            It should look something like this:
            +--------+------------+------------+-----------+  (row separator)
            | EMP_NO | BIRTH_DATE | FIRST_NAME | LAST_NAME |  (labels row)
            +--------+------------+------------+-----------+  (row separator)
             */

            // Iterate over columns
            for (c in columns) {
                var width = c.width

                // Center the column label
                var toPrint: String
                val name = c.label
                var diff = width - name.length
                if (diff % 2 == 1) {
                    // diff is not divisible by 2, add 1 to width (and diff)
                    // so that we can have equal padding to the left and right
                    // of the column label.
                    width++
                    diff++
                    c.width = width
                }
                val paddingSize = diff / 2 // InteliJ says casting to int is redundant.

                // Cool String repeater code thanks to user102008 at stackoverflow.com
                // (http://tinyurl.com/7x9qtyg) "Simple way to repeat a string in java"
                val padding = String(CharArray(paddingSize)).replace("\u0000", " ")
                toPrint = "| $padding$name$padding "
                // END centering the column label
                strToPrint.append(toPrint)
                rowSeparator.append("+")
                rowSeparator.append(String(CharArray(width + 2)).replace("\u0000", "-"))
            }
            var lineSeparator = System.getProperty("line.separator")

            // Is this really necessary ??
            lineSeparator = lineSeparator ?: "\n"
            rowSeparator.append("+").append(lineSeparator)
            strToPrint.append("|").append(lineSeparator)
            strToPrint.insert(0, rowSeparator)
            strToPrint.append(rowSeparator)
            val sj = StringJoiner(", ")
            for (name in tableNames) {
                sj.add(name)
            }
            var info = "Printing $rowCount"
            info += if (rowCount > 1) " rows from " else " row from "
            info += if (tableNames.size > 1) "tables " else "table "
            info += sj.toString()
            println(info)

            // Print out the formatted column labels
            print(strToPrint.toString())
            var format: String

            // Print out the rows
            for (i in 0 until rowCount) {
                for (c in columns) {

                    // This should form a format string like: "%-60s"
                    format = String.format("| %%%s%ds ", c.justifyFlag, c.width)
                    print(String.format(format, c.getValue(i)))
                }
                println("|")
                print(rowSeparator)
            }
            println()

            /*
                Hopefully this should have printed something like this:
                +--------+------------+------------+-----------+--------+-------------+
                | EMP_NO | BIRTH_DATE | FIRST_NAME | LAST_NAME | GENDER |  HIRE_DATE  |
                +--------+------------+------------+-----------+--------+-------------+
                |  10001 | 1953-09-02 | Georgi     | Facello   | M      |  1986-06-26 |
                +--------+------------+------------+-----------+--------+-------------+
                |  10002 | 1964-06-02 | Bezalel    | Simmel    | F      |  1985-11-21 |
                +--------+------------+------------+-----------+--------+-------------+
             */
        } catch (e: SQLException) {
            System.err.println("SQL exception in DBTablePrinter. Message:")
            System.err.println(e.message)
        }
    }

    /**
     * Takes a generic SQL type and returns the category this type
     * belongs to. Types are categorized according to print formatting
     * needs:
     *
     *
     * Integers should not be truncated so column widths should
     * be adjusted without a column width limit. Text columns should be
     * left justified and can be truncated to a max. column width etc...
     *
     * See also: [
 * java.sql.Types](http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html)
     *
     * @param type Generic SQL type
     * @return The category this type belongs to
     */
    private fun whichCategory(type: Int): Int {
        return when (type) {
            Types.BIGINT, Types.TINYINT, Types.SMALLINT, Types.INTEGER -> CATEGORY_INTEGER
            Types.REAL, Types.DOUBLE, Types.DECIMAL -> CATEGORY_DOUBLE
            Types.DATE, Types.TIME, Types.TIME_WITH_TIMEZONE, Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE -> CATEGORY_DATETIME
            Types.BOOLEAN -> CATEGORY_BOOLEAN
            Types.VARCHAR, Types.NVARCHAR, Types.LONGVARCHAR, Types.LONGNVARCHAR, Types.CHAR, Types.NCHAR -> CATEGORY_STRING
            else -> CATEGORY_OTHER
        }
    }

    /**
     * Represents a database table column.
     */
    private class Column(
        /**
         * Column label.
         */
        val label: String,
        /**
         * Generic SQL type of the column as defined in
         * [
 * java.sql.Types
](http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html) * .
         */
        var type: Int, typeName: String
    ) {
        /**
         * Returns the column label
         *
         * @return Column label
         */
        /**
         * Returns the generic SQL type of the column
         *
         * @return Generic SQL type
         */
        /**
         * Returns the generic SQL type name of the column
         *
         * @return Generic SQL type name
         */
        /**
         * Generic SQL type name of the column as defined in
         * [
 * java.sql.Types
](http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html) * .
         */
        val typeName: String
        /**
         * Returns the width of the column
         *
         * @return Column width
         */
        /**
         * Sets the width of the column to `width`
         *
         * @param width Width of the column
         */
        /**
         * Width of the column that will be adjusted according to column label
         * and values to be printed.
         */
        var width = 0

        /**
         * Column values from each row of a `ResultSet`.
         */
        private val values: MutableList<String> = ArrayList()
        /**
         * Returns the value of the [.justifyFlag]. The column
         * values will be printed using `String.format` and
         * this flag will be used to right or left justify the text.
         *
         * @return The [.justifyFlag] of this column
         * @see .justifyLeft
         */
        /**
         * Flag for text justification using `String.format`.
         * Empty string (`""`) to justify right,
         * dash (`-`) to justify left.
         *
         * @see .justifyLeft
         */
        var justifyFlag = ""
            private set
        /**
         * Returns the generic SQL type category of the column
         *
         * @return The [.typeCategory] of the column
         */
        /**
         * Sets the [.typeCategory] of the column
         *
         * @param typeCategory The type category
         */
        /**
         * Column type category. The columns will be categorised according
         * to their column types and specific needs to print them correctly.
         */
        var typeCategory = 0

        /**
         * Adds a `String` representation (`value`)
         * of a value to this column object's [.values] list.
         * These values will come from each row of a
         * [
 * ResultSet
](http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html) *  of a database query.
         *
         * @param value The column value to add to [.values]
         */
        fun addValue(value: String) {
            values.add(value)
        }

        /**
         * Returns the column value at row index `i`.
         * Note that the index starts at 0 so that `getValue(0)`
         * will get the value for this column from the first row
         * of a [
 * ResultSet](http://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html).
         *
         * @param i The index of the column value to get
         * @return The String representation of the value
         */
        fun getValue(i: Int): String {
            return values[i]
        }

        /**
         * Sets [.justifyFlag] to `"-"` so that
         * the column value will be left justified when printed with
         * `String.format`. Typically numbers will be right
         * justified and text will be left justified.
         */
        fun justifyLeft() {
            justifyFlag = "-"
        }

        /**
         * Constructs a new `Column` with a column label,
         * generic SQL type and type name (as defined in
         * [
 * java.sql.Types
](http://docs.oracle.com/javase/8/docs/api/java/sql/Types.html) * )
         *
         * @param label Column label or name
         * @param type Generic SQL type
         * @param typeName Generic SQL type name
         */
        init {
            type = type
            this.typeName = typeName
        }
    }
}