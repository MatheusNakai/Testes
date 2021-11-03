import os
import sqlite3

path = os.path.abspath('data_base\\meubancodedados.db')
conn = sqlite3.connect(path)

def get_user_id(user, conn):
    c = conn.cursor()
    c.execute("select user_id from User where username=?;", (user,))
    conn.commit()
    row = c.fetchall()
    return row[0][0]

def find_user_by_name(user, conn):
    c = conn.cursor()
    c.execute("select user_id from User where username=?", (user,))
    conn.commit()
    row = c.fetchall()
    if len(row) == 0:
        return False
    return True

def add_user(user, conn):
    c = conn.cursor()
    c.execute("insert into User (username) values (?);", (user,))
    conn.commit()

conn.close()
