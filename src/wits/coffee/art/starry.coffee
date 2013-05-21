$ ->

	clamp		= arts.clamp
	hsvToRGB	= arts.hsvToRGB

	random = arts.random

	canvas = new arts.Canvas 'art'
	canvas.clearColor = 'white'
	canvas.clear()

	stars = []
	for i in [0...random.inIntRange(3, 6)]
		numberOfTries = 0
		maxNumberOfTries = 20
		margin	= 50

		nextStar = ->
			x	= random.inRange(margin, canvas.width - margin)
			y	= random.inRange(margin, canvas.height - margin)
			mass	= random.inRange(0.3, 0.6)
			return {x: x, y: y, mass: mass}

		tooClose = (star) ->
			for otherStar in stars
				dx	= star.x - otherStar.x
				dy	= star.y - otherStar.y
				d	= dx*dx + dy*dy
				return true if d < 200*200
			return false

		star = nextStar()
		while tooClose(star)
			++numberOfTries
			break if numberOfTries >= maxNumberOfTries
			star = nextStar()
		stars.push star

	brushStrokes = []
	for gridX in [0..canvas.width] by 10
		for gridY in [0..canvas.height] by 10
			x	= gridX + random.inRange(-5, 5)
			y	= gridY + random.inRange(-5, 5)
			dirX	= 0
			dirY	= 0
			maxIntensity	= 0

			for star in stars
				dx		= star.x - x
				dy		= star.y - y
				angle		= Math.atan2(dy, dx) + Math.PI/2
				dist		= Math.sqrt(dx * dx + dy * dy)

				if dist > 0
					distFactor	= Math.pow(100 / dist, 2)
					xInfluence	= Math.cos(angle) * distFactor
					yInfluence	= Math.sin(angle) * distFactor
					dirX		+= xInfluence
					dirY		+= yInfluence
					intensity	= Math.pow(Math.E, -Math.sqrt(dx*dx + dy*dy) * 0.022 / star.mass)
					maxIntensity	= intensity if intensity > maxIntensity

			dir = Math.atan2(dirY, dirX)

			brushStrokes.push {x: x, y: y, direction: dir, intensity: maxIntensity}

	brushStrokes = brushStrokes.sort (a, b) ->
		intensityFactor =\
			if a.intensity > b.intensity
				-1
			else if b.intensity > a.intensity
				1
			else
				0

		randomFactor = random.inRange -1, 1

		return intensityFactor * 0.5 + randomFactor * 0.5

	for brushStroke in brushStrokes
		width	= random.inRange 20, 40
		height	= random.inRange width * 0.25, width * 0.5

		color		= null
		colorCategory	= brushStroke.intensity * 0.75 + Math.random() * 0.25

		# Ordered by darkness
		#skyColors = [
		#	'#093154'
		#	'#0F79D6'
		#	'#56AFFC'
		#]

		#starColors = [
		#	'#FFFCCF'
		#	'#F7D748'
		#	'#DEC762'
		#]

		if colorCategory < 0.25
			h	= random.inRange(205, 215)
			v	= 0.05 + (Math.pow(clamp(brushStroke.intensity/0.001, 0, 1), 0.22) * 0.8 + Math.random() * 0.2) * 0.75
			s	= 1

			[r, g, b]	= hsvToRGB([h, s, v])
			color		= "rgb(#{r}, #{g}, #{b})"

		else
			h	= random.inRange(45, 55)
			s	= random.inRange(0.1, 0.8)
			v	= 0.8 + (clamp(brushStroke.intensity/0.9, 0, 1) * 0.5 + Math.random() * 0.5) * 0.2

			[r, g, b]	= hsvToRGB([h, s, v])
			color		= "rgb(#{r}, #{g}, #{b})"

		canvas.drawRect({
			x: brushStroke.x
			y: brushStroke.y
			width: width
			height: height
			rotation: brushStroke.direction
			color: color
			centered: true
		})
