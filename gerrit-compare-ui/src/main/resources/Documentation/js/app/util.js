structuralDiff.createPagination =  function () {
	var rows = $('#gerritCommitStatus').find('tbody tr').length;
	var no_rec_per_page = 15;
	var no_pages = Math.ceil(rows / no_rec_per_page);
	var $pagenumbers = $('<div id="pages" style="float: right;"></div>');
	for (var i = 0; i < no_pages; i++)
	{
		$('<span class="page">' + (i + 1) + '</span>').appendTo($pagenumbers);
	}
	$pagenumbers.insertAfter('#gerritCommitStatus');
	$('.page').hover(
			function () {
				$(this).addClass('hover');
			},
			function () {
				$(this).removeClass('hover');
			}
	);
	$('#gerritCommitStatus').find('tbody tr').hide();
	var tr = $('#gerritCommitStatus tbody tr');
	for (i = 0;i <= no_rec_per_page - 1;i++)
	{
		$(tr[i]).show();
	}
	$('span').click(function (event) {
		$('#gerritCommitStatus').find('tbody tr').hide();
		for (i = ($(this).text() - 1) * no_rec_per_page;i <= $(this).text() * no_rec_per_page - 1;i++)
		{
			$(tr[i]).show();
		}
	});
};


structuralDiff.diffUsingJS =  function (baseVersion, patchVersion, draftMsgArray) {
	var $ = dojo.byId;
	var url = window.location.toString().split("#")[0];
	var base = difflib.stringAsLines(baseVersion);
	var newtxt = difflib.stringAsLines(patchVersion);
	var sm = new difflib.SequenceMatcher(base, newtxt);
	var opcodes = sm.get_opcodes();
	var diffoutputdiv = $("diffoutput");
	var code;
	while (diffoutputdiv.firstChild)
		diffoutputdiv.removeChild(diffoutputdiv.firstChild);
	contextSize = null;
	diffoutputdiv.appendChild(diffview.buildView({
		baseTextLines : base,
		newTextLines : newtxt,
		opcodes : opcodes,
		baseTextName : "Base Set",
		newTextName : "Patch Set",
		contextSize : contextSize,
		viewType : 0,
		draftMsgArray : draftMsgArray
	}));
	for ( var i = 0; i < opcodes.length; i++) {
		code = opcodes[i];
		if (code[0] != "equal") {
			navigateModule.incrementChanges();
		}
	}
};

structuralDiff.createBaseURL = function (url) {
	var baseUrl;
	if (url.length > 0) {
		baseUrl = url.substring(0,url.length - 1);
		baseUrl = baseUrl.concat("1");
		return baseUrl;
	} else {
		return "";
	}
};