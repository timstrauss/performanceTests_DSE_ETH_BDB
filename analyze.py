import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

ethGetBool1 = np.loadtxt('./benchmarks/ethereum/getBool1.txt', delimiter="|", unpack=True)
ethGetBool2 = np.loadtxt('./benchmarks/ethereum/getBool2.txt', delimiter="|", unpack=True)
ethGetBool5 = np.loadtxt('./benchmarks/ethereum/getBool5.txt', delimiter="|", unpack=True)
ethGetBool10 = np.loadtxt('./benchmarks/ethereum/getBool10.txt', delimiter="|", unpack=True)
ethGetInt1 = np.loadtxt('./benchmarks/ethereum/getInt1.txt', delimiter="|", unpack=True)
ethGetInt2 = np.loadtxt('./benchmarks/ethereum/getInt2.txt', delimiter="|", unpack=True)
ethGetInt5 = np.loadtxt('./benchmarks/ethereum/getInt5.txt', delimiter="|", unpack=True)
ethGetInt10 = np.loadtxt('./benchmarks/ethereum/getInt10.txt', delimiter="|", unpack=True)
fig, axes = plt.subplots(nrows=3, ncols=4)
ax0, ax1, ax2, ax3, ax10, ax11, ax12, ax13, ax20, ax21, ax22, ax23 = axes.flatten()
ax0.hist(ethGetBool1[0],
         bins=100,
         histtype='bar')
ax0.axvline(ethGetBool1[0].mean(), color='k', linestyle='dashed', linewidth=1)
ax0.set_title('Average: ' + str(ethGetBool1[0].mean()) + '\nOverall: ' + str(ethGetBool1[0].size))

ax1.hist(ethGetBool2[0],
         bins=100,
         histtype='bar')
ax1.axvline(ethGetBool2[0].mean(), color='k', linestyle='dashed', linewidth=1)
ax1.set_title('Average: ' + str(ethGetBool2[0].mean()) + '\nOverall: ' + str(ethGetBool2[0].size))

ax2.hist(ethGetBool5[0],
         bins=100,
         histtype='bar')
ax2.axvline(ethGetBool5[0].mean(), color='k', linestyle='dashed', linewidth=1)
ax2.set_title('Average: ' + str(ethGetBool5[0].mean()) + '\nOverall: ' + str(ethGetBool5[0].size))

ax3.hist(ethGetBool10[0],
         bins=100,
         histtype='bar')
ax3.axvline(ethGetBool10[0].mean(), color='k', linestyle='dashed', linewidth=1)
ax3.set_title('Average: ' + str(ethGetBool10[0].mean()) + '\nOverall: ' + str(ethGetBool10[0].size))

ax10.axvline(ethGetInt1[0].mean(), color='k', linestyle='dashed', linewidth=1)
ax10.set_title('Average: ' + str(ethGetInt1[0].mean()) + '\nOverall: ' + str(ethGetInt1[0].size))

ax11.hist(ethGetInt2[0],
         bins=100,
         histtype='bar')
ax11.axvline(ethGetInt2[0].mean(), color='k', linestyle='dashed', linewidth=1)
ax11.set_title('Average: ' + str(ethGetInt2[0].mean()) + '\nOverall: ' + str(ethGetInt2[0].size))

ax12.hist(ethGetInt5[0],
         bins=100,
         histtype='bar')
ax12.axvline(ethGetInt5[0].mean(), color='k', linestyle='dashed', linewidth=1)
ax12.set_title('Average: ' + str(ethGetInt5[0].mean()) + '\nOverall: ' + str(ethGetInt5[0].size))

ax13.hist(ethGetInt10[0],
         bins=100,
         histtype='bar')
ax13.axvline(ethGetInt10[0].mean(), color='k', linestyle='dashed', linewidth=1)
ax13.set_title('Average: ' + str(ethGetInt10[0].mean()) + '\nOverall: ' + str(ethGetInt10[0].size))

plt.show()