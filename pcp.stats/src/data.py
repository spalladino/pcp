import pickle
import os

def ensuredir(dir):
	if not os.path.isdir(dir): 
		os.mkdir(dir)

def getobject(path, createfunc):

	try:
		input = open(path, 'rb')
		obj = pickle.load(input)
		input.close()
		print ' Successfully loaded'
		return obj
	
	except:
		obj = createfunc()
		
		try:
			output = open(path, 'wb')
			pickle.dump(obj, output, -1)
			output.close()
			print ' Successfully created and stored'
		except:
			' Created but not stored', path
			pass
		
		return obj