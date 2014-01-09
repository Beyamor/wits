$ ->
	# shout out: http://metadataconsulting.blogspot.ca/2013/05/the-best-way-to-hide-your-email-from.html
	# and: http://stackoverflow.com/a/617685
	rot13 = (s) ->
		s.replace /[a-zA-Z]/g, (c) ->
			String.fromCharCode(
				if ((if c <= "Z" then 90 else 122) >= (c = c.charCodeAt(0) + 13))
					c
				else
					c - 26
			)

	email = rot13 "gntvofba@yvir.pn"

	$(".email").attr("href", "mailto:#{email}")
