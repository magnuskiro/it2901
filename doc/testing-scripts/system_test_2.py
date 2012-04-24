#!/usr/bin/env python

from os import getenv
from subprocess import Popen
from time import sleep

node_id = int(getenv('node_id'))
path = '/home/qos/server/mobiemu/system-test-2/experiments/'
gfSleep = 60
wso2Sleep = 190
experimentSleep = 320

#Since node 2 is a "router" node we don't do anything there
if node_id == 1:
	#This is the ESB/GlashFish node
	#Start GlashFish:
	print 'Starting GlashFish'
	gf = Popen(['{0}glassfish/bin/./startserv'.format(path)])
	#Wait for glassfish to start
	sleep(gfSleep)
	#Start ESB
	print 'Starting WSO2'
	wso2 = Popen(['{0}esb/bin/./wso2server.sh'.format(path)])
	#Need to sleep to wait for experiment to finish
	sleep(wso2Sleep + experimentSleep)	
	print 'Done with experiment, terminating GlashFish and WSO2'
	wso2.kill()
	gf.terminate()
elif node_id == 2 or node_id == 3 or node_id == 4:
	#This is the client node
	#Need to wait for GF and WSO2 to start
	print 'Starting node 3, EchoClientClient'
	sleep(gfSleep + wso2Sleep)
	echo = Popen(['java', '-Djava.net.preferIPv4Stack=true', '-jar', '{0}EchoClient/EchoClientClient.jar'.format(path), '{0}client_{1}.config'.format(path, node_id - 1)])
	sleep(experimentSleep)
	#Check if echo has terminated, 
	#poll() returns None if it hasn't terminated
	echo.poll()
	if not echo.returncode:
		#Client has most likely crashed at this point
		#as such we need to kill it, softly with this song...
		echo.terminate()
