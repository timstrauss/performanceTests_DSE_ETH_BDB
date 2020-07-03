import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

size10 = len(np.loadtxt('./benchmarks/dse/getBool10.txt', delimiter="|", unpack=True)[0])
size2 = len(np.loadtxt('./benchmarks/dse/getBool2.txt', delimiter="|", unpack=True)[0])
size1 = len(np.loadtxt('./benchmarks/dse/getBool1.txt', delimiter="|", unpack=True)[0])

plt.plot(["Thread 1", "Thread 2", "Thread 10"],[size1, size2, size10])

plt.show()
