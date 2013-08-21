structuralDiff.commentPanel = (function () {
	
	"use strict";
	
	function insertTRow(rowIndex){
		if (!isNaN(rowIndex)) {
			rowIndex = parseInt(rowIndex);
		} 
		var table = document.getElementById("diffOutTable");
		var row = table.insertRow(rowIndex+1);
		return row;
	}
	
	function commentPanel(row, rowIndex, cellIndex){
		var draft = structuralDiff.commentPanel.createDraftTitle();
		var textarea = structuralDiff.commentPanel.createTextArea().show();
		var paragraph = structuralDiff.commentPanel.createParagraph().hide();
		var buttons = structuralDiff.commentPanel.createButton('on',rowIndex);

		var cell1 = row.insertCell(0);
		cell1.setAttribute('class', 'lineNumber');
		var cell2 = row.insertCell(1);
		cell2.setAttribute('class', 'commentPanelBGColor');
		var cell3 = row.insertCell(2);
		cell3.setAttribute('class', 'lineNumber');
		var cell4 = row.insertCell(3);
		cell4.setAttribute('class', 'commentPanelBGColor');

		if (cellIndex === 1) {
			draft.appendTo(cell2);
			textarea.appendTo(cell2);
			paragraph.appendTo(cell2);
			buttons.appendTo(cell2);
		} else {
			draft.appendTo(cell4);
			textarea.appendTo(cell4);
			paragraph.appendTo(cell4);
			buttons.appendTo(cell4);
		}
	}
	
	return {
		createTextArea: function() {
			var textarea = $('<textarea/> ').attr({
				rows : 4,
				cols : 40
			}).hide();
			return textarea;
	      },
	      
	    createHiddenElem: function () {
	    	var hiddenElem = $('<input>').attr('type','hidden');
	    	return hiddenElem;
	    },

		createDraftTitle: function () {
			var draft = $('<div/>').attr("class", "draftmessage").text("(Draft)");
			return draft;
		},

		createParagraph: function () {
			var paragraph = $('<p/>').attr("class", "paragraph");
			return paragraph;
		},

		createButton: function (flag, rowIndex) {
			var buttonPanel = $('<div/>').attr("class", "commentPanelButtons");
			
			if (flag === 'published') {
				var replyButton = $('<Button/>').attr("class", "panelButton").attr(
						"onclick", "structuralDiff.commentPanel.createReplyPanel(event)").text("Reply");
				replyButton.appendTo(buttonPanel);
				return buttonPanel;
			}
			
			var saveButton = $('<Button/>').attr("class", "panelButton").attr(
					"onclick", "structuralDiff.saveReviewerComment(event,"+ rowIndex +")")
					.text("Save").hide();
			var editButton = $('<Button/>').attr("class", "panelButton").attr(
					"onclick", "structuralDiff.commentPanel.editReviwerComment(event)").text("Edit");
			var cancelButton = $('<Button/>').attr("class", "panelButton").attr(
					"onclick", "structuralDiff.commentPanel.cancel(event)").text("Cancel").hide();
			var discardButton = $('<Button/>').attr("class", "panelButton").attr(
					"onclick", "structuralDiff.deleteTableRow(event,"+ rowIndex +")").text("Discard")
					.hide();

			if (flag === 'on') {
				saveButton.show();
				editButton.hide();
				cancelButton.hide();
				discardButton.show();
			}

			saveButton.appendTo(buttonPanel);
			editButton.appendTo(buttonPanel);
			cancelButton.appendTo(buttonPanel);
			discardButton.appendTo(buttonPanel);
			return buttonPanel;
		},

		createCommentPanel: function (e) {
			var editableFlag = commentPanelCell.getEditableFlag();
			if (editableFlag === "true") {
				commentPanelCell.setEditableFlag("false");
				var rowIndex;
				var row = e.target.parentNode;
				var thList =  $(row).find('th');
				var cellIndex = e.target.cellIndex;
				if( cellIndex == 1){
					rowIndex = thList[0].textContent;
					if(rowIndex==""){
						rowIndex = thList[1].textContent;
					}
				} else {
					rowIndex = thList[1].textContent;
					if(rowIndex==""){
						rowIndex = thList[0].textContent;
					}
				}
				var row = insertTRow(row.rowIndex);
				commentPanel(row, rowIndex, cellIndex);
			}
		},
		
		createReplyPanel: function (e) {
			var editableFlag = commentPanelCell.getEditableFlag();
			if (editableFlag === "true") {
				commentPanelCell.setEditableFlag("false");
				var row = e.target.parentNode.parentNode.parentNode;
				var rowIndex = $(row).find('input[type=hidden]').text();
				var cellIndex = e.target.parentNode.parentNode.cellIndex;
				var row = insertTRow(row.rowIndex);
				commentPanel(row, rowIndex, cellIndex);
			}
		},

		cancel: function (e) {
			var tableColumn = e.target.parentNode.parentNode;
			var element = tableColumn.getElementsByTagName('textarea');
			$(element[0]).hide();

			var p = tableColumn.getElementsByTagName('p');
			$(p[0]).show();

			var divTag = e.target.parentNode;
			var buttons = divTag.getElementsByTagName('button');
			$(buttons[0]).hide();
			$(buttons[1]).show();
			$(buttons[2]).hide();
			$(buttons[3]).hide();
			commentPanelCell.setEditableFlag("true");
		},

		editReviwerComment: function (e) {
			var tableColumn = e.target.parentNode.parentNode;
			var element = tableColumn.getElementsByTagName('textarea');
			$(element[0]).show();
			var p = tableColumn.getElementsByTagName('p');
			$(p[0]).hide();
			element[0].value = p[0].innerHTML;

			var divTag = e.target.parentNode;
			var buttons = divTag.getElementsByTagName('button');
			$(buttons[0]).show();
			$(buttons[1]).hide();
			$(buttons[2]).show();
			$(buttons[3]).show();
			commentPanelCell.setEditableFlag("false");
		}

	};
})();

var commentPanelCell = (function () {
	var editableFlag = "true";
	return {
		getEditableFlag: function () {
			return editableFlag;
		},
		
		setEditableFlag: function (boolValue) {
			editableFlag = boolValue;
		}
	};
})();
