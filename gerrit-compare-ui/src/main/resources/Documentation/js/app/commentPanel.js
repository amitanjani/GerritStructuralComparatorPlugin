structuralDiff.commentPanel = (function () {
	
	"use strict";
	
	return {
		createTextArea: function() {
			var textarea = $('<textarea/> ').attr({
				rows : 4,
				cols : 40
			}).hide();
			return textarea;
	      },

		createDraftTitle: function () {
			var draft = $('<div/>').attr("class", "draftmessage").text("(Draft)");
			return draft;
		},

		createParagraph: function () {
			var paragraph = $('<p/>').attr("class", "paragraph");
			return paragraph;
		},

		createButton: function (flag) {
			var buttonPanel = $('<div/>').attr("class", "commentPanelButtons");
			var saveButton = $('<Button/>').attr("class", "panelButton").attr(
					"onclick", "structuralDiff.saveReviewerComment(event)")
					.text("Save").hide();
			var editButton = $('<Button/>').attr("class", "panelButton").attr(
					"onclick", "structuralDiff.commentPanel.editReviwerComment(event)").text("Edit");
			var cancelButton = $('<Button/>').attr("class", "panelButton").attr(
					"onclick", "structuralDiff.commentPanel.cancel(event)").text("Cancel").hide();
			var discardButton = $('<Button/>').attr("class", "panelButton").attr(
					"onclick", "structuralDiff.deleteTableRow(event)").text("Discard")
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
			var table = document.getElementById("diffOutTable");
			var rowIndex = e.target.parentNode.rowIndex;
			var cellIndex = e.target.cellIndex;
			var row = table.insertRow(rowIndex);

			var draft = structuralDiff.commentPanel.createDraftTitle();
			var textarea = structuralDiff.commentPanel.createTextArea().show();
			var paragraph = structuralDiff.commentPanel.createParagraph().hide();
			var buttons = structuralDiff.commentPanel.createButton('on');

			var cell1 = row.insertCell(0);
			cell1.setAttribute('class', 'lineNumber');
			var cell2 = row.insertCell(1);
			cell2.setAttribute('class', 'commentPanelBGColor');
			var cell3 = row.insertCell(2);
			cell3.setAttribute('class', 'lineNumber');
			var cell4 = row.insertCell(3);
			cell4.setAttribute('style', 'commentPanelBGColor');

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
		}

	};
})();
