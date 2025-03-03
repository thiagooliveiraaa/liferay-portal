<@liferay_ui["panel-container"]
	extended=true
	id="${namespace + 'facetAssetEntriesPanelContainer'}"
	markupView="lexicon"
	persistState=true
>
	<@liferay_ui.panel
		collapsible=true
		cssClass="search-facet search-facet-display-label"
		id="${namespace + 'facetAssetEntriesPanel'}"
		markupView="lexicon"
		persistState=true
		title="type"
	>
		<#if !assetEntriesSearchFacetDisplayContext.isNothingSelected()>
			<@clay.button
				cssClass="btn-unstyled c-mb-4 facet-clear-btn"
				displayType="link"
				id="${namespace + 'facetAssetEntriesClear'}"
				onClick="Liferay.Search.FacetUtil.clearSelections(event);"
			>
				<strong>${languageUtil.get(locale, "clear")}</strong>
			</@clay.button>
		</#if>

		<#if entries?has_content>
			<div class="label-container">
				<#list entries as entry>
					<@clay.button
						cssClass="label label-lg facet-term ${(entry.isSelected())?then('label-primary facet-term-selected', 'label-secondary facet-term-unselected')} term-name"
						data\-term\-id="${entry.getFilterValue()}"
						disabled="true"
						displayType="unstyled"
						onClick="Liferay.Search.FacetUtil.changeSelection(event);"
					>
						<span class="label-item label-item-expand">
							${htmlUtil.escape(entry.getBucketText())}

							<#if entry.isFrequencyVisible()>
								(${entry.getFrequency()})
							</#if>
						</span>
					</@clay.button>
				</#list>
			</div>
		</#if>
	</@>
</@>