onloadCallbacks = {}

window.whenPageLoads = (page, f) ->
	onloadCallbacks[page] or= []
	onloadCallbacks[page].push f

callOnloadCallbacks = ->
	page = document.location.pathname

	if onloadCallbacks[page]?
		f() for f in onloadCallbacks[page]

$ ->
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

	window.scrollTo = ($el) ->
		# Scroll back up the header
		$('body,html').animate({
			scrollTop: $el.offset().top,
			duration: 200,
			queue: false
		})

		# But stop scrolling animation if the user tries to scroll
		# http://stackoverflow.com/questions/8858994/let-user-scrolling-stop-jquery-animation-of-scrolltop
		$viewport.bind("scroll mousedown DOMMouseScroll mousewheel keyup", (e) ->
			if e.which > 0 or e.type == "mousedown" or e.type == "mousewheel"
				$viewport.stop().unbind('scroll mousedown DOMMouseScroll mousewheel keyup')
		)
	window.scrollToTop = ->
		scrollTo $('#header')

	pjaxReload = ->
		scrollToTop()

	$(document).on('pjax:success', pjaxReload)

	#$('#art').height($(window).height() - $('#header').height())
	## ugh, so hack. header height + 120 pixels of padding
	#$('#page').css('min-height', ($(window).height() - $('#header').height() - 120))
	drawArt('art') if drawArt?

	$(document).on('pjax:end', callOnloadCallbacks)
	callOnloadCallbacks()
