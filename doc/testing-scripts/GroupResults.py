#!/usr/bin/env python

import argparse
import re
from os import listdir, path, walk, mkdir, rmdir
from shutil import move

def add_arguments(parser):
	parser.add_argument('folder', metavar='Folder', type=str, help='the System Test result folder to move from')
	parser.add_argument('moveTo', metavar='MoveTo', type=str, help='the folder to move all the results in')
	parser.add_argument('-p' ,'--progress', action='store_true',  help='Show progress during move')
	parser.add_argument('--remove', action='store_true',  help='Remove old result folder')
	
def print_progress(prints, line):
	if prints:
		print line
	
def move_folders(folder, move_to, prints=False, remove=False):
	old_dirs = []
	print_progress(prints, 'Getting path of all folders to move from')
	for dirs in listdir(folder):
		old_dirs.append(path.abspath(folder + dirs))
	print_progress(prints, 'Retrieved path of all folders to move from')
	print_progress(prints, 'Found {0} folders'.format(len(old_dirs)))
	results = {}
	core_log_re = re.compile(r"(?<=datarate=)[0-9]+KBps")
	length = len(old_dirs)
	for i, old_dir in enumerate(old_dirs):
		current = old_dir.split('/')[-1]
		for root, dirs, files in walk(old_dir):
			for fil in files:
				if 'core.log' in fil:
					#print old_dir, fil
					f = path.abspath(root + '/' + fil)
					datarate = get_core_datarate(f)
					print_progress(prints, 'Found datarate: {0} in folder: {1},\t{2}%'.format(datarate, current, int(((i+1.0)/length)*100)))
					if datarate in results:
						results[datarate].append(old_dir)
					else:
						results[datarate] = [old_dir]
	for key in sorted(results.keys()):
		print_progress(prints, 'Folders for datarate {0}: {1}'.format(key, len(results[key])))
	create_dir(move_to)
	for key in sorted(results.keys()):
		m_t = path.abspath(move_to + str(key))
		create_dir(m_t)
		print_progress(prints, 'Created folder: {0}'.format(m_t))
		new_length = len(results[key])
		for i, folder in enumerate(results[key]):
			move(folder, m_t)
			print_progress(prints, 'Moving folder\t{0}%'.format(int((i+1.0)/new_length))*100)
	if remove:
		print_progress(prints, 'Removing original result folder')
		rmdir(folder)
	print 'Done'
	
						
def get_core_datarate(fil):
	core_log_re = re.compile(r"(?<=datarate=)[0-9]+KBps")
	with open(fil, 'r') as d:
		content = d.read()
		datarate = core_log_re.search(content)
		return datarate.group(0)
		
def create_dir(folder):
	try:
		mkdir(folder)
	except OSError:
		pass

if __name__ == '__main__':
	parser = argparse.ArgumentParser(description='Move results from a System Test result folder and group them')
	add_arguments(parser)
	args = vars(parser.parse_args())
	move_folders(args['folder'], args['moveTo'], args['progress'], args['remove'])
