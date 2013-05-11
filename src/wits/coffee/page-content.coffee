$ ->
	$.pjax('.page-content', '#page')
	$.on 'pjax:complete', ->
		$.pjax('.page-content', '#page')
