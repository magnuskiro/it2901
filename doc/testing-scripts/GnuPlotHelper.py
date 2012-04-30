#!/usr/bin/env python

from subprocess import Popen, PIPE

def prepare_gnu_plot(plot, boxwidth=1):
	plot.stdin.write('set terminal svg\n')
	plot.stdin.write('set xtics ("Node 2" 0, "Node 3" 2, "Node 4" 4) \n')
	plot.stdin.write('set boxwidth {0}\n'.format(boxwidth))
	plot.stdin.write('set xrange[-1:5]\n'.format(boxwidth))
	return plot

def plote_file():
	plot = Popen(['gnuplot'], stdin=PIPE)
	plot = prepare_gnu_plot(plot)
	return plot
	
def plot_time_graph(plot_name, file_name):
	plot = plote_file()
	plot.stdin.write('set title "{0}"\n'.format(plot_name))
	plot.stdin.write('set output "{0}"\n'.format(plot_name.replace(' ', '_') + '_time' + '.svg'))
	plot.stdin.write('set yrange [0:]\n')
	plot.stdin.write('set ylabel "Average sending time(ms)"\n')
	plot.stdin.write('''plot "{0}" using 1:2:3 notitle with boxerrorbars, "{0}" using 4:5:6 notitle with boxerrorbars, "{0}" using 7:8:9 notitle with boxerrorbars\n'''.format(file_name))
	plot.stdin.write('exit\n')
	
def plot_message_graph(plot_name, file_name):
	plot = plote_file()
	plot.stdin.write('set title "{0}"\n'.format(plot_name))
	plot.stdin.write('set yrange [0.0:1.0]\n')
	plot.stdin.write('set output "{0}"\n'.format(plot_name.replace(' ', '_') + '_messages' + '.svg'))
	plot.stdin.write('set ylabel "Number of messages successfully received"\n')
	plot.stdin.write('''plot "{0}" using 1:2 notitle with boxes, "{0}" using 3:4 notitle with boxes, "{0}" using 5:6 notitle with boxes\n'''.format(file_name))
	plot.stdin.write('exit\n')
