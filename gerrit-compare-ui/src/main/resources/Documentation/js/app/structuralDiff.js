structuralDiff = (function () {
	
	"use strict";
	
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
			addToBaseChangeArray: function (key, value) {
				baseChangeArray[key] = value;
			},
			
			addToPatchChangeArray: function (key, value) {
				patchChangeArray[key] = value;
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
	
	// This Method parses the JSON data
	function parseJSONResponse(comparatorResult) {
		var changedFileTreeStructure;
		var draftMsgArray;
		var baseVersion = "";
		var patchVersion = "";
		var i,
			k;
		var parsedJSON = JSON.parse(comparatorResult);
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
		for (i = 0; i < parsedJSON.imports.length; i++) {
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

				for (k = 0; k < parsedJSON.types[i].commonChilds.length; k++) {
					if (parsedJSON.types[i].commonChilds[k].diff !== 0) {
						baseVersion += '\n'; 
						patchVersion += '\n';
					}
					if (parsedJSON.types[i].commonChilds[k].diff === 10) {
						changedFileTreeStructure += createModifiedMethodList(parsedJSON, i, k, 0, 'fileModified');
						baseVersion += parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
				        patchVersion += parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
					} else if (parsedJSON.types[i].commonChilds[k].diff === -1) {
						changedFileTreeStructure += createModifiedMethodList(parsedJSON, i, k, 1, 'fileNew');
						patchVersion += parsedJSON.types[i].commonChilds[k].declarations[1].completeNodeValue;
					} else if (parsedJSON.types[i].commonChilds[k].diff === 1) {
						changedFileTreeStructure += createModifiedMethodList(parsedJSON, i, k, 0, 'fileDelete'); 
						baseVersion += parsedJSON.types[i].commonChilds[k].declarations[0].completeNodeValue;
					}
				}
			}
		}
		changedFileTreeStructure += "</tbody></table>";
		$('#ModificationDetails').html(changedFileTreeStructure);

		if (baseVersion === "" && patchVersion === "") {
			$('#ModificationDetails').treetable('destroy');
			$('#ModificationDetails').html('');
			baseVersion = "No changes Found";
			patchVersion = "No changes Found";
		}
		structuralDiff.diffUsingJS(baseVersion, patchVersion, draftMsgArray);
		$(".hide_Rows").hide();
		$('#ModificationDetails').treetable('destroy');
		createClassStructure('#ModificationDetails');
	}

	
	function createModifiedMethodList(parsedJSON, index1, index2, index3, fileOp) {
		var parameter = getParameter(parsedJSON, index1,index2,index3);
		var methodName =  parsedJSON.types[index1].commonChilds[index2].declarations[index3].name + parameter;
		var changedFileTreeStructure;
		if (parsedJSON.types[index1].commonChilds[index2].diff === 10) {
			changeStructureTree.addToBaseChangeArray(methodName, parsedJSON.types[index1].commonChilds[index2].declarations[0].completeNodeValue);
			changeStructureTree.addToPatchChangeArray(methodName, parsedJSON.types[index1].commonChilds[index2].declarations[1].completeNodeValue);
		} else if (parsedJSON.types[index1].commonChilds[index2].diff === -1) {
			changeStructureTree.addToBaseChangeArray(methodName, "");
			changeStructureTree.addToPatchChangeArray(methodName, parsedJSON.types[index1].commonChilds[index2].declarations[index3].completeNodeValue);
		} else if (parsedJSON.types[index1].commonChilds[index2].diff === 1) {
			changeStructureTree.addToBaseChangeArray(methodName, parsedJSON.types[index1].commonChilds[index2].declarations[index3].completeNodeValue);
			changeStructureTree.addToPatchChangeArray(methodName, "");
		}
		changedFileTreeStructure = addToModifiedMethodList(index1 + 1, methodName, fileOp);
		return changedFileTreeStructure;
	}

	
	function addToModifiedMethodList(i, methodName, clazz ){
		var row = "<tr data-tt-id='1-1-" + (i + 1) + "' data-tt-parent-id='1-1'><td><span class=\""+ clazz + "\"><a onclick=\"structuralDiff.diffSelectedChange(this)\" >" + methodName + "</a></span></td></tr>";
		return row;
	}

	
	function getParameter(parsedJSON, index1, index2, index3){
		if (parsedJSON.types[index1].commonChilds[index2].declarations[index3].hasOwnProperty('parameters')) {
			var parameter =  parsedJSON.types[index1].commonChilds[index2].declarations[index3].parameters;
			parameter  = parameter.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
			return '(' + parameter + ')';
		} else {
			return '';
		}
	}

	function populateProjectDetails(parsedChangeJSON) {
		$("#owner").text( parsedChangeJSON.owner );
		$("#uploadedon").text( parsedChangeJSON.creationDate );
		$("#updatedon").text( parsedChangeJSON.lastUpdationDate );
		$("#branch").text( parsedChangeJSON.branch );
		$("#project").text( parsedChangeJSON.projectName );
		$("#commitmsg").text( parsedChangeJSON.subject );
	}
	
	function createOpenStatusTable(commitList) {
		 $("#changeIdList-template").tmpl(commitList).appendTo("#openStatus");
	}
	
	function createPatchFileNameList(parsedChangeJSON) {
		var i,
			j,
			patch,
			tbody,
			id,
			totalPatches,
			table;
		var totalPatchSet = parsedChangeJSON.patchSets.length;
		var multiArray = new Array(totalPatchSet);
		for (i = 0; i < parsedChangeJSON.patchSets.length; i++) {
			patch = parsedChangeJSON.patchSets[i];
			tbody = "<tbody><tr data-tt-id='1'><td><span class='folder'>Patch "
					+ (i + 1) + "</span></td></tr>";
			totalPatches = patch.patchList.length;
			multiArray[i] = new Array(totalPatches);
			for (j = 0; j < patch.patchList.length; j++) {
				tbody += "<tr data-tt-id='1-"
						+ (j + 1)
						+ "' data-tt-parent-id='1'><td><span class=\"file\"><a onclick=\"structuralDiff.fetchDatafromURL('"
						+ patch.patchList[j].fileURL + "','" + j
						+ "')\" style=\"padding-left: 19px;\"> "
						+ patch.patchList[j].patchFileName
						+ "</a></span></td></tr>";
				multiArray[i][j] = "<a onclick=\"structuralDiff.PatchComparison('"
						+ patch.patchList[j].fileURL + "')\"> " + (i + 1)
						+ "</a>";

			}
			patchset.setMultiArray(multiArray);
			id = "patch" + (i + 1);
			tbody += "</tbody>";
			table = "<table id=\"patch" + (i + 1)
					+ "\" cellpadding=\"0\" cellspacing=\"0\">" + tbody
					+ " </table>";
			$('#'+id).html(table);
		}
	}
	
	function createPatchSetLabel(patchNo) {
		var patchSetLabel,
			i;
		var multiArray = patchset.getMultiArray();
		
		patchSetLabel = "<a onclick=\"structuralDiff.PatchComparison('')\">Base </a>";

		for (i = 0; i < multiArray.length; i++) {
			patchSetLabel += " ";
			patchSetLabel += multiArray[i][patchNo];
		}

		$('#patchSetLabel1').html(patchSetLabel);
		$('#patchSetLabel2').html(patchSetLabel);
	}
	
	return {
		// This Method fetches the data from the provided URL
		fetchDatafromURL: function (url, patchNo) {
			var baseVersion = "";
			var patchVersion = "";
			commentPanelCell.setEditableFlag("true");
			$("#containerId").hide();
			$("#wait").show();
			
			urlModule.setLastClickedUrl(url);

			$.get("../gerritPlugin", { url: url  }).done(function (comparatorResult) {
				if (comparatorResult.indexOf("JavaCode") === 0) {
					$('#ModificationDetails').treetable('destroy');
					$('#ModificationDetails').html('');
					baseVersion = "";
					patchVersion = comparatorResult.replace('JavaCode', '');
					structuralDiff.diffUsingJS(baseVersion, patchVersion, null);
				} else {
					parseJSONResponse(comparatorResult);
				}
				createPatchSetLabel(patchNo);
				$("#containerId").show();
				$("#wait").hide();
				$("#ShowDetail").show();
				$("#HideDetail").hide();
			}).fail(function () {  console.log("Error occured in fetchDatafromURL method"); });
		},
		
		// This Method compares selected Patches with BaseVersion
		PatchComparison: function(patchSetUrl) {
			var baseVersion = "";
			var patchVersion = "";
			var urlPath;
			var lastClickedUrl = urlModule.getLastClickedUrl();
			commentPanelCell.setEditableFlag("true");
			$("#containerId").hide();
			$("#wait").show();
			
			if (patchSetUrl === '') {
				if (lastClickedUrl !== null) {
					urlPath = lastClickedUrl.split(",");
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
				structuralDiff.diffUsingJS(baseVersion, patchVersion, null);
				$("#containerId").show();
				$("#wait").hide();
			} else {
				$.get("../gerritPlugin", { patchSetURL1:lastClickedUrl, patchSetURL2:patchSetUrl  }).done(function( comparatorResult ) {
					if ( comparatorResult.indexOf("JavaCode") === 0 ) {
						$('#ModificationDetails').treetable('destroy');
						$('#ModificationDetails').html('');
						baseVersion = "";
						patchVersion = comparatorResult.replace('JavaCode', '');
						structuralDiff.diffUsingJS(baseVersion, patchVersion, null);
					} else {
						parseJSONResponse(comparatorResult);
					}
				});
				$("#containerId").show();
				$("#wait").hide();
			}
			urlModule.setLastClickedUrl(patchSetUrl);
		},
	
		getChangeIdDetails: function (key) {
			var parsedChangeJSON;
			key = key.trim();
			$("#containerId").hide();
			$("#wait").show();
			
			$.get("../gerritPlugin", { id:key }).done(function( jsonData ) {
				parsedChangeJSON = JSON.parse( jsonData ).change;
				populateProjectDetails( parsedChangeJSON );
				createPatchFileNameList(parsedChangeJSON);
				if (parsedChangeJSON.patchSets.length == 1) {
					$("#patch2").html('<table id="patch2" cellpadding="0" cellspacing="0"><tbody></tbody></table>');
					$("#patch3").html('<table id="patch3" cellpadding="0" cellspacing="0"><tbody></tbody></table>');
				} else if (parsedChangeJSON.patchSets.length == 2) {
					$("#patch3").html('<table id="patch3" cellpadding="0" cellspacing="0"><tbody></tbody></table>');
				}
				$('#patch1').treetable('destroy');
				comparePatchIds('#patch1');
				$("#wait").hide();
			});
		},
		
		// This Method retrieves all changeIds list, project details and all Patches of most updated changes
		getGerritCommitDetails: function (){
			var parsedJSON;
			$.get("../gerritPlugin", function(data) {
				parsedJSON = JSON.parse( data ).changeIDs;
				createOpenStatusTable( parsedJSON.reverse() );
				structuralDiff.createPagination();
				});
		},

		diffSelectedChange: function ( anchor ){
			var key = anchor.innerHTML;
			var baseChangeArray = changeStructureTree.getBaseChangeArray();
			var patchChangeArray = changeStructureTree.getPatchChangeArray();
			structuralDiff.diffUsingJS(baseChangeArray[key], patchChangeArray[key], null );
		},
		
		saveReviewerComment: function (e,line) {
			var rowIndex = e.target.parentNode.parentNode.parentNode.rowIndex;
			var cellIndex = e.target.parentNode.parentNode.cellIndex; 
			var tableColumn = e.target.parentNode.parentNode;
			var element = tableColumn.getElementsByTagName('textarea');
			var draftMessage = element[0].value;
			var lastClickedUrl = urlModule.getLastClickedUrl();
			var p;
			if( draftMessage != ''){
				$(element[0]).hide();
				p = tableColumn.getElementsByTagName('p');
				$(p[0]).show();
				p[0].innerHTML = draftMessage;
				
				var divTag = e.target.parentNode;
				var buttons = divTag.getElementsByTagName('button');
				commentPanelCell.setEditableFlag("true");
				$(buttons[0]).hide();
				$(buttons[1]).show();
				$(buttons[2]).hide();
				$(buttons[3]).hide();
				$.get("../patchDetailService", { lineNumber:line, side:cellIndex, message:draftMessage, changeDetail:lastClickedUrl, flag:'save' }).done(function( result ) {
					console.log('Draft Message saved successfully ')
				});
			}
		},

		deleteTableRow: function (e,line){
			var rowIndex = e.target.parentNode.parentNode.parentNode.rowIndex;
			var cellIndex = e.target.parentNode.parentNode.cellIndex; 
			var tableColumn = e.target.parentNode.parentNode;
			var element = tableColumn.getElementsByTagName('textarea');
			var draftMessage = element[0].value;
			var lastClickedUrl = urlModule.getLastClickedUrl();
			commentPanelCell.setEditableFlag("true");
			document.getElementById("diffOutTable").deleteRow(rowIndex);
			if( draftMessage != ''){
				$.get("../patchDetailService", { lineNumber:line, side:cellIndex, message:draftMessage, changeDetail:lastClickedUrl, flag:'discard' }).done(function( result ) {
					console.log('Draft Message Deleted successfully ')
				});
			}
		}
	}
	
})();