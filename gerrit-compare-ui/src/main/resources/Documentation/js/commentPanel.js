		function createTextArea() 
		{
			 var textarea = $('<textarea/> ').attr({rows:4,cols:40}).hide();
			 return textarea;
		}
		
		function createDraftTitle() 
		{
			var draft = $('<div/>').attr("class","draftmessage").text("(Draft)");
		    return draft;
		}
		
		function createParagraph() 
		{
			var paragraph =  $('<p/>').attr("class","paragraph");
			return paragraph;
		}
		
		function createButton(flag) 
		{
			var buttonPanel = $('<div/>').attr("class","commentPanelButtons");
			var saveButton =  $('<Button/>').attr("class","panelButton").attr("onclick","saveReviewerComment(event)").text("Save").hide();
			var editButton =  $('<Button/>').attr("class","panelButton").attr("onclick","editReviwerComment(event)").text("Edit");
			var cancelButton = $('<Button/>').attr("class","panelButton").attr("onclick","cancel(event)").text("Cancel").hide();
			var discardButton =  $('<Button/>').attr("class","panelButton").attr("onclick","deleteTableRow(event)").text("Discard").hide();
			
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
		}
		
		function displayCommentHolder(e)
		{
			var table=document.getElementById("diffOutTable");
			var rowIndex = e.target.parentNode.rowIndex;
			var cellIndex = e.target.cellIndex;
		    var row=table.insertRow(rowIndex);

		    var draft = createDraftTitle();
		    var textarea = createTextArea().show();
		    var paragraph = createParagraph().hide(); 
		    var buttons = createButton('on');
		    
		    var cell1=row.insertCell(0);
		    cell1.setAttribute('class', 'lineNumber');
		    var cell2=row.insertCell(1);
		    cell2.setAttribute('style', 'background: #e5ecf9');
		    var cell3=row.insertCell(2);
		    cell3.setAttribute('class', 'lineNumber');
		    var cell4=row.insertCell(3);
		    cell4.setAttribute('style', 'background: #e5ecf9');
		    
		    if( cellIndex == 1 ){
		    	draft.appendTo(cell2);
		    	textarea.appendTo(cell2);
		    	paragraph.appendTo(cell2);
		    	buttons.appendTo(cell2);
		    }else{
		    	draft.appendTo(cell4);
		    	textarea.appendTo(cell4);
		    	paragraph.appendTo(cell4);
		    	buttons.appendTo(cell4);
		    }
		}


		function cancel(e){
			var tableColumn = e.target.parentNode.parentNode;
			var element = tableColumn.getElementsByTagName('textarea');
			element[0].style.display = "none";
			
			var p = tableColumn.getElementsByTagName('p');
			p[0].style.display = "block";
			
			var divTag = e.target.parentNode;
			var buttons = divTag.getElementsByTagName('button');
			buttons[0].style.display = "none";
			buttons[1].style.display = "inline";
			buttons[2].style.display = "none";
			buttons[3].style.display = "none";
		}

		function editReviwerComment(e){
			var tableColumn = e.target.parentNode.parentNode;
			var element = tableColumn.getElementsByTagName('textarea');
			element[0].style.display = "block";
			
			var p = tableColumn.getElementsByTagName('p');
			p[0].style.display = "none";
			
			element[0].value = p[0].innerHTML;
			
			var divTag = e.target.parentNode;
			var buttons = divTag.getElementsByTagName('button');
			buttons[0].style.display = "inline";
			buttons[1].style.display = "none";
			buttons[2].style.display = "inline";
			buttons[3].style.display = "inline";
		}