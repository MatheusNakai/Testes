class Teste:
    soma = 0
    te=0
    
    def __init__(self, valor):
        self.soma += valor
        self.te=0

    def __str__(self):
       return str(self.soma)
t1 = Teste(1)

print(t1)