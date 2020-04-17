graphics_toolkit('fltk')
data = importdata('reactor-benchmark.txt');
diffs = diff(data)/1000;

x = 1:999999;

#there are some wierd values...
#filter them out...  (not that many (<20))
excludes = find(diffs > 200);
diffs(excludes) = [];

avg = mean(diffs);

hold on 
stem(diffs);
plot(mean(diffs));
