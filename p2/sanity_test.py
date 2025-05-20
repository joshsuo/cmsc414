#!/usr/bin/python3

# University of Maryland, CMSC414 

# For determining whether or not the submission
# contains all of the required files

import re
import sys

kv_pair = re.compile('^([a-z0-9_]+)="(.*)"\s*$')
'''Used for getting the student's solutions as a key value pair from the text file'''
def get_symbols(sol, required_symbols):
	ret = dict()
	try:
		fil = open(sol, "r")
	except FileNotFoundError:
		if (sol != "task4.txt"): # optional
			print("FAIL: The required file \"{}\" is not present.".format(sol))
		else:
			print("task 4 - no comment provided (optional)")
		return False
	lines = fil.readlines()

	if (sol == "task1.txt"):
		try:
			username = lines[0].strip("\n")
		except:
			print("ERROR: first line failure, username problem")
		try:
			password = lines[1].strip("\n")
		except:
			print("ERROR: second line failure, password problem")
		print("task 1 - username provided: " + "==>" + username + "<==")
		print("task 1 - password provided: " + "==>" + password + "<==")

	if (sol == "task2.txt"):
		try:
			old_password = lines[0].strip("\n")
		except:
			print("ERROR: first line failure, old password problem")
		try:
			new_password = lines[1].strip("\n")
		except:
			print("ERROR: second line failure, new password problem")
		try:
			confirm_password = lines[2].strip("\n")
		except:
			print("ERROR: third line failure, confirm password problem")
		print("task 2 - old password: " + "==>" + old_password + "<==")
		print("task 2 - new password: " + "==>" + new_password + "<==")
		print("task 2 - confirm password: " + "==>" + confirm_password + "<==")

	if (sol == "task3.txt"):
		try:
			url = lines[0].strip("\n")
		except:
			print("ERROR: first line failure, url problem")
		print("task 3 - url provided: " + "==>" + url + "<==")

	if (sol == "task4.txt"):
		try:
			code = lines[0].strip("\n")
		except:
			print("ERROR: first line failure, code problem")
		print("task 4 - code provided: " + "==>" + code + "<==")

	if (sol == "task5.py"):
		try:
			python_file_sample = ' '.join([line for line in lines])
		except:
			print("ERROR: python file sample problem")
		print("task 5 - python file provided sample: " + "==>" + python_file_sample[0:15] + "..." + "<==")

	if (sol == "task6.html"):
		try:
			html_file_sample = ' '.join([line for line in lines])
		except:
			print("ERROR: html file problem")
		print("task 6 - html file provided sample: " + "==>" + html_file_sample[0:15] + "..." + "<==")

	if (sol == "readme.txt"):
		try:
			readme_sample = ' '.join([line for line in lines])
		except:
			print("ERROR: readme.txt file problem")
		print("readme.txt - file provided sample: " + "==>" + readme_sample[0:8] + "..." + "<==")

	fil.close()
	return True



requirements_list = [
	("task1.txt", ['username', 'password']), #1
	("task2.txt", ['search', 'link']), #2
	("task3.txt", ['new_password']), #3
	("task4.txt", ['url']), #4
	("task5.py",[]), #5
	("task6.html", []), #6
	("readme.txt", []), # the write up
]

if __name__ == "__main__":
	tasks_count = 0

	# Test each requirement
	for rq in requirements_list:
		compliant = get_symbols(rq[0], rq[1])
		if (compliant):
			tasks_count += 1

	print("\n")

	# Inform user of result
	if tasks_count == len(requirements_list):
		print("SUCCESS: Nothing seems to suggest that your submission is incomplete\n")
		print("Be sure to include plain-english explanations where required")
		print("This script will not verify the correctness of your solution")
		exit(0)
	else:
		print("WARNING - Your submission is incomplete for the reasons mentioned above.")
		exit(1)
