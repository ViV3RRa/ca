import csv
import math
import sys

with open(sys.argv[1], 'rb') as csvfile:
	spamreader = csv.DictReader(csvfile, delimiter=',')
	#eNormList = []
	soundList = []
	#timeList = []
	statusList = []
	for row in spamreader:
		soundL = float(row['soundLevel'])
		#time = float(row['timestamp'])
		status = row['status']
		soundList.append(soundL)
		#timeList.append(time)
		statusList.append(status)
		#eNorm = math.sqrt(x*x+y*y+z*z)
		##eNormList.append(eNorm)
		#print(x, y, z, eNorm)

	windowsSo = []
	#windowsT = []
	windowsSt = []
	while len(soundList) >= 500:
		windowSo = []
		#windowT = []
		windowSt = []
		#windowsT.append(timeList[0])
		windowsSt.append(statusList[0])
		for i in range(0, 250):
			sound = soundList.pop(0)
			#time = timeList.pop(0)
			status = statusList.pop(0)
			windowSo.append(sound)
			#windowT.append(time)
			#windowSt.append(status)
		for i in range(0, 250):
			#eNorm = eNormList[i]
			sound = soundList[i]
			#time = timeList[i]
			#status = statusList[i]
			windowSo.append(sound)
			#windowT.append(time)
			#windowSt.append(status)
		windowsSo.append(windowSo)
		#windowsT.append(windowT)
		#windowsSt.append(windowSt)

	print 'status,min,max,avg,stdVar'
	num = 0
	for w in windowsSo:
		status = windowsSt[num]
		#time = windowsT[num]
		minVV = min(w)
		maxVV = max(w)
		avgVV = sum(w)/float(len(w)) 
		
		sumVV = 0

		for i in windowSo:
			sumVV += math.pow(avgVV-i, 2)
		
		sumDiv = sumVV/500
		stdVarVV = math.sqrt(sumDiv)
		print status, ',', minVV, ',', maxVV, ',', avgVV, ',', stdVarVV
		#print "Hello"
		num += 1


	