$ ->
	$(document).pjax('.page-content', '#page-content')
	$(document).on('pjax:complete', ->
		$(document).pjax('.page-content', '#page-content')
	)
