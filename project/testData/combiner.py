import csv

files = ['cData.csv',
'lData.csv']


print 'min,max,avg,stdDev,status' 
for f in files:
	with open(f, 'rb') as csvfile:
		spamreader = csv.DictReader(csvfile, delimiter=',')
		for row in spamreader:
			print row['min'], ',', row['max'], ',', row['avg'], ',', row['stdVar'], ',', f