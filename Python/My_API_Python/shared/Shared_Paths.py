class Shared_Path:
    def __init__(self):
        self.mysql = {
            'user': 'root',
            'password': 'password',
            'host': 'localhost',
            'raise_on_warnings': True,
            'buffered': True,
            'database': 'MAL'
        }
        self.mongo = {
            'host': 'localhost',
            'port': 27017,
        }
