$ ->
	setUpPJAXLinks = ->
		$(document).pjax('.page-content', '#page-content')
	$(document).on('pjax:complete', setUpPJAXLinks)
	setUpPJAXLinks()
