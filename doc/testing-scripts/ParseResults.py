#!/usr/bin/env python
import argparse
import re
from os import listdir, path, walk
from math import sqrt

from GnuPlotHelper import plot_time_graph, plot_message_graph

def add_arguments(parser):
	parser.add_argument('folder', metavar='Folder', type=str, help='the System Test result folder to parse from')
	parser.add_argument('nodes', metavar='Nodes', type=int, nargs='+', help='the nodes to include when parsing')
	parser.add_argument('-m', '--messages', action='store_true', help='Generate a graph over the number of messages successfully received back for each client')
	parser.add_argument('-t' ,'--time', action='store_true',  help='Generate a graph over the average time each client waited for reply')
	parser.add_argument('-p' ,'--print', action='store_true',  help='Print results to terminal')
	parser.add_argument('-g' ,'--graph', metavar='name_for_graph', type=str,  help='Create graphs of the results')
	parser.add_argument('-c', '--core', action='store_true', help='Print info about core')
	
def get_files(folder, nodes):
	result = {}
	node_logs = re.compile(r"node[{0}].log".format(''.join(str(i) for i in nodes)))
	for root, dirs, files in walk(folder):
		for fil in files:
			if node_logs.match(fil):
				f = str(fil)
				if f in result:
					result[f].append(path.abspath(root + '/' + fil))
				else:
					result[f] = [path.abspath(root + '/' + fil)]
	return result
	
def create_message_graph(files, graph_name, prints=False, graph=False):
	file_name = 'message_data.dat'
	total_messages = re.compile(r"(?<=nofreqs set to:)[ 0-9]+")
	actual_messages = re.compile(r"INFO: [0-9]+ms: Got response for [0-9]+")
	mess = {}
	for key in files.keys():
		total = 0
		actual = 0
		act_sq = 0
		for fil in files[key]:
			with open(fil, 'r') as r:
				content = r.read()
				h = actual_messages.findall(content)
				total += float(total_messages.search(content).group(0))
				actual += len(h)
				act_sq += len(h)**2
		actual /= total		
		act_sq /= total
		act_sq = sqrt(act_sq)
		mess[key] = (actual, act_sq, act_sq - actual)
	if prints:
		for key in sorted(mess.keys()):
			print 'Percentage of messages received for', key, ':', mess[key][0]
	if graph:
		with open(file_name, 'w') as d:
			for i, key in enumerate(sorted(mess.keys())):
				d.write(str(i*2) + '\t' + str(mess[key][0]) + '\t')
			d.write('\n')
		try:
			plot_message_graph(graph_name, file_name)
		except OSError:
			print 'Could not plot graph.\nEnsure that you have Gnuplot installed and rerun this program'
	return mess

def create_time_graph(files, graph_name, prints=False, graph=False):
	file_name = 'time_data.dat'
	times_pattern = re.compile(r"(?<=INFO: )[0-9]+(?=ms: Got response for)")
	result = {}
	for key in files.keys():
		total = 0
		sum1 = 0
		sum_sq = 0
		for fil in files[key]:
			with open(fil, 'r') as r:
				content = r.read()
				t = times_pattern.findall(content)
				for n in t:
					sum1 += int(n)
					sum_sq += int(n)**2
				total += len(t)
		if total == 0:
			sum1 = 1
			sum_sq = 1
		else:
			sum1 /= total
			sum_sq /= total
		sum_sq = sqrt(sum_sq)
		dev = sum_sq - sum1
		result[key] = (sum1, sum_sq, dev)
	if prints:
		for key in sorted(result.keys()):
			print 'Average time for received messages(in ms) for ', key, ':', result[key][0], '\tdeviation:', int(result[key][2]),'({0}%)'.format(int((result[key][2]/result[key][0])*100))
	if graph:
		with open(file_name, 'w') as d:
			for i, key in enumerate(sorted(result.keys())):
				d.write(str(i*2) + '\t' + str(result[key][0]) + '\t' + str(result[key][2]) + '\t')
			d.write('\n')
		try:
			plot_time_graph(graph_name, file_name)
		except OSError:
			print 'Could not plot graph.\nEnsure that you have Gnuplot installed and rerun this program'
	return result

if __name__ == '__main__':
	parser = argparse.ArgumentParser(description='Parse result from a System Test and generate graphs')
	add_arguments(parser)
	args = vars(parser.parse_args())
	files = get_files(args['folder'], args['nodes'])
	if args['core']:
		core = open(args['folder']+'core.log')
		print ""
		print core.readline(),
	#print files
	if(args['messages']):
		create_message_graph(files, args['graph'], args['print'], len(args['graph']) != 0)
	if(args['time']):
		create_time_graph(files, args['graph'], args['print'], len(args['graph']) != 0)
