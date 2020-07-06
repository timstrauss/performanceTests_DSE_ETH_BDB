import numpy as np
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

technology = "dse"
technologyName = "DSE"
direc = "sameResource/"

def getMean95And99(array):
    array = [array[0], array[2]]
    quantization = np.digitize(array, np.arange(0,800000000,60000000))[1]
    array = np.concatenate((array, [quantization]))
    array = [array[0],array[2]]
    array = np.transpose(array)
    array = array[array[:,1].argsort()]
    x = np.unique(array[:, 1], return_counts=True)[0]
    array = np.split(array[:, 0], np.cumsum(np.unique(array[:, 1], return_counts=True)[1])[:-1])
    array = [np.sort(arr) for arr in array]
    return x, [np.mean(arr) for arr in array], [arr[int((len(arr) / 10) * 9.5)] for arr in array],[arr[int((len(arr) / 10) * 9.9)] for arr in array]

fig, axes = plt.subplots(3, 4)

method = "getBool"
methodDescription = "Get bool"

thread8 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '8.txt', delimiter="|", unpack=True)
thread4 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '4.txt', delimiter="|", unpack=True)
thread2 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '2.txt', delimiter="|", unpack=True)
thread1 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '1.txt', delimiter="|", unpack=True)

plt.subplot(4, 3, 1)
x, mean, up95, up99 = getMean95And99(thread1)
plt.title(technologyName + ": " + methodDescription + " scatter with one thread")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

plt.subplot(4, 3, 4)
x, mean, up95, up99 = getMean95And99(thread2)
plt.title(technologyName + ": " + methodDescription + " scatter with two threads")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

plt.subplot(4, 3, 7)
x, mean, up95, up99 = getMean95And99(thread4)
plt.title(technologyName + ": " + methodDescription + " scatter with four threads")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

plt.subplot(4, 3, 10)
x, mean, up95, up99 = getMean95And99(thread8)
plt.title(technologyName + ": " + methodDescription + " scatter with eight threads")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

method = "setBool"
methodDescription = "Set bool"

thread8 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '8.txt', delimiter="|", unpack=True)
thread4 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '4.txt', delimiter="|", unpack=True)
thread2 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '2.txt', delimiter="|", unpack=True)
thread1 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '1.txt', delimiter="|", unpack=True)

plt.subplot(4, 3, 2)
x, mean, up95, up99 = getMean95And99(thread1)
plt.title(technologyName + ": " + methodDescription + " scatter with one thread")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

plt.subplot(4, 3, 5)
x, mean, up95, up99 = getMean95And99(thread2)
plt.title(technologyName + ": " + methodDescription + " scatter with two threads")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

plt.subplot(4, 3, 8)
x, mean, up95, up99 = getMean95And99(thread4)
plt.title(technologyName + ": " + methodDescription + " scatter with four threads")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

plt.subplot(4, 3, 11)
x, mean, up95, up99 = getMean95And99(thread8)
plt.title(technologyName + ": " + methodDescription + " scatter with eight threads")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

method = "addArray"
methodDescription = "Add item to array"

thread8 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '8.txt', delimiter="|", unpack=True)
thread4 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '4.txt', delimiter="|", unpack=True)
thread2 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '2.txt', delimiter="|", unpack=True)
thread1 = np.loadtxt('./benchmarks/' + direc + technology + '/' + method + '1.txt', delimiter="|", unpack=True)

plt.subplot(4, 3, 3)
x, mean, up95, up99 = getMean95And99(thread1)
plt.title(technologyName + ": " + methodDescription + " scatter with one thread")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

plt.subplot(4, 3, 6)
x, mean, up95, up99 = getMean95And99(thread2)
plt.title(technologyName + ": " + methodDescription + " scatter with two threads")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

plt.subplot(4, 3, 9)
x, mean, up95, up99 = getMean95And99(thread4)
plt.title(technologyName + ": " + methodDescription + " scatter with four threads")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

plt.subplot(4, 3, 12)
x, mean, up95, up99 = getMean95And99(thread8)
plt.title(technologyName + ": " + methodDescription + " scatter with eight threads")
plt.plot(x, mean)
plt.fill_between(x, mean, up95, alpha=0.2)
plt.fill_between(x, mean, up99, alpha=0.5)
maxVal = np.amax(up99)
minVal = np.amin(mean)
plt.ylim(0.8*minVal, maxVal*1.2)
plt.subplots_adjust(bottom=0.2)
plt.ylabel("Response time in microseconds")
plt.xlabel("Running test time in minutes")

fig.tight_layout(pad=1.0)

plt.show()
