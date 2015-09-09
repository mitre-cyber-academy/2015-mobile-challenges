import os, json, threading, math, string, random, httpagentparser
import sys, getopt
from flask import Flask, abort, request, jsonify
from sched import scheduler
from time import time, sleep


app = Flask(__name__)
s = scheduler(time, sleep)

class Game(object):
    def __init__(self, masterPasscode, masterOrder, secret, port):
        self.passcode = masterPasscode
        self.order = masterOrder
        self.flag = secret
        self.port = port

g = Game('REHS', '3210', 'FLAG', '5000')

def setFlag(argv):
    global g
    try:
        opts, args = getopt.getopt(argv,"hc:o:f:p:",["passcode=","order=","flag=","port="])
    except getopt.GetoptError:
        print 'sherlocked_backend.py -c <passcode> -o <order> -f <flag>'
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h':
            print 'sherlocked_backend.py -c <passcode> -o <order> -f <flag>'
            sys.exit()
        elif opt in ("-c", "--passcode"):
            g.passcode = arg
        elif opt in ("-o", "--order"):
            g.order = arg
        elif opt in ("-f", "--flag"):
            g.flag = arg
        elif opt in ("-p", "--port"):
            g.port = arg
    print g.passcode
    print g.order
    print g.flag
    print g.port

@app.route('/passcode', methods=['GET'])
def get_passcode ():
    global g
#    if request.method == 'GET':
    return g.passcode

@app.route('/order', methods=['GET'])
def get_order ():
    global g
  #  if request.method == 'GET':
    return g.order

@app.route('/challenge', methods=['POST'])
def challenge_question ():
    global g
    loc_passcode = request.form['passcode']
    print(request.form['passcode'])
    loc_order = request.form['order']
    print(request.form['order'])
    #if request.method == 'POST':
    if loc_order == g.order:
        if loc_passcode == g.passcode:
            return jsonify({'flag': g.flag})
    else:
        return jsonify({'flag': 'You didnt say the magic word','loc_passcode':loc_passcode,'loc_order':loc_order, 'passcode':passcode, 'order':order})

if __name__ == '__main__':
    setFlag(sys.argv[1:])
    app.run(host='0.0.0.0', debug=True, port=5000)
