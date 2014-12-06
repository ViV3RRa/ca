import csv
import re

window_width = 100;

with open('log.csv', 'rb') as csvfile:
    spamreader = csv.DictReader(csvfile, delimiter='|')
    windows = [];
    cur_window = {};
    cur_time = 0;
    for row in spamreader:
        timestamp = int(row['timestamp']);
        if(timestamp > cur_time + window_width):
            cur_time = timestamp;
            windows.append(cur_window);
            cur_window = {};
            #print("new window");
        else:
            sensor = row['sensorName'];
            #print(sensor);
            if((sensor in cur_window) is False):
                cur_window[sensor] = [];
            sensor_window = cur_window[sensor];
            #print(sensor_window);
            sensor_window.append(row);

    def average_vector_value(dataset):
        xChannel = 0;
        yChannel = 0;
        zChannel = 0;
        for reading in dataset:
            # Read out the raw string
            value_str = reading['value'];
            # Remove '[' and ']'
            value_str_trim = value_str[1:-1];
            # Split the string, into 3 substrings on ','
            # We now have one string for each of the coords
            value_str_split = value_str_trim.split(',');
            # Map each string into a float
            x,y,z = map(float, value_str_split);
            # Append these to our WindowGyro'X'
            xChannel += x;
            yChannel += y;
            zChannel += z;
        # Get the total number of readings
        num_readings = len(dataset);
        # Find the average of all readings
        xChannel /= num_readings;
        yChannel /= num_readings;
        zChannel /= num_readings;
        return xChannel, yChannel, zChannel

    def mapper(keyX, keyY, keyZ, output_map, dataset):
        valueX, valueY, valueZ = average_vector_value(dataset);
        if(keyX is not None): output_map[keyX] = valueX
        if(keyY is not None): output_map[keyY] = valueY
        if(keyZ is not None): output_map[keyZ] = valueZ
        return output_map

    def lat_lon(dataset):
        longitude = 0;
        latitude  = 0;
        for reading in dataset:
            # Get the value string
            value_str = reading['value'];
            # Regex match the Longitude and Latitude
            lon = re.search('(?<="mLongitude":)\d*(\.\d*)?', value_str).group(0);
            lat = re.search('(?<="mLatitude":)\d*(\.\d*)?', value_str).group(0);
            # Add em to the global one
            longitude += float(lon);
            latitude  += float(lat);
            #print(lon + " " + lat);
        # Get the total number of readings
        num_readings = len(dataset);
        # Find the average of all readings
        longitude /= num_readings;
        latitude  /= num_readings;
        return longitude, latitude

    def lat_lon_mapper(output_map, dataset):
        # TODO: Read out more from GPS
        lon, lat = lat_lon(dataset);
        output_map['Lon'] = lon;
        output_map['Lat'] = lat;
        return output_map;
             
    # Translate from symbolic name, to data handler
    handler_map = {
        'Gyro'   : lambda output_map, dataset: mapper('GyroX', 'GyroY', 'GyroZ', output_map, dataset),
        'Acc'    : lambda output_map, dataset: mapper('AccX', 'AccY', 'AccZ', output_map, dataset),
        'Light'  : lambda output_map, dataset: mapper('Light', None, None, output_map, dataset),
        'Magnet' : lambda output_map, dataset: mapper('MagX', 'MagY', 'MagZ', output_map, dataset),
        'Orient' : lambda output_map, dataset: mapper('OrientX', 'OrientY', 'OrientZ', output_map, dataset),
        'GPS'    : lambda output_map, dataset: lat_lon_mapper(output_map, dataset),
        'WiFi'   : lambda output_map, x: None
    };

    # Translate from hardware sensor name, to symbolic name
    translate_map = {
        'BOSCH BMA250 3-axis Accelerometer' : 'Acc',
        'CM36282 Light sensor' : 'Light',
        'AK8963 3-axis Magnetic field sensor' : 'Magnet',
        'R3GD20 Gyroscope sensor' : 'Gyro',
        'ST 9-axis Orientation Sensor' : 'Orient',
        'GPS' : 'GPS',
        'WiFi' : 'WiFi'
    };

    # Assert that each output of the translate map is contained in the handler map
    for key in translate_map:
        value = translate_map[key];
        assert(value in handler_map);

    # Call the map handlers for everything
    window_maps = []
    for window in windows:
        window_map = {}
        for key in window:
            # Assert that we map everything
            assert(key in translate_map);
            # Lookup the handler function
            handler = handler_map[translate_map[key]];
            # Call the handler
            handler(window_map, window[key]);
        window_maps.append(window_map);

    # Build a global key map
    keys = [];
    for window_map in window_maps:
        for key in window_map:
            if((key in keys) is False):
                keys.append(key)
    
    # Output csv
    print(','.join(keys));
    for window_map in window_maps:
        window_string = ""
        for key in keys:
            if(key in window_map):
                window_string += str(window_map[key]);
            else:
                window_string += "NO-DATA"
            window_string += ","
        print(window_string);
