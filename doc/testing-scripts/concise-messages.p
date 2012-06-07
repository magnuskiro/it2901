set terminal postscript eps monochrome dashed lw 2
set title "Successful messages compared with Timeout"
set xlabel "Timeout(ms)"
set ylabel "Successful messages received back"
set xtics ("500" 1, "1000" 2, "2000" 3, "5000" 4, "100000" 5)
set xrange [0:7]
set yrange [0:100]
set format y "%g %%"
set output "concise-message-graph.eps"
plot "concise-message.dat" using 1:($2*100) title '5kBps-node2' with lines, \
	"concise-message.dat" using 1:($3*100) title '5kBps-node3' with lines, \
	"concise-message.dat" using 1:($4*100) title '5kBps-node4' with lines, \
	"concise-message.dat" using 1:($5*100) title '10kBps-node2' with lines, \
	"concise-message.dat" using 1:($6*100) title '10kBps-node3' with lines, \
	"concise-message.dat" using 1:($7*100) title '10kBps-node4' with lines, \
	"concise-message.dat" using 1:($8*100) title '20kBps-node2' with lines, \
	"concise-message.dat" using 1:($9*100) title '20kBps-node3' with lines, \
	"concise-message.dat" using 1:($10*100) title '20kBps-node4' with lines