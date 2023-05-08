# import csv file, and plot them
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd

N=100

df = pd.read_csv(r'scope_export_1.csv')
df.apply(pd.to_numeric, downcast='float')

second=df.second
Volt=df.Volt

print(second)
print(Volt)

plt.subplot(2,1,1)
plt.plot(second,Volt)
plt.xlabel("time (s)")
plt.ylabel("Volt (V)")

plt.subplot(2,1,2)
fft=np.fft.rfft(Volt,N)
fft_fre=np.fft.rfftfreq(N,1/N)
plt.plot(fft_fre,np.abs(fft))

plt.show()