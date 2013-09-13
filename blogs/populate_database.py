import MySQLdb
from os import listdir
import re
from datetime import datetime

config = {}
execfile("../db.conf", config)

db = MySQLdb.connect(
		host=config["host"],
		user=config["user"],
		passwd=config["password"],
		db=config["database"]
)

cur = db.cursor()

blog_file_names = [f for f in listdir(".") if re.match("^(.*?).blarg$", f)]
for blog_file_name in blog_file_names:
	reading_properties = True

	title = None
	date = None
	content = ""

	with open(blog_file_name, "r") as blog_file:
		for line in blog_file:
			if reading_properties:
				line = line.strip()
				if len(line) is 0:
					reading_properties = False
				else:
					(prop, val) = line.split(": ", 1)
					if prop == "title":
						title = val
					elif prop == "date":
						date = datetime.strptime(val, "%m-%d-%Y")
			else:
				content += line

	title = db.escape_string(title)
	content = db.escape_string(content)
	date = date.strftime("%Y-%m-%d")

	cur.execute("replace into blogs set title=\"%s\", date=\"%s\", content=\"%s\"" % (title, date, content))

db.commit()
