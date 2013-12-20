whenPageLoads '/projects', ->
	$previews = $ '.collection > .previews > .preview'

	selectCategory = ($el) ->
		$('.collection > .categories > .category').removeClass 'selected'
		$el.addClass 'selected'
		# scrollTo $('.collection')

	showCategory = (category) ->
		$previews.each (_, el) ->
			$el = $(el)

			if $el.data('category') is category
				$el.show()
			else
				$el.hide()

	$('.collection > .categories > .category').click ->
		$this = $(this)
		category = $this.data 'category'
		if category is 'all'
			$previews.show()
		else
			showCategory category
		selectCategory $this

	$previews.each (_, preview) ->
		$preview = $(preview)

		$('a', $preview).click (e) ->
			unless e.ctrlKey
				e.preventDefault()
				data = projectCollection[$preview.data('id')]

				$('.showcase > .screenshot img').attr 'src', data['showcase']
				$('.showcase .project-link').attr 'href', data['url']
				$('.showcase > .info > .title').text data['title']
				$('.showcase > .info > .summary').html data['short-description']

				scrollToTop()
