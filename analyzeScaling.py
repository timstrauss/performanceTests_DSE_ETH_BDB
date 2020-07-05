import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

dseSize8 = len(np.loadtxt('./benchmarks/dse/getBool8.txt', delimiter="|", unpack=True)[0])
dseSize4 = len(np.loadtxt('./benchmarks/dse/getBool4.txt', delimiter="|", unpack=True)[0])
dseSize2 = len(np.loadtxt('./benchmarks/dse/getBool2.txt', delimiter="|", unpack=True)[0])
dseSize1 = len(np.loadtxt('./benchmarks/dse/getBool1.txt', delimiter="|", unpack=True)[0])

plt.subplot(1, 3, 1)
plt.plot([1,2,4,8],[dseSize1, dseSize2, dseSize4, dseSize8])
the_table = plt.table(cellText=[dseSize1, dseSize2, dseSize4, dseSize8],
                      rowLabels=[1],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')
plt.show()
