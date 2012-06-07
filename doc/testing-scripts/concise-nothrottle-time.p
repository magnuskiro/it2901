set terminal postscript eps monochrome dashed lw 2
set title "Average sending time without Throttle mediator"
set xlabel "Bandwidth"
set ylabel "Average sending time(ms)"
set xtics ("5kBps" 1, "10kBps" 2, "20kBps" 3)
set xrange [0.5:3.5]
set yrange [0:250000]
set output "concise-time-graph-nothrottle.eps"
plot "concise-nothrottle.dat" using 1:2:3 notitle with yerrorbars, \
	"concise-nothrottle.dat" using 1:2:3 title 'Node 2' with lines, \
	"concise-nothrottle.dat" using 1:4:5 notitle with yerrorbars, \
	"concise-nothrottle.dat" using 1:4:5 title 'Node 3' with lines, \
	"concise-nothrottle.dat" using 1:6:7 notitle with yerrorbars, \
	"concise-nothrottle.dat" using 1:6:7 title 'Node 4' with lines