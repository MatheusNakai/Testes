import matplotlib.pyplot as plt

def conllatz_conjecture(number):
    if number % 2 == 0:
        return number // 2
    else:
        return 3 * number + 1

def plot(list_of_points):
    plt.plot(list_of_points)
    plt.show()
    
for i in range(1, 10000):
    list_of_points = [i]
    while i != 1:
        i = conllatz_conjecture(i)
        list_of_points.append(i)


plot(list_of_points)