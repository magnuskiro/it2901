set terminal postscript monochrome
set title "Successfull messages without Throttle mediator"
set xlabel "Bandwidth"
set ylabel "Successfull messages received back(%)"
set xtics ("5kBps" 1, "10kBps" 2, "20kBps" 3)
set xrange [0.5:3.5]
set yrange [0.0:1.0]
set output "concise-message-graph-nothrottle.ps"
plot "concise-nothrottle.dat" using 1:8 title 'Node 2' with lines, \
	"concise-nothrottle.dat" using 1:9 title 'Node 3' with lines, \
	"concise-nothrottle.dat" using 1:10 title 'Node 4' with lines