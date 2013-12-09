import MySQLdb
from os import listdir
import re
from datetime import datetime
import ConfigParser
import sys
from optparse import OptionParser
import time

config = ConfigParser.ConfigParser()
config.readfp(open("../db.conf"))

db = MySQLdb.connect(
		host = config.get("db", "host"),
		user = config.get("db", "user"),
		passwd = config.get("db", "password"),
		db = config.get("db", "database")
)

cur = db.cursor()

opt_parser = OptionParser()
opt_parser.add_option("-w", "--watch", action="store_true", dest="watch")
(options, blog_file_names) = opt_parser.parse_args()

if len(blog_file_names) is 0:
	blog_file_names = [f for f in listdir(".") if re.match("^(.*?).blarg$", f)]

def populate():
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
							# try with hour first
							try:
								date = datetime.strptime(val, "%m-%d-%Y %I:%M%p")

							# then without hour
							except ValueError:
								date = datetime.strptime(val, "%m-%d-%Y")
				else:
					content += line

		url = title
		url = re.sub(" ", "-", url)
		url = re.sub("[^\w\-]", "", url)
		title = db.escape_string(title)
		content = db.escape_string(content)
		date = date.strftime("%Y-%m-%d %H:%M:%S")

		cur.execute("replace into blogs set url=\"%s\", title=\"%s\", date=\"%s\", content=\"%s\"" %
				(url, title, date, content))

	db.commit()

if options.watch:
	quit = False
	while not quit:
		try:
			populate()
			time.sleep(1)
		except KeyboardInterrupt:
			quit = True
		except:
			pass
else:
	populate()
