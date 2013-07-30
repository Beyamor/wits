$ ->
	$previews = $ '.collection > .previews > .preview'

	$('#collection-category-all').click ->
		$previews.show()

	showCategory = (category) ->
		$previews.each (_, el) ->
			$el = $(el)

			if $el.data('category') is category
				$el.show()
			else
				$el.hide()

	$('#collection-category-games').click ->
		showCategory 'game'

	$('#collection-category-pcg').click ->
		showCategory 'pcg'

	$previews.click (e) ->
		data = projectCollection[$(this).data('id')]

		$('.showcase > .screenshot > img').attr 'src', data['showcase']
		$('.showcase > .info > .title').text data['title']
		$('.showcase > .info > .summary > p').text data['short-description']
		$('.showcase > .info > .summary > p').text data['short-description']
		$('.showcase > .info > a').attr 'href', data['url']
