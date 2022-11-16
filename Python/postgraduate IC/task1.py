# fft sin signal wave
import numpy as np
import matplotlib.pyplot as plt

frequency=100
N=100

plt.subplot(2,1,1)
x=np.linspace(0,1,N)
y=np.sin(x*2*np.pi) #y=sin(2*pi*x)
x=x/frequency

plt.plot(x,y)
plt.xlabel("time (s)")
plt.ylabel("Amplitude")
plt.grid()

plt.subplot(2,1,2)
fft=np.fft.rfft(y,N)
fft_fre=np.fft.rfftfreq(N,1/N)
plt.plot(fft_fre,np.abs(fft))
plt.grid()

#save the figure. must beyond plt.show()
#plt.savefig('line_plot.png')

plt.show()