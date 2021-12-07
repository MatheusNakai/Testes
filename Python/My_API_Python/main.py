from pprint import pprint

from connections import mysql
from shared.Shared_Paths import Shared_Path
import mysql.connector
from connections.mysql.MySQL_Connection import MySQL_Connection

if __name__ == '__main__':
    db = MySQL_Connection()

