var navigateModule = (function () {
	var changes = 0;
	var counter = -1;
	return {
		incrementCounter: function () {
			return counter++;
		},
		
		decrementCounter: function () {
			return counter--;
		},
		
		getCounter: function () {
			return counter;
		},
		
		resetCounter: function () {
			counter = -1;
		},
		
		updateCounter: function () {
			counter = changes - 1;
			return counter;
		},
		
		incrementChanges: function () {
			changes++;
		},
		
		resetChanges: function () {
			changes = 0;
		},
		
		getTotalChange: function () {
			return changes;
		} 
	}; 
})();

var urlModule = (function () {
	var lastClickedUrl = "";
	return {
		setLastClickedUrl: function (url) {
			lastClickedUrl = url;
		},
		
		getLastClickedUrl: function () {
			return lastClickedUrl;
		}
	};
})();

var changeStructureTree = (function () {
	var baseChangeArray = [];
	var patchChangeArray = [];
	return {
		setBaseChangeArray: function (changeArr) {
			baseChangeArray = changeArr;
		},
		
		setPatchChangeArray: function (changeArr) {
			patchChangeArray = changeArr;
		},
		
		getBaseChangeArray: function () {
			return baseChangeArray;
		},
		
		getPatchChangeArray: function () {
			return patchChangeArray;
		}
	};
})();

var patchset = (function () {
	var multiArray;
	return {
		setMultiArray: function (patchSet) {
			multiArray = patchSet;
		},
		
		getMultiArray: function () {
			return multiArray;
		}
	};
})();


(function ($) {
	$.gotoNextChange = function () {
		var marker = '';
		var w;
		var counter = navigateModule.getCounter();
		var changes = navigateModule.getTotalChange();
		if (counter < changes) {
			counter = navigateModule.incrementCounter();
			if (counter === changes) {
				navigateModule.resetCounter();
			}
			marker = '#Marker' + counter;
		}
		var offset = $(marker).offset();
		var topoffset = offset.top;
		var className =  $('#diffoutput').attr('class');
		if (className === "vScroll") {
			w = $(".vScroll");
			w.scrollTop(topoffset - w.height() / 2);
		} else {
			w = $(".vScrollFullScreen");
			w.scrollTop(topoffset - w.height() / 2);
		}
	};
	
	$.gotoPreviousChange = function () {
		var marker = '';
		var w;
		var counter = navigateModule.getCounter();
		var changes = navigateModule.getTotalChange();
		if (counter >= 0) {
			navigateModule.decrementCounter();
			if (counter === -1) {
				counter = navigateModule.updateCounter();
			}
			marker = '#Marker' + counter;
		}
		var offset = $(marker).offset();
		var topoffset = offset.top;
		var className = $('#diffoutput').attr('class');
		if (className === "vScroll") {
			w = $(".vScroll");
			w.scrollTop(topoffset - w.height() / 2);
		} else {
			w = $(".vScrollFullScreen");
			w.scrollTop(topoffset - w.height() / 2);
		}
	};
})(jQuery);

// This Method compares selected Patches with BaseVersion
function PatchComparison(patchSetUrl) {
	var baseVersion = "";
	var patchVersion = "";
	var lastClickedUrl = urlModule.getLastClickedUrl();

	$("#containerId").hide();
	$("#wait").show();
	
	if (patchSetUrl === '') {
		if (lastClickedUrl !== null) {
			var urlPath = lastClickedUrl.split(",");
			if (urlPath.length === 3) {
				patchSetUrl = urlPath[0] + ",1," + urlPath[2];
			}
			patchSetUrl = patchSetUrl.substring(0, patchSetUrl.length - 1).concat('1');
		}
	}
	
	if (patchSetUrl === lastClickedUrl) {
		baseVersion = "No Difference Found";
		patchVersion = "";
		$('#ModificationDetails').treetable('destroy');
		$('#ModificationDetails').html('');
		diffUsingJS(baseVersion, patchVersion, null);
		$("#containerId").show();
		$("#wait").hide();
	} else {
		$.get("../gerritPlugin", { patchSetURL1:lastClickedUrl, patchSetURL2:patchSetUrl  }).done(function( comparatorResult ) {
			if ( comparatorResult.indexOf("JavaCode") === 0 ) {
				$('#ModificationDetails').treetable('destroy');
				$('#ModificationDetails').html('');
				baseVersion = "";
				patchVersion = comparatorResult.replace('JavaCode', '');
				diffUsingJS(baseVersion, patchVersion, null);
			} else {
				parseJSONResponse(comparatorResult);
			}
		});
		$("#containerId").show();
		$("#wait").hide();
	}
	urlModule.setLastClickedUrl(patchSetUrl);
}



// This Method fetches the data from the provided URL
function fetchDatafromURL(url, patchNo) {
	var baseVersion = "";
	var patchVersion = "";
	var multiArray = patchset.getMultiArray();
	
	$("#containerId").hide();
	$("#wait").show();
	
	urlModule.setLastClickedUrl(url);

	$.get("../gerritPlugin", { url: url  }).done(function (comparatorResult) {
		var patchSetLabel = "<a onclick=\"PatchComparison('')\">Base </a>";
		var baseSetLabel = "<a onclick=\"PatchComparison('')\">Base </a>";
		for (var i = 0; i < multiArray.length; i++) {
			patchSetLabel += " ";
			patchSetLabel += multiArray[i][patchNo];
		}

		$('#patchSetLabel1').html(patchSetLabel);
		$('#patchSetLabel2').html(patchSetLabel);

		if (comparatorResult.indexOf("JavaCode") === 0) {
			$('#ModificationDetails').treetable('destroy');
			$('#ModificationDetails').html('');
			baseVersion = "";
			patchVersion = comparatorResult.replace('JavaCode', '');
			diffUsingJS(baseVersion, patchVersion, null);
		} else {
			parseJSONResponse(comparatorResult);
		}
		$("#containerId").show();
		$("#wait").hide();
		$("#ShowDetail").show();
		$("#HideDetail").hide();
	}).fail(function() {  console.log("Error occured in fetchDatafromURL method"); });
}

// This Method parses the JSONResponse
function parseJSONResponse(comparatorResult) {
	var parsedJSON = JSON.parse(comparatorResult);
	var changedFileTreeStructure;
	var draftMsgArray;
	var baseVersion = "";
	var patchVersion = "";
	var key = "";
	var parameter = "";
	var baseChangeArray = [];
	var patchChangeArray = [];
	
	navigateModule.resetCounter();
	navigateModule.resetChanges();
	
	changedFileTreeStructure = "<table id=\"ModificationDetails\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr data-tt-id='1'><td><span class='compilationUnit'>Compilation Unit </span></td></tr> <tr data-tt-id='1-1' data-tt-parent-id='1'><td><span class='javaIcon'>";
	draftMsgArray = parsedJSON.draftMessage;
	
	// package name changes
	if (parsedJSON.pkg.diff === -1 || parsedJSON.pkg.diff === 10 || parsedJSON.pkg.diff === 1) {
		baseVersion += parsedJSON.pkg.lines[0].value;
		patchVersion += parsedJSON.pkg.lines[1].value;
	}

	// changes in Import statement
	for (var i = 0; i < parsedJSON.imports.length; i++) {
		if (parsedJSON.imports[i].diff !== 0) {
			baseVersion += '\n';
			patchVersion += '\n';
		}
		
		if (parsedJSON.imports[i].diff === 1) {
			baseVersion += parsedJSON.imports[i].lines[0].value;
		} else if (parsedJSON.imports[i].diff === -1) {
			patchVersion += parsedJSON.imports[i].lines[1].value;
		} else if (parsedJSON.imports[i].diff === 10) {
			baseVersion += parsedJSON.imports[i].lines[0].value;
			patchVersion += parsedJSON.imports[i].lines[1].value;
		}
	}

	// Changes in program body
	for (i = 0; i < parsedJSON.types.length; i++) {
		if (parsedJSON.types[i].diff !== 0) {
			baseVersion += '\n';
			patchVersion += '\n';
		}
		
		if (parsedJSON.types[i].diff === 1) {
			baseVersion += parsedJSON.types[i].declarations[0].completeNodeValue;
			changedFileTreeStructure += parsedJSON.types[i].declarations[0].name + "</span></td></tr>";
			
		} else if (parsedJSON.types[i].diff === -1) {
			patchVersion += parsedJSON.types[i].declarations[1].completeNodeValue;
			changedFileTreeStructure += parsedJSON.types[i].declarations[1].name + "</span></td></tr>";
			
		} else if (parsedJSON.types[i].diff === 10) {
			changedFileTreeStructure += parsedJSON.types[i].declarations[0].name + "</span></td></tr>";

			for (var k = 0; k < parsedJSON.types[i].commonChilds.length; k++) {
				if (parsedJSON.types[i].commonChilds[k].diff !== 0) {
					baseVersion += '\n'; 
					patchVersion += '\n';
				}
				
				if (parsedJSON.types[i].commonChilds[k].diff === 10) {
					key = parsedJSON.types[i].commonChilds[k].declarations[0].name;
					changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1) + "' data-tt-parent-id='1-1'><td><span class=\"fileModified\"><a onclick=\"diffSelectedChange(this)\" >" + parsedJSON.types[i].commonChilds[k].declarations[0].name;
					if (parsedJSON.types[i].commonChilds[k].declarations[0].hasOwnProperty('parameters')) {
						parameter =  parsedJSON.types[i].commonChilds[k].declarations[0].parameters;
						parameter  = parameter.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
						key += '( ' + parameter + ')';
						changedFileTreeStructure +=  '( ' + parameter + ')' + "</a></span></td></tr>";
						
					} else {
						changedFileTreeStructure += "</a></span></td></tr>";
					}
					//Selecting Modified method start
			        baseVersion += parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
			        patchVersion += parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
			      //Selecting Modified method End
			        
			        baseChangeArray[key] = parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
			        patchChangeArray[key] = parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
				} else if (parsedJSON.types[i].commonChilds[k].diff === -1) {
					key = parsedJSON.types[i].commonChilds[k].declarations[1].name;
					changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1) + "' data-tt-parent-id='1-1'><td><span class=\"fileNew\"><a onclick=\"diffSelectedChange(this)\" >" + parsedJSON.types[i].commonChilds[k].declarations[1].name;
					if (parsedJSON.types[i].commonChilds[k].declarations[1].hasOwnProperty('parameters')) {
						parameter =  parsedJSON.types[i].commonChilds[k].declarations[1].parameters;
						parameter  = parameter.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
						key += '( ' + parameter + ')';
						changedFileTreeStructure += '( ' + parameter + ')' + "</a></span></td></tr>";
					} else {
						changedFileTreeStructure += "</a></span></td></tr>";
					}
					
			        patchVersion += parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
			        
			        baseChangeArray[key] = "";
			        patchChangeArray[key] = parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
				} else if (parsedJSON.types[i].commonChilds[k].diff === 1) {
					key = parsedJSON.types[i].commonChilds[k].declarations[0].name;
					changedFileTreeStructure += "<tr data-tt-id='1-1-" + (i + 1) + "' data-tt-parent-id='1-1'><td><span class=\"fileDelete\"><a onclick=\"diffSelectedChange(this)\" >" + parsedJSON.types[i].commonChilds[k].declarations[0].name;
					if (parsedJSON.types[i].commonChilds[k].declarations[0].hasOwnProperty('parameters')) {
						parameter =  parsedJSON.types[i].commonChilds[k].declarations[0].parameters;
						parameter  = parameter.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
						key  += '(' + parameter + ')';
						changedFileTreeStructure += '(' + parameter + ')' + "</a></span></td></tr>";
					} else {
						changedFileTreeStructure += "</a></span></td></tr>";
					}
					
			        baseVersion += parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
			        baseChangeArray[key] = parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
			        patchChangeArray[key] = "";
				}
			}
		}
	}
	
	changeStructureTree.setBaseChangeArray(baseChangeArray);
	changeStructureTree.setPatchChangeArray(patchChangeArray);
	
	changedFileTreeStructure += "</tbody></table>";
	$('#ModificationDetails').html(changedFileTreeStructure);

	if (baseVersion === "" && patchVersion === "") {
		$('#ModificationDetails').treetable('destroy');
		$('#ModificationDetails').html('');
		baseVersion = "No changes Found";
		patchVersion = "No changes Found";
	}
	
	diffUsingJS(baseVersion, patchVersion, draftMsgArray);
	$(".hide_Rows").hide();
	$('#ModificationDetails').treetable('destroy');
	createClassStructure('#ModificationDetails');
}

function diffSelectedChange( anchor ){
	var key = anchor.innerHTML;
	var baseChangeArray = changeStructureTree.getBaseChangeArray();
	var patchChangeArray = changeStructureTree.getPatchChangeArray();
	diffUsingJS(baseChangeArray[key], patchChangeArray[key], null );
}

function diffUsingJS(baseVersion, patchVersion, draftMsgArray) {
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
}

// This Method retrieves all changeIds list, project details and all Patches of most updated changes
function getChangeIdDetails( key ) {
	var parsedChangeJSON;
	var totalPatchSet;
	var multiArray;
	
	key = key.trim();
	$("#containerId").hide();
	$("#wait").show();
	
	$.get("../gerritPlugin", { id:key }).done(function( jsonData ) {
		parsedChangeJSON = JSON.parse( jsonData ).change;
		totalPatchSet = parsedChangeJSON.patchSets.length;
		populateProjectDetails( parsedChangeJSON );
		multiArray = new Array(totalPatchSet);
		for ( var i = 0; i < parsedChangeJSON.patchSets.length; i++) {
			var patch = parsedChangeJSON.patchSets[i];
			var tbody = "<tbody><tr data-tt-id='1'><td><span class='folder'>Patch "
					+ (i + 1) + "</span></td></tr>";
			var totalPatches = patch.patchList.length;
			multiArray[i] = new Array(totalPatches);
			for ( var j = 0; j < patch.patchList.length; j++) {
				tbody += "<tr data-tt-id='1-"
						+ (j + 1)
						+ "' data-tt-parent-id='1'><td><span class=\"file\"><a onclick=\"fetchDatafromURL('"
						+ patch.patchList[j].fileURL + "','" + j
						+ "')\" style=\"padding-left: 19px;\"> "
						+ patch.patchList[j].patchFileName
						+ "</a></span></td></tr>";
				multiArray[i][j] = "<a onclick=\"PatchComparison('"
						+ patch.patchList[j].fileURL + "')\"> " + (i + 1)
						+ "</a>";

			}
			patchset.setMultiArray(multiArray);
			var id = "patch" + (i + 1);
			tbody += "</tbody>";
			var table = "<table id=\"patch" + (i + 1)
					+ "\" cellpadding=\"0\" cellspacing=\"0\">" + tbody
					+ " </table>";
			$('#'+id).html(table);
		}
		
		if (parsedChangeJSON.patchSets.length == 1) {
			$("patch2").html('<table id="patch2" cellpadding="0" cellspacing="0"><tbody></tbody></table>');
			$("patch3").html('<table id="patch3" cellpadding="0" cellspacing="0"><tbody></tbody></table>');
		} else if (parsedChangeJSON.patchSets.length == 2) {
			$("patch3").html('<table id="patch3" cellpadding="0" cellspacing="0"><tbody></tbody></table>');
		}
		$('#patch1').treetable('destroy');
		comparePatchIds('#patch1');
		$("#wait").hide();
	});
}

function populateProjectDetails( parsedChangeJSON ){
	$("#owner").text( parsedChangeJSON.owner );
	$("#uploadedon").text( parsedChangeJSON.creationDate );
	$("#updatedon").text( parsedChangeJSON.lastUpdationDate );
	$("#branch").text( parsedChangeJSON.branch );
	$("#project").text( parsedChangeJSON.projectName );
	$("#commitmsg").text( parsedChangeJSON.subject );
}

function saveReviewerComment(e){
	var rowIndex = e.target.parentNode.parentNode.parentNode.rowIndex;
	var cellIndex = e.target.parentNode.parentNode.cellIndex; 
	var tableColumn = e.target.parentNode.parentNode;
	var element = tableColumn.getElementsByTagName('textarea');
	var draftMessage = element[0].value;
	var lastClickedUrl = urlModule.getLastClickedUrl();
	
	if( draftMessage != ''){
		element[0].style.display = "none";
		var p = tableColumn.getElementsByTagName('p');
		p[0].style.display = "block";
		p[0].innerHTML = draftMessage;
		
		var divTag = e.target.parentNode;
		var buttons = divTag.getElementsByTagName('button');
		buttons[0].style.display = "none";
		buttons[3].style.display = "none";
		buttons[2].style.display = "none";
		buttons[1].style.display = "inline";
		
		$.get("../patchDetailService", { lineNumber:rowIndex, side:cellIndex, message:draftMessage, changeDetail:lastClickedUrl, flag:'save' }).done(function( result ) {
			console.log('Draft Message saved successfully ')
		});
	}
}

function deleteTableRow(e){
	var rowIndex = e.target.parentNode.parentNode.parentNode.rowIndex;
	var cellIndex = e.target.parentNode.parentNode.cellIndex; 
	var tableColumn = e.target.parentNode.parentNode;
	var element = tableColumn.getElementsByTagName('textarea');
	var draftMessage = element[0].value;
	var lastClickedUrl = urlModule.getLastClickedUrl();
	
	document.getElementById("diffOutTable").deleteRow(rowIndex);
	if( draftMessage != ''){
		$.get("../patchDetailService", { lineNumber:rowIndex, side:cellIndex, message:draftMessage, changeDetail:lastClickedUrl, flag:'discard' }).done(function( result ) {
			console.log('Draft Message Deleted successfully ')
		});
	}
}


function getGerritCommitDetails(){
	$.get("../gerritPlugin", function(data) {
		var parsedJSON = JSON.parse( data ).changeIDs;
		createStatusTable( parsedJSON.reverse() );
		$.createPagination();
		});
}


function createStatusTable(commitList){
	var tbody = "<tbody>";
	for ( var i = 0; i < commitList.length; i++) {
		tbody +="\n<tr><td><a onclick=\"init(this)\">" + commitList[i].change_id + " </td><td> " + commitList[i].commitMsg + "</td></tr>";
	}
	tbody += "</tbody>";
	document.getElementById("openStatus").innerHTML = tbody;
}


$(document).jkey('f6', function() {
	$.gotoNextChange();
});

$(document).jkey('f7', function() {
	$.gotoPreviousChange();
});

$(document).ready(function() {
	$(".hide_Rows").hide();
	$(".showDetails").click(function() {
    	$(".hide_Rows").toggle();
    	$(".showDetails").toggle();
    });
	
	navigateModule.resetCounter();
	
	$("#markerNext").click(function() {
		$.gotoNextChange();
	});

	$("#markerPrev").click(function() {
		$.gotoPreviousChange();
	});
});
