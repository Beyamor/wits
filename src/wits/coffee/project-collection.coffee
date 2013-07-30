onPageReady '/projects', ->
	$previews = $ '.collection > .previews > .preview'

	selectCategory = ($el) ->
		$('.collection > .categories > .category').removeClass 'selected'
		$el.addClass 'selected'


	$('#collection-category-all').click ->
		$previews.show()
		selectCategory $(this)

	showCategory = (category) ->
		$previews.each (_, el) ->
			$el = $(el)

			if $el.data('category') is category
				$el.show()
			else
				$el.hide()

	$('#collection-category-games').click ->
		showCategory 'game'
		selectCategory $(this)

	$('#collection-category-pcg').click ->
		showCategory 'pcg'
		selectCategory $(this)

	$previews.click (e) ->
		data = projectCollection[$(this).data('id')]

		$('.showcase > .screenshot > img').attr 'src', data['showcase']
		$('.showcase > .info > .title').text data['title']
		$('.showcase > .info > .summary > p').text data['short-description']
		$('.showcase > .info > .summary > p').text data['short-description']
		$('.showcase > .info > a').attr 'href', data['url']
