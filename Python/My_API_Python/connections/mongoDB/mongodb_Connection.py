import pymongo

from shared.Shared_Paths import Shared_Path


class MongoDBConnection:
    def __init__(self):
        self.db = None
        self.create_connection()

    def create_connection(self):
        shared_path = Shared_Path()
        url = shared_path.mongo
        self.db = pymongo.MongoClient('mongodb://' + url['host'] + url['port'])

    def close_connection(self):
        self.db.close()

    def execute_query(self, query):
        return self.db.db.collection.find(query)
