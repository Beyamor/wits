#!/usr/bin/env python
import edn_format as edn
from os import listdir
import re
import ConfigParser
import MySQLdb

TITLE = edn.Keyword("title")

config = ConfigParser.ConfigParser()
config.readfp(open("../db.conf"))

db = MySQLdb.connect(
		host = config.get("db", "host"),
		user = config.get("db", "user"),
		passwd = config.get("db", "password"),
		db = config.get("db", "database")
)

cur = db.cursor()

cur.execute("delete from projects;")

ordering = []
with open("ordering", "r") as f:
	for line in f:
		ordering.append(line.strip())

def order(project):
	title = project[TITLE]
	if title in ordering:
		return ordering.index(title)
	else:
		return len(ordering)

file_names = [f for f in listdir(".") if re.match("^(.*?).clj$", f)]

for file_name in file_names:
	with open(file_name, "r") as f:
		print file_name
		content	= f.read()
		project	= edn.loads(content)

		url = project[TITLE].lower()
		url = re.sub(" ", "-", url)
		url = re.sub("[^\w\-]", "", url)

		cur.execute("insert into projects (url, ordering, data) values (%s, %s, %s)",
				(url, order(project), content))

db.commit()
