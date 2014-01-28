$ ->
	Function::properties = (definitions) ->
		for prop, desc of definitions
			Object.defineProperty this.prototype, prop, desc

	Array::toString = ->
		s = "["
		for i in [0...@length]
			s += this[i].toString()
			s += ", " if i < @length - 1
		s += "]"
		return s

	clone = (obj) ->
		return obj  if obj is null or typeof (obj) isnt "object"
		temp = new obj.constructor()
		for key of obj
			temp[key] = clone(obj[key])
		temp

	random =
		inRange: (args...) ->
			if args.length is 1
				random.inRange 0, args[0]
			else
				args[0] + Math.random() * (args[1] - args[0])

		intInRange: (args...) ->
			if args.length is 1
				random.intInRange 0, args[0]
			else
				args[0] + Math.floor(Math.random() * (args[1] - args[0]))

		any: (coll) ->
			coll[random.intInRange(coll.length)]

		choose: (args...) ->
			@any args

		chance: (probability) ->
			Math.random() * 100 < probability
	Object.defineProperty random, "coinFlip", get: -> Math.random() < 0.5
	Object.defineProperty random, "plusMinus", get: -> if random.coinFlip then -1 else 1

	repeatString = (s, n) ->
		a = []
		while a.length < n
			a.push s
		return a.join ""

	lerp = (start, end, t) ->
		start + t * (end - start)

	colorString = ({r, g, b}) ->
		"rgb(#{r}, #{g}, #{b})"

	hsbToRGB = ({hue, saturation, brightness}) ->
		hue /= 360
		saturation /= 100
		brightness /= 100

		r = g = b = 0

		if saturation is 0
			r = g = b = brightness * 255 + 0.5
		else
			h = (hue - Math.floor(hue)) * 6
			f = h - Math.floor(h)
			p = brightness * (1 - saturation)
			q = brightness * (1 - saturation * f)
			t = brightness * (1 - (saturation * (1 - f)))

			switch Math.floor(h)
				when 0
					r = brightness * 255 + 0.5
					g = t * 255 + 0.5
					b = p * 255 + 0.5
				when 1
					r = q * 255 + 0.5
					g = brightness * 255 + 0.5
					b = p * 255 + 0.5
				when 2
					r = p * 255 + 0.5
					g = brightness * 255 + 0.5
					b = 255 + 0.5
				when 3
					r = p * 255 + 0.5
					g = q * 255 + 0.5
					b = brightness * 255 + 0.5
				when 4
					r = t * 255 + 0.5
					g = p * 255 + 0.5
					b = brightness * 255 + 0.5
				when 5
					r = brightness * 255 + 0.5
					g = p * 255 + 0.5
					b = q * 255 | 0.5

		return {
			r: Math.floor(r)
			g: Math.floor(g)
			b: Math.floor(b)
		}

	class Vec
		constructor: (@x=0, @y=0) ->

		plusEq: (other) ->
			@x += other.x
			@y += other.y
			return this

		minus: (other) ->
			new Vec @x - other.x, @y - other.y

		clampEq: (length) ->
			if @length > length
				ratio = length / @length
				@x *= ratio
				@y *= ratio
			return this

		clamp: (length) ->
			@clone.clampEq length

		times: (scale) ->
			@clone.timesEq scale

		timesEq: (scale) ->
			@x *= scale
			@y *= scale
			return this

		toString: ->
			"(#{@x}, #{@y})"

		@properties
			length:
				get: -> Math.sqrt(@x*@x + @y*@y)

			clone:
				get: -> new Vec @x, @y

			normal:
				get: ->
					length = @length
					return new Vec @x / length, @y / length


	canvas	= $ '#art'

	CANVAS_WIDTH	= canvas.width()
	CANVAS_HEIGHT	= canvas.height()

	context			= canvas[0].getContext '2d'
	context.canvas.width	= CANVAS_WIDTH
	context.canvas.height	= CANVAS_HEIGHT

	UNIT = CANVAS_WIDTH / 40
	BASE_Y = CANVAS_HEIGHT - UNIT * 2
	BASE_HEIGHT = CANVAS_HEIGHT - BASE_Y

	context.fillStyle = context.strokeStyle = "#e8e8e8"
	#context.fillStyle = context.strokeStyle = "black"

	class Structure
		constructor: (@x, @width) ->

		draw: ->
			context.beginPath()

	class Building extends Structure
		constructor: (x, width) ->
			super x, width
			@height = UNIT * random.inRange(2, 4) / Math.pow(width / 2 / UNIT, 2)

		draw: ->
			super()
			
			margin = UNIT * 0.1
			context.fillRect @x, BASE_Y - 1, @width, BASE_HEIGHT
			context.fillRect @x + margin, BASE_Y - @height, @width - 2 * margin, @height

			WINDOW_UNIT = UNIT * 0.2
			if random.chance 70
				left	= @x + margin + WINDOW_UNIT
				right	= left + @width - 2 * margin - 2 * WINDOW_UNIT
				top	= BASE_Y - @height + WINDOW_UNIT
				bottom	= top + @height - 2 * WINDOW_UNIT
				x = left + random.intInRange((right - left) / WINDOW_UNIT) * WINDOW_UNIT
				y = top + random.intInRange((bottom - top) / WINDOW_UNIT)  * WINDOW_UNIT

				context.clearRect x, y, WINDOW_UNIT, WINDOW_UNIT

			if random.chance 30
				# block thing
				width = @width * random.inRange 0.2, 0.4
				height = width * random.inRange 0.1, 0.2
				x = @x + margin + random.inRange @width - margin * 2 - width

				context.fillRect x, BASE_Y - @height - height + 1, width, height

			if random.chance 0
				# antenna things?
				number = random.choose 1, 2, 2
				for i in [0...number]
					width = UNIT * 0.1
					height = UNIT * random.inRange 0.1, 0.2
					x = @x + margin + random.inRange @width - margin * 2 - width

					context.fillRect x, BASE_Y - @height - height + 1, width, height

	fillWithBackgroundBuildings = (startX, endX) ->
			x = startX
			while x < endX
				width	= Math.min(UNIT * random.inRange(0.4, 0.6), endX - x)

				if prevHeightMultiplier?
					heightMultiplier = prevHeightMultiplier
					while Math.abs(prevHeightMultiplier - heightMultiplier) < 0.1
						heightMultiplier = random.inRange(0.4, 0.8)
				else
						heightMultiplier = random.inRange(0.4, 0.8)
				prevHeightMultiplier = heightMultiplier

				height	= BASE_HEIGHT * heightMultiplier

				context.fillRect x, CANVAS_HEIGHT - height, width + 1, height
				x += width

	class ThinBridge extends Structure
		draw: ->
			super()
			context.fillRect @x, BASE_Y, @width, UNIT * 0.2
			fillWithBackgroundBuildings @x, @x + @width

	class ArcBridge extends Structure
		draw: ->
			super()
			context.fillRect @x, BASE_Y, @width, UNIT * 0.2

			pillarWidth	= UNIT * random.inRange 0.2, 0.3
			pillarTop	= BASE_Y - UNIT * random.inRange 0.1, 0.2
			pillarHeight	= CANVAS_HEIGHT - pillarTop

			rounded = true #random.coinFlip

			context.fillRect @x, pillarTop - 1, pillarWidth, pillarHeight + 1
			if rounded
				context.beginPath()
				context.arc @x + pillarWidth / 2, pillarTop, pillarWidth / 2, Math.PI, 0
				context.fill()

			context.fillRect @x + @width - pillarWidth, pillarTop - 1, pillarWidth, pillarHeight + 1
			if rounded
				context.beginPath()
				context.arc @x + @width - pillarWidth / 2, pillarTop, pillarWidth / 2, Math.PI, 0
				context.fill()

			arcRadius = (@width - pillarWidth) / 2

			context.beginPath()
			context.arc @x + @width / 2, BASE_Y, arcRadius, Math.PI, 0
			context.lineWidth = UNIT * 0.3
			context.stroke()

			numberOfSupports = 10
			context.lineWidth = UNIT * 0.15
			inc = (@width - pillarWidth) / numberOfSupports
			for i in [0...numberOfSupports]
				x	= @x + inc * (i + 1)
				theta	= Math.acos((x - @x - @width / 2) / arcRadius)
				y	= BASE_Y - arcRadius * Math.sin theta

				context.beginPath()
				context.moveTo x, y
				context.lineTo x, BASE_Y + 1
				context.stroke()

			fillWithBackgroundBuildings @x, @x + @width

	RULES =
		building: {or: ["$thinBuilding", "$thinBuilding", "fatBuilding"]}

		thinBuildingCluster: {or: [
			["$thinBuilding"]
			["$thinBuilding"]
			["$thinBuilding", "$thinBuilding"]
		]}

		buildingCluster: {or: [
			["thinBuildingCluster"]
			["thinBuildingCluster"]
			["$fatBuilding", "thinBuildingCluster"]
			["thinBuildingCluster", "$fatBuilding"]
		]}

		bridge: [{or: ["bigBridge", "smallBridge"]}]

		bigBridge: {or: ["$arcBridge", "$longThinBridge", "$longThinBridge"]}

		smallBridge: "$smallThinBridge"

		anything: ["buildingCluster", "bridge", "buildingCluster", "anything"]

	REIFIERS =
		thinBuilding:
			structure:	Building
			width:		2

		fatBuilding:
			structure:	Building
			width:		3

		smallThinBridge:
			structure:	ThinBridge
			width:		1

		longThinBridge:
			structure:	ThinBridge
			width:		5

		arcBridge:
			structure:	ArcBridge
			width:		5

	isTerminal = (token) ->
		token.substring(0, 1) is "$"

	minNumberOfTerminals = CANVAS_WIDTH / UNIT + 1

	terminalCount = (chain) ->
		count = 0
		for symbol in chain
			break unless isTerminal symbol
			++count
		return count

	applyProduction = (data) ->
		if data instanceof String or typeof data is "string"
			return [data]
		else if data instanceof Array
			result = []
			for element in data
				result = result.concat applyProduction element
			return result
		else if data.or?
			result = applyProduction random.any data.or
			return result
		else throw new Error "Can't produce unknown type"

	produce = (symbol) ->
		if isTerminal symbol
			[symbol]
		else
			applyProduction RULES[symbol]

	buildChain = ->
		chain = ["anything"]

		while terminalCount(chain) < minNumberOfTerminals
			nextChain = []
			for symbol in chain
				production = produce symbol
				nextChain = nextChain.concat production
			chain = nextChain

		return chain

	window.drawArt = ->
		chain		= buildChain()
		structures	= []
		x		= -random.intInRange UNIT

		for symbol in chain
			break if x > CANVAS_WIDTH

			symbol	= symbol.substring 1
			reifier	= REIFIERS[symbol]
			width	= reifier.width * UNIT

			structures.push new reifier.structure x, width

			x += width - 1

		structure.draw() for structure in structures
