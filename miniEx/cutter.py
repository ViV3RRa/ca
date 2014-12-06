import csv
import sys

with open(sys.argv[1], 'rb') as csvfile:
		spamreader = csv.DictReader(csvfile, delimiter=',')
		spamList = list(spamreader)
		length = len(spamList)
		print 'motionUserAccelerationX,motionUserAccelerationY,motionUserAccelerationZ'
		for i in range(length/10, length-length/10):
			x = float(spamList[i]['motionUserAccelerationX'])
			y = float(spamList[i]['motionUserAccelerationY'])
			z = float(spamList[i]['motionUserAccelerationZ'])
			print x, ',', y, ',', z