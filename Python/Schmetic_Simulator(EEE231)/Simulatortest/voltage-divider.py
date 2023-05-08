#! /usr/bin/env python3


#r# This example shows the computation of the DC bias and sensitivity in a voltage divider.

####################################################################################################

import PySpice.Logging.Logging as Logging
logger = Logging.setup_logging()

####################################################################################################

from PySpice.Spice.Netlist import Circuit
from PySpice.Unit import *

####################################################################################################

#f# circuit_macros('voltage-divider.m4')

circuit = Circuit('Simulator')


#if unit == 'u_A':
#    circuit.I('NODEID',circuit.gnd,"""P_NODE_ID""",""" value """@unit)
#elif unit == 'u_V':
#    circuit.V('NODEID',"""N_NODE_ID""","""P_NODE_ID""",""" value """@unit)
#elif unit == 'u_Ohm':
#    circuit.R('ARCID', """P_NODE_ID""", """N_NODE_ID""","""value"""+'@u_Ohm')

circuit.I('current',circuit.gnd,'c',1@u_A)
circuit.V('input', 'in', 'c', 10@u_V)
circuit.R(1, 'in', 'out', 9@u_kOhm)
circuit.R(2, 'out', circuit.gnd, 1@u_kOhm)

####################################################################################################

simulator = circuit.simulator()

analysis = simulator.operating_point()

# print(float(analysis.branches['R1']))

for branch in analysis.branches.values():
    print('Node {}: {} A'.format(str(branch), float(branch)))

for node in analysis.nodes.values():
    print('Node {}: {} V'.format(str(node), float(node)))
# for node in (analysis['in'], analysis.out): # .in is invalid !
#     print('Node {}: {} V'.format(str(node), float(node)))
#o#
