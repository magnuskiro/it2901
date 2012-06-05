set terminal postscript monochrome
set title "Average sending time compared with Timeout"
set xlabel "Timeout(ms)"
set ylabel "Average sending time (ms)"
set xtics ("500" 1, "1000" 2, "2000" 3, "5000" 4, "100000" 5)
set xrange [0:6]
set output "concise-time-graph.ps"
plot "concise-time.dat" using 1:2:3 notitle with yerrorbars, \
	"concise-time.dat" using 1:2:3 title '5kBps-node2' with lines, \
	"concise-time.dat" using 1:4:5 notitle with yerrorbars, \
	"concise-time.dat" using 1:4:5 title '5kBps-node3' with lines, \
	"concise-time.dat" using 1:6:7 notitle with yerrorbars, \
	"concise-time.dat" using 1:6:7 title '5kBps-node4' with lines, \
	"concise-time.dat" using 1:8:9 notitle with yerrorbars, \
	"concise-time.dat" using 1:8:9 title '10kBps-node2' with lines, \
	"concise-time.dat" using 1:10:11 notitle with yerrorbars, \
	"concise-time.dat" using 1:10:11 title '10kBps-node3' with lines, \
	"concise-time.dat" using 1:12:13 notitle with yerrorbars, \
	"concise-time.dat" using 1:12:13 title '10kBps-node4' with lines, \
	"concise-time.dat" using 1:14:15 notitle with yerrorbars, \
	"concise-time.dat" using 1:14:15 title '20kBps-node2' with lines, \
	"concise-time.dat" using 1:16:17 notitle with yerrorbars, \
	"concise-time.dat" using 1:16:17 title '20kBps-node3' with lines, \
	"concise-time.dat" using 1:18:19 notitle with yerrorbars, \
	"concise-time.dat" using 1:18:19 title '20kBps-node4' with lines