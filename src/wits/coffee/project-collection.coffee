$ ->
	$previews = $ '.collection > .previews'

	$('#collection-category-all').click ->
		$('.preview', $previews).show()

	showCategory = (category) ->
		$('.preview', $previews).each (_, e) ->
			$e = $(e)

			if $e.data('category') is category
				$e.show()
			else
				$e.hide()

	$('#collection-category-games').click ->
		showCategory 'game'

	$('#collection-category-pcg').click ->
		showCategory 'pcg'
