# NOTE: the default pyvisa import works well for Python 3.6+
# if you are working with python version lower than 3.6, use 'import visa' instead of import pyvisa as visa

import pyvisa as visa
import time
# start of Untitled

rm = visa.ResourceManager()
DSO_X_2002A = rm.open_resource('USB0::0x0957::0x179B::MY55462402::0::INSTR')
DSO_X_2002A.write(':AUToscale')
DSO_X_2002A.close()
rm.close()

# end of Untitled
