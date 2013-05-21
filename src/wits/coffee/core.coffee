$ ->
	class window.Canvas
		constructor: (id) ->
			@el		= $("##{id}")
			@context	= @el[0].getContext("2d")

			@clearColor	= "black"

			@el.resize ->
				@resetWidth()
				@resetHeight()
			@resetWidth()
			@resetHeight()

		drawRect: (x, y, width, height, color) ->
			@context.fillStyle = color
			@context.fillRect(x, y, width, height)

		resetWidth: ->
			@context.canvas.width	= @width	= @el.width()

		resetHeight: ->
			@context.canvas.height	= @height	= @el.height()

		drawTriangle: ([x1, y1], [x2, y2], [x3, y3], color) ->
			@context.beginPath()
			@context.moveTo(x1, y1)
			@context.lineTo(x2, y2)
			@context.lineTo(x3, y3)
			@context.lineTo(x1, y1)
			@context.fillStyle = color
			@context.fill()

		drawImage: (src, x=0, y=0) ->
			img		= new Image
			img.onload	= => @context.drawImage(img, x, y)
			img.src		= src

		clear: ->
			@drawRect(0, 0, @width, @height, @clearColor)

	# Used to fix the header to the top of the screen when scrolling down
	# Dunno. Looks kinda cheesy, but I'll keep the code around for now.
	###
	$header		= $('#header')
	basePos		= $header.offset().top
	fixHeader = ->
		if $(window).scrollTop() > basePos
			$header.css({'position': 'fixed', 'top': "0px"})
		else
			$header.css({'position': 'relative', 'top': 'auto'})
	$(window).scroll(fixHeader)
	fixHeader()
	###
	
	$viewport = $('html, body')
	$.pjax.defaults.scrollTo = false if $.support.pjax
	$(document).pjax('.pjax', '#page')

	pjaxReload = ->
		# Scroll back up the header
		$('body,html').animate({
			scrollTop: $('#header').offset().top,
			duration: 200,
			queue: false
		})

		# But stop scrolling animation if the user tries to scroll
		# http://stackoverflow.com/questions/8858994/let-user-scrolling-stop-jquery-animation-of-scrolltop
		$viewport.bind("scroll mousedown DOMMouseScroll mousewheel keyup", (e) ->
			if e.which > 0 or e.type == "mousedown" or e.type == "mousewheel"
				$viewport.stop().unbind('scroll mousedown DOMMouseScroll mousewheel keyup')
		)

	$(document).on('pjax:success', pjaxReload)

	$('#art').height($(window).height() - $('#header').height())
	drawArt('art') if drawArt?
