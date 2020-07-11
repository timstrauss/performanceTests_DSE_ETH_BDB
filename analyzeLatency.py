import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

technology = "dse"
technologyName = "DSE"
direc = "sameResource/"
direc2 = "differentResource/"


dseSizeBool8 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool8.txt', delimiter="|", unpack=True)[0].mean())
dseSizeBool4 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool4.txt', delimiter="|", unpack=True)[0].mean())
dseSizeBool2 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool2.txt', delimiter="|", unpack=True)[0].mean())
dseSizeBool1 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool1.txt', delimiter="|", unpack=True)[0].mean())
differentResource8 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/getBool8.txt', delimiter="|", unpack=True)[0].mean())
differentResource4 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/getBool4.txt', delimiter="|", unpack=True)[0].mean())
differentResource2 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/getBool2.txt', delimiter="|", unpack=True)[0].mean())

x = np.logspace(0, 3, num=4, base=2)

fig, axes = plt.subplots(3, 3)

plt.subplot(3, 3, 1)
plt.title(technologyName + ": Get bool with different number of threads")
plt.bar(x-x/10, [dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8], width=x/5, label='Same entity per thread')
plt.bar(x+x/10, [0, differentResource2, differentResource4, differentResource8], width=x/5, label='Different entity per thread')
plt.legend()
the_table = plt.table(cellText=[[dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8],['', differentResource2, differentResource4, differentResource8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Latency in us")

dseSizeSet8 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool8.txt', delimiter="|", unpack=True)[0].mean())
dseSizeSet4 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool4.txt', delimiter="|", unpack=True)[0].mean())
dseSizeSet2 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool2.txt', delimiter="|", unpack=True)[0].mean())
dseSizeSet1 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool1.txt', delimiter="|", unpack=True)[0].mean())
differentResource8 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/setBool8.txt', delimiter="|", unpack=True)[0].mean())
differentResource4 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/setBool4.txt', delimiter="|", unpack=True)[0].mean())
differentResource2 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/setBool2.txt', delimiter="|", unpack=True)[0].mean())

plt.subplot(3, 3, 4)
plt.title(technologyName + ": Set bool with different number of threads")
plt.bar(x-x/10, [dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8], width=x/5, label='Same entity per thread')
plt.bar(x+x/10, [0, differentResource2, differentResource4, differentResource8], width=x/5, label='Different entity per thread')
plt.legend()
the_table = plt.table(cellText=[[dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8],['', differentResource2, differentResource4, differentResource8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Latency in us")

dseSizeArray8 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray8.txt', delimiter="|", unpack=True)[0].mean())
dseSizeArray4 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray4.txt', delimiter="|", unpack=True)[0].mean())
dseSizeArray2 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray2.txt', delimiter="|", unpack=True)[0].mean())
dseSizeArray1 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray1.txt', delimiter="|", unpack=True)[0].mean())
differentResource8 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/addArray8.txt', delimiter="|", unpack=True)[0].mean())
differentResource4 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/addArray4.txt', delimiter="|", unpack=True)[0].mean())
differentResource2 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/addArray2.txt', delimiter="|", unpack=True)[0].mean())

plt.subplot(3, 3, 7)
plt.title(technologyName + ": Add array with different number of threads")
plt.bar(x-x/10, [dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8], width=x/5, label='Same entity per thread')
plt.bar(x+x/10, [0, differentResource2, differentResource4, differentResource8], width=x/5, label='Different entity per thread')
plt.legend()
the_table = plt.table(cellText=[[dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8],['', differentResource2, differentResource4, differentResource8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(12)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Latency in us")

technology = "bdb"
technologyName = "BDB"

dseSizeBool8 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool8.txt', delimiter="|", unpack=True)[0].mean())
dseSizeBool4 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool4.txt', delimiter="|", unpack=True)[0].mean())
dseSizeBool2 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool2.txt', delimiter="|", unpack=True)[0].mean())
dseSizeBool1 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool1.txt', delimiter="|", unpack=True)[0].mean())
differentResource8 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/getBool8.txt', delimiter="|", unpack=True)[0].mean())
differentResource4 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/getBool4.txt', delimiter="|", unpack=True)[0].mean())
differentResource2 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/getBool2.txt', delimiter="|", unpack=True)[0].mean())

x = np.logspace(0, 3, num=4, base=2)

plt.subplot(3, 3, 2)
plt.title(technologyName + ": Get bool with different number of threads")
plt.bar(x-x/10, [dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8], width=x/5, label='Same entity per thread')
plt.bar(x+x/10, [0, differentResource2, differentResource4, differentResource8], width=x/5, label='Different entity per thread')
plt.legend()
the_table = plt.table(cellText=[[dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8],['', differentResource2, differentResource4, differentResource8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Latency in us")

dseSizeSet8 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool8.txt', delimiter="|", unpack=True)[0].mean())
dseSizeSet4 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool4.txt', delimiter="|", unpack=True)[0].mean())
dseSizeSet2 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool2.txt', delimiter="|", unpack=True)[0].mean())
dseSizeSet1 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool1.txt', delimiter="|", unpack=True)[0].mean())
differentResource8 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/setBool8.txt', delimiter="|", unpack=True)[0].mean())
differentResource4 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/setBool4.txt', delimiter="|", unpack=True)[0].mean())
differentResource2 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/setBool2.txt', delimiter="|", unpack=True)[0].mean())

plt.subplot(3, 3, 5)
plt.title(technologyName + ": Set bool with different number of threads")
plt.bar(x-x/10, [dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8], width=x/5, label='Same entity per thread')
plt.bar(x+x/10, [0, differentResource2, differentResource4, differentResource8], width=x/5, label='Different entity per thread')
plt.legend()
the_table = plt.table(cellText=[[dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8],['', differentResource2, differentResource4, differentResource8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Latency in us")

dseSizeArray8 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray8.txt', delimiter="|", unpack=True)[0].mean())
dseSizeArray4 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray4.txt', delimiter="|", unpack=True)[0].mean())
dseSizeArray2 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray2.txt', delimiter="|", unpack=True)[0].mean())
dseSizeArray1 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray1.txt', delimiter="|", unpack=True)[0].mean())
differentResource8 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/addArray8.txt', delimiter="|", unpack=True)[0].mean())
differentResource4 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/addArray4.txt', delimiter="|", unpack=True)[0].mean())
differentResource2 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/addArray2.txt', delimiter="|", unpack=True)[0].mean())

plt.subplot(3, 3, 8)
plt.title(technologyName + ": Add array with different number of threads")
plt.bar(x-x/10, [dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8], width=x/5, label='Same entity per thread')
plt.bar(x+x/10, [0, differentResource2, differentResource4, differentResource8], width=x/5, label='Different entity per thread')
plt.legend()
the_table = plt.table(cellText=[[dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8],['', differentResource2, differentResource4, differentResource8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(12)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Latency in us")

technology = "ethereum"
technologyName = "Ethereum"

dseSizeBool8 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool8.txt', delimiter="|", unpack=True)[0].mean())
dseSizeBool4 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool4.txt', delimiter="|", unpack=True)[0].mean())
dseSizeBool2 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool2.txt', delimiter="|", unpack=True)[0].mean())
dseSizeBool1 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/getBool1.txt', delimiter="|", unpack=True)[0].mean())
differentResource8 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/getBool8.txt', delimiter="|", unpack=True)[0].mean())
differentResource4 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/getBool4.txt', delimiter="|", unpack=True)[0].mean())
differentResource2 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/getBool2.txt', delimiter="|", unpack=True)[0].mean())

x = np.logspace(0, 3, num=4, base=2)

plt.subplot(3, 3, 3)
plt.title(technologyName + ": Get bool with different number of threads")
plt.bar(x-x/10, [dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8], width=x/5, label='Same entity per thread')
plt.bar(x+x/10, [0, differentResource2, differentResource4, differentResource8], width=x/5, label='Different entity per thread')
plt.legend()
the_table = plt.table(cellText=[[dseSizeBool1, dseSizeBool2, dseSizeBool4, dseSizeBool8],['', differentResource2, differentResource4, differentResource8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Latency in us")

dseSizeSet8 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool8.txt', delimiter="|", unpack=True)[0].mean())
dseSizeSet4 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool4.txt', delimiter="|", unpack=True)[0].mean())
dseSizeSet2 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool2.txt', delimiter="|", unpack=True)[0].mean())
dseSizeSet1 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/setBool1.txt', delimiter="|", unpack=True)[0].mean())
differentResource8 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/setBool8.txt', delimiter="|", unpack=True)[0].mean())
differentResource4 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/setBool4.txt', delimiter="|", unpack=True)[0].mean())
differentResource2 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/setBool2.txt', delimiter="|", unpack=True)[0].mean())

plt.subplot(3, 3, 6)
plt.title(technologyName + ": Set bool with different number of threads")
plt.bar(x-x/10, [dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8], width=x/5, label='Same entity per thread')
plt.bar(x+x/10, [0, differentResource2, differentResource4, differentResource8], width=x/5, label='Different entity per thread')
plt.legend()
the_table = plt.table(cellText=[[dseSizeSet1, dseSizeSet2, dseSizeSet4, dseSizeSet8],['', differentResource2, differentResource4, differentResource8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(14)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Latency in us")

dseSizeArray8 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray8.txt', delimiter="|", unpack=True)[0].mean())
dseSizeArray4 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray4.txt', delimiter="|", unpack=True)[0].mean())
dseSizeArray2 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray2.txt', delimiter="|", unpack=True)[0].mean())
dseSizeArray1 = int(np.loadtxt('./benchmarks/' + direc + '' + technology + '/addArray1.txt', delimiter="|", unpack=True)[0].mean())
differentResource8 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/addArray8.txt', delimiter="|", unpack=True)[0].mean())
differentResource4 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/addArray4.txt', delimiter="|", unpack=True)[0].mean())
differentResource2 = int(np.loadtxt('./benchmarks/' + direc2 + '' + technology + '/addArray2.txt', delimiter="|", unpack=True)[0].mean())

plt.subplot(3, 3, 9)
plt.title(technologyName + ": Add array with different number of threads")
plt.bar(x-x/10, [dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8], width=x/5, label='Same entity per thread')
plt.bar(x+x/10, [0, differentResource2, differentResource4, differentResource8], width=x/5, label='Different entity per thread')
plt.legend()
the_table = plt.table(cellText=[[dseSizeArray1, dseSizeArray2, dseSizeArray4, dseSizeArray8],['', differentResource2, differentResource4, differentResource8]],
                      colLabels=["1 Thread", "2 Threads", "4 Threads", "8 Threads"],
                      loc='bottom')

the_table.auto_set_font_size(False)
the_table.set_fontsize(12)

plt.subplots_adjust(bottom=0.2)
plt.xscale('log', basex=2)
plt.xticks([])
plt.ylabel("Latency in us")

fig.tight_layout(pad=1.0)

plt.show()
