class User:
    def __init__(self, id, name, lvl, exp):
        self.id = id
        self.name = name
        self.lvl = lvl
        self.exp = exp

    def __str__(self):
        return 'User: {}'.format(self.name)

    def addExp(self, exp):
        self.exp += exp