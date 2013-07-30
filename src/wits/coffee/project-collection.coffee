whenPageLoads '/projects', ->
	$previews = $ '.collection > .previews > .preview'

	selectCategory = ($el) ->
		$('.collection > .categories > .category').removeClass 'selected'
		$el.addClass 'selected'
		# scrollTo $('.collection')

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

	$previews.each (_, preview) ->
		$preview = $(preview)

		$('a', $preview).click (e) ->
			unless e.ctrlKey
				e.preventDefault()
				data = projectCollection[$preview.data('id')]

				$('.showcase > .screenshot img').attr 'src', data['showcase']
				$('.showcase > .screenshot > a').attr 'href', data['url']
				$('.showcase > .info > .title').text data['title']
				$('.showcase > .info > .summary > p').text data['short-description']
				$('.showcase > .info > .summary > p').text data['short-description']
				$('.showcase > .info > a').attr 'href', data['url']

				scrollToTop()
