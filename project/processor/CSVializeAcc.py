import csv
import re
import sys

with open(sys.argv[1], 'rb') as csvfile:
    spamreader = csv.DictReader(csvfile, delimiter='|')
    print "AccX,AccY,AccZ"
    for row in spamreader:
        sensor = row['sensorName'];
        if(sensor == "BOSCH BMA250 3-axis Accelerometer"):
            # Read out the raw string
            value_str = row['value'];
            # Remove '[' and ']'
            value_str_trim = value_str[1:-1];
            # Split the string, into 3 substrings on ','
            # We now have one string for each of the coords
            value_str_split = value_str_trim.split(',');
            # Map each string into a float
            x,y,z = map(str, value_str_split);
            print x, ",", y, ",", z
