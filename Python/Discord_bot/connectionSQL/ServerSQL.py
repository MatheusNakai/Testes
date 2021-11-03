import os
import sqlite3

path = os.path.abspath('data_base\\meubancodedados.db')
conn = sqlite3.connect(path)

def get_server_id(server, conn):
    c = conn.cursor()
    c.execute("select server_id from Server where server_name=?;", (server,))
    conn.commit()
    row = c.fetchall()
    return row[0][0]

def find_server_by_name(server, conn):
    c = conn.cursor()
    c.execute("select server_id from Server where server_name=?", (server,))
    conn.commit()
    row = c.fetchall()
    if len(row) == 0:
        return False
    return True

def add_server(server, conn):
    c = conn.cursor()
    c.execute("insert into Server (server_name) values (?);", (server,))
    conn.commit()

conn.close()