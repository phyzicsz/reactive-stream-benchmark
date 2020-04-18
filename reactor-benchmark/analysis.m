graphics_toolkit('fltk')

#open the file
fileName = 'reactor-benchmark.txt'
data = importdata(fileName);

#convert to microseconds
diffs = diff(data)/1000;

# there are some wierd data points that make it hard to see down at the mean
# lets filter those out...
anomolies = find(diffs > 200)
diffs(anomolies) = [];

#plot the diffs
figure
plot(diffs)

#plot the histogram
figure
hist(diffs,0:.03:2);




