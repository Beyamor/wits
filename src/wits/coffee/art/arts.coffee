window.arts = {}
ns = window.arts

random = {
	trueFalse: -> Math.random() < 0.5
	either: (a, b) -> if random.trueFalse() then a else b
	posNeg: -> random.either 1, -1
	inRange: (min, max) -> min + Math.random() * (max - min)
	inIntRange: (min, max) -> Math.floor(random.inRange min, max)
	chance: (percentChance) -> Math.random() * 100 <= percentChance
	any: (coll) -> coll[Math.floor(Math.random() * coll.length)]
	choose: (choices...) -> random.any choices
}
ns.random = random

class Canvas
	constructor: (id) ->
		@el		= $('#' + id)
		@context	= @el[0].getContext("2d")
		@clearColor	= "#202638"

		@el.resize ->
			@resetWidth()
			@resetHeight()
		@resetWidth()
		@resetHeight()

	resetWidth: ->
		@context.canvas.width = @width = @el.width()
	
	resetHeight: ->
		@context.canvas.height = @height = @el.height()

	drawPoint: (x, y, color) ->
		@drawRect(x, y, 1, 1, color)

	drawRect: ({x: x, y: y, width: width, height: height, rotation: rotation, color: color, centered: centered}) ->
		@context.save()
		@context.translate(x, y)
		@context.rotate(rotation) if rotation
		@context.translate(-width/2, -height/2) if centered
		@context.fillStyle = color
		@context.fillRect(0, 0, width, height)
		@context.restore()

	drawTriangle: ([x1, y1], [x2, y2], [x3, y3], color) ->
		@context.beginPath()
		@context.moveTo(x1, y1)
		@context.lineTo(x2, y2)
		@context.lineTo(x3, y3)
		@context.lineTo(x1, y1)
		@context.fillStyle = color
		@context.fill()

	drawLine: ([x1, y1], [x2, y2], color, width=1) ->
		@context.beginPath()
		@context.moveTo(x1, y1)
		@context.lineTo(x2, y2)
		@context.lineWidth = width
		@context.strokeStyle = color
		@context.stroke()

	drawCircle: (x, y, radius, color) ->
		@context.beginPath()
		@context.arc(x, y, radius, 0, 2 * Math.PI, false)
		@context.fillStyle = color
		@context.fill()


	outlineCircle: (x, y, radius, color, width=1) ->
		@context.beginPath()
		@context.arc(x, y, radius, 0, 2 * Math.PI, false)
		@context.lineWidth = width
		@context.strokeStyle = color
		@context.stroke()

	drawImage: (img, x, y, rotation=0) ->
		@context.save()
		@context.translate(x, y)
		@context.rotate(rotation) if rotation isnt 0
		@context.drawImage(img, -img.width/2, -img.height/2)
		@context.restore()

	clear: ->
		@drawRect({x: 0, y: 0, width: @width, height: @height, color: @clearColor})
ns.Canvas = Canvas

ns.hsvToRGB = ([h, s, v]) ->
	c = v * s
	h1 = h / 60
	x = c * (1 - Math.abs((h1 % 2) - 1))
	m = v - c

	[r, g, b] =\
		if not h? then [0, 0, 0]
		else if (h1 < 1) then [c, x, 0]
		else if (h1 < 2) then [x, c, 0]
		else if (h1 < 3) then [0, c, x]
		else if (h1 < 4) then [0, x, c]
		else if (h1 < 5) then [x, 0, c]
		else if (h1 <= 6) then [c, 0, x]

	return [255 * (r + m), 255 * (g + m), 255 * (b + m)].map (x) -> Math.floor(x)
ns.clamp = (val, min, max) -> Math.max(min, Math.min(max, val))
