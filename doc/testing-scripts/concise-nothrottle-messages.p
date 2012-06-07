set terminal postscript eps monochrome dashed lw 2
set title "Successful messages without Throttle mediator"
set xlabel "Bandwidth"
set ylabel "Successful messages received back"
set xtics ("5kBps" 1, "10kBps" 2, "20kBps" 3)
set xrange [0.5:4.0]
set yrange [0:100]
set format y "%g %%"
set output "concise-message-graph-nothrottle.eps"
plot "concise-nothrottle.dat" using 1:($8*100) title 'Node 2' with lines, \
	"concise-nothrottle.dat" using 1:($9*100) title 'Node 3' with lines, \
	"concise-nothrottle.dat" using 1:($10*100) title 'Node 4' with lines