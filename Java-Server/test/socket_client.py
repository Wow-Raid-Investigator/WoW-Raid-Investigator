import socket

target_ip = "127.0.0.1"
target_port = 7777
buffer = 1024

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

s.connect((target_ip,target_port))

file = open("SampleData.log","r")

data = file.read(buffer)

while(data):
	s.send(data)
	data = file.read(buffer)

f.close()
s.close()