import csv
import math
import sys

with open(sys.argv[1], 'rb') as csvfile:
	spamreader = csv.DictReader(csvfile, delimiter=',')
	eNormList = []
	for row in spamreader:
		x = float(row['motionUserAccelerationX'])
		y = float(row['motionUserAccelerationY'])
		z = float(row['motionUserAccelerationZ'])
		eNorm = math.sqrt(x*x+y*y+z*z)
		eNormList.append(eNorm)
		#print(x, y, z, eNorm)

	windows = []
	while len(eNormList) >= 128:
		window = []
		for i in range(0, 64):
			eNorm = eNormList.pop(0)
			window.append(eNorm)
		for i in range(0, 64):
			eNorm = eNormList[i]
			window.append(eNorm)
		windows.append(window)

	print 'min,max,avg,stdVar'
	for w in windows:
		minVV = min(w)
		maxVV = max(w)
		avgVV = sum(w)/float(len(w)) 
		
		sumVV = 0

		for i in window:
			sumVV += math.pow(avgVV-i, 2)
		
		sumDiv = sumVV/128
		stdVarVV = math.sqrt(sumDiv)
		print minVV, ',', maxVV, ',', avgVV, ',', stdVarVV


	