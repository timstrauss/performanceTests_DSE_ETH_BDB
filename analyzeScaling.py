import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

technology = "dse"
technologyName = "DSE"
direc = "sameResource/"

dseSizeBool8 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool8.txt', delimiter="|", unpack=True)[0])
dseSizeBool4 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool4.txt', delimiter="|", unpack=True)[0])
dseSizeBool2 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool2.txt', delimiter="|", unpack=True)[0])
dseSizeBool1 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool1.txt', delimiter="|", unpack=True)[0])

x = np.logspace(0, 3, num=4, base=2)

fig, axes = plt.subplots(3, 3)

plt.subplot(3, 3, 1)
plt.title(technologyName + ": Get bool with different number of threads")
plt.bar(x, [dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8], width=x/3)
the_table = plt.table(cellText=[[dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Transactions")

dseSizeSet8 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool8.txt', delimiter="|", unpack=True)[0])
dseSizeSet4 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool4.txt', delimiter="|", unpack=True)[0])
dseSizeSet2 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool2.txt', delimiter="|", unpack=True)[0])
dseSizeSet1 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool1.txt', delimiter="|", unpack=True)[0])

plt.subplot(3, 3, 4)
plt.title(technologyName + ": Set bool with different number of threads")
plt.bar(x, [dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8], width=x/3)
the_table = plt.table(cellText=[[dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Transactions")

dseSizeArray8 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray8.txt', delimiter="|", unpack=True)[0])
dseSizeArray4 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray4.txt', delimiter="|", unpack=True)[0])
dseSizeArray2 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray2.txt', delimiter="|", unpack=True)[0])
dseSizeArray1 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray1.txt', delimiter="|", unpack=True)[0])

plt.subplot(3, 3, 7)
plt.title(technologyName + ": Add array with different number of threads")
plt.bar(x, [dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8], width=x/3)
the_table = plt.table(cellText=[[dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(12)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Transactions")

technology = "bdb"
technologyName = "BDB"

dseSizeBool8 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool8.txt', delimiter="|", unpack=True)[0])
dseSizeBool4 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool4.txt', delimiter="|", unpack=True)[0])
dseSizeBool2 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool2.txt', delimiter="|", unpack=True)[0])
dseSizeBool1 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool1.txt', delimiter="|", unpack=True)[0])

x = np.logspace(0, 3, num=4, base=2)

plt.subplot(3, 3, 2)
plt.title(technologyName + ": Get bool with different number of threads")
plt.bar(x, [dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8], width=x/3)
the_table = plt.table(cellText=[[dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Transactions")

dseSizeSet8 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool8.txt', delimiter="|", unpack=True)[0])
dseSizeSet4 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool4.txt', delimiter="|", unpack=True)[0])
dseSizeSet2 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool2.txt', delimiter="|", unpack=True)[0])
dseSizeSet1 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool1.txt', delimiter="|", unpack=True)[0])

plt.subplot(3, 3, 5)
plt.title(technologyName + ": Set bool with different number of threads")
plt.bar(x, [dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8], width=x/3)
the_table = plt.table(cellText=[[dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Transactions")

dseSizeArray8 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray8.txt', delimiter="|", unpack=True)[0])
dseSizeArray4 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray4.txt', delimiter="|", unpack=True)[0])
dseSizeArray2 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray2.txt', delimiter="|", unpack=True)[0])
dseSizeArray1 = len(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray1.txt', delimiter="|", unpack=True)[0])

plt.subplot(3, 3, 8)
plt.title(technologyName + ": Add array with different number of threads")
plt.bar(x, [dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8], width=x/3)
the_table = plt.table(cellText=[[dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(12)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Transactions")

fig.tight_layout(pad=1.0)

plt.show()
