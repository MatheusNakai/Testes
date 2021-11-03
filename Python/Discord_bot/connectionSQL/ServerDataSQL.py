import os
import sqlite3

path = os.path.abspath('data_base\\meubancodedados.db')
conn = sqlite3.connect(path)


def lvl_up(user_id, server_id, conn):
    c = conn.cursor()
    c.execute("update ServerData set level= level +1, exp = 0 where user_id=? and server_id=?;", (user_id, server_id))
    conn.commit()


def exp_up(user_id, server_id, exp_msg, conn):
    c = conn.cursor()
    c.execute("update ServerData set exp = exp+? where user_id=? and server_id=?;", (exp_msg, user_id, server_id))
    conn.commit()


def find_user_server(user_id, server_id, conn):
    c = conn.cursor()
    c.execute("select * from ServerData where (user_id=? and server_id=?)", [user_id, server_id])
    conn.commit()
    row = c.fetchall()
    if len(row) == 0:
        return False
    return True


def add_user_on_server(user_id, server_id, conn):
    c = conn.cursor()
    c.execute("insert into ServerData (user_id, server_id) values (?,?);", (user_id, server_id,))
    conn.commit()
