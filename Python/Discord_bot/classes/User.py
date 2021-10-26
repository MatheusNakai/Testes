class User:
    def __init__(self, id, name, lvl, exp):
        self.id = id
        self.name = name

    def __str__(self):
        return 'User: {}'.format(self.name)