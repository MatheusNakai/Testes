import mysql.connector
from mysql.connector import errorcode

from shared.Shared_Paths import Shared_Path


class MySQL_Connection:

    def __init__(self):
        self.db = None
        self.create_connection()

    def create_connection(self):
        shared_path = Shared_Path()
        try:
            self.db = mysql.connector.connect(**shared_path.config)
        except mysql.connector.Error as err:
            if err.errno == errorcode.ER_ACCESS_DENIED_ERROR:
                print("Something is wrong with your user name or password")
            elif err.errno == errorcode.ER_BAD_DB_ERROR:
                print("Database does not exist")
            else:
                print(err)
        if self.db.is_connected():
            print("Connection established")

    def close_connection(self):
        self.db.close()

    def execute_query(self, query: str):
        cursor = self.db.cursor(dictionary=True)
        cursor.execute(query)
        return cursor

    def read(self, table: str, where: str):
        result = self.execute_query(f"SELECT * FROM {table} WHERE {where}")
        for row in result:
            print(row)

    def create(self, table: str, dictionary: dict):
        keys = ""
        values = ""
        for key, value in dictionary.items():
            keys += f"{key},"
            values += f"'{value}',"
        keys = keys[:-1]
        values = values[:-1]
        query = f"INSERT INTO {table} ({keys}) VALUES ({values})"
        self.execute_query(query)

    def update(self, table: str, dictionary: dict, id: str):
        query = f"UPDATE {table} SET "
        for key, value in dictionary.items():
            query += f"{key}='{value}',"
        query = query[:-1]
        query += f" WHERE id={id}"
        self.execute_query(query)
        self.db.commit()

    def delete(self, table: str, id: str):
        query = f"DELETE FROM {table} WHERE id={id}"
        self.execute_query(query)
        self.db.commit()

    def get_Id(self, table: str, column: str, value: str):
        query = f"SELECT id FROM {table} WHERE {column}='{value}'"
        result = self.execute_query(query)
        for row in result:
            return row[0]

    def get_By_Id(self, id: int, table: str):
        query = f"SELECT * FROM {table} WHERE id={id}"
        result = self.execute_query(query)
        for row in result:
            return row

    def get_All(self, table: str):
        list_Of_Dictionary = []
        query = f"SELECT * FROM {table}"
        result = self.execute_query(query)
        for row in result:
            list_Of_Dictionary.append(row)
        return list_Of_Dictionary

