import csv
import sys

with open(sys.argv[1], 'rb') as csvfile:
    spamreader = csv.DictReader(csvfile, delimiter=',')
    
    # Build a global key map
    keys = spamreader.next().keys();
    # Output csv
    print(','.join(keys));
    # Output padded values
    old_row = None;
    for row in spamreader:
        window_string = ""
        if(old_row is None):
            old_row = row;
        for key in row:
            if row[key] == "NO-DATA":
                row[key] = old_row[key];
            window_string += row[key] + ","
        old_row = row;
        print(window_string)
