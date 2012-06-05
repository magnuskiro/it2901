set terminal postscript monochrome
set title "Successfull messages compared with Timeout"
set xlabel "Timeout(ms)"
set ylabel "Successfull messages received back(%)"
set xtics ("500" 1, "1000" 2, "2000" 3, "5000" 4, "100000" 5)
set xrange [0:6]
set yrange [0.0:1.0]
set output "concise-message-graph.ps"
plot "concise-message.dat" using 1:2 title '5kBps-node2' with lines, \
	"concise-message.dat" using 1:3 title '5kBps-node3' with lines, \
	"concise-message.dat" using 1:4 title '5kBps-node4' with lines, \
	"concise-message.dat" using 1:5 title '10kBps-node2' with lines, \
	"concise-message.dat" using 1:6 title '10kBps-node3' with lines, \
	"concise-message.dat" using 1:7 title '10kBps-node4' with lines, \
	"concise-message.dat" using 1:8 title '20kBps-node2' with lines, \
	"concise-message.dat" using 1:9 title '20kBps-node3' with lines, \
	"concise-message.dat" using 1:10 title '20kBps-node4' with lines