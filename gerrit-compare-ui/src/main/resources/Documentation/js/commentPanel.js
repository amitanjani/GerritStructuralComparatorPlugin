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
		
		function createParagraph(text) 
		{
			var paragraph =  $('<p/>').attr("class","paragraph").text(text);
			return paragraph;
		}
		
		function createButton() 
		{
			var divTag = $('<div/>').attr("class","commentPanelButtons");
			var saveButton =  $('<Button/>').attr("class","panelButton").attr("onclick","saveReviewerComment(event)").text("Save").hide();
			var editButton =  $('<Button/>').attr("class","panelButton").attr("onclick","editReviwerComment(event)").text("Edit");
			var cancelButton = $('<Button/>').attr("class","panelButton").attr("onclick","cancel(event)").text("Cancel").hide();
			var discardButton =  $('<Button/>').attr("class","panelButton").attr("onclick","deleteTableRow(event)").text("Discard").hide();
			saveButton.appendTo(divTag); 
			editButton.appendTo(divTag); 
			cancelButton.appendTo(divTag); 
			discardButton.appendTo(divTag); 
			return divTag;	    
		}
		
		function displayCommentHolder(e)
		{
			var table=document.getElementById("diffOutTable");
			var rowIndex = e.target.parentNode.rowIndex;
			var cellIndex = e.target.cellIndex;
		    var row=table.insertRow(rowIndex);
		    var textarea = document.createElement('textarea');
		    textarea.setAttribute('rows', '4');
		    textarea.setAttribute('cols', '40');
		    
		    var paragraph = document.createElement('p');
		    paragraph.setAttribute('style', 'padding-bottom: 10px;font-family: sans-serif;');
		    
		    var cell1=row.insertCell(0);
		    cell1.setAttribute('class', 'lineNumber');
		    var cell2=row.insertCell(1);
		    cell2.setAttribute('style', 'background: #e5ecf9');
		    var cell3=row.insertCell(2);
		    cell3.setAttribute('class', 'lineNumber');
		    var cell4=row.insertCell(3);
		    cell4.setAttribute('style', 'background: #e5ecf9');
		    
		    var draft = document.createElement('div');
		    draft.setAttribute('style', 'font-weight: bold;font-family: sans-serif;padding-bottom: 4px;');
		    draft.innerHTML = "(Draft)";
		    
		    var divTag = document.createElement("div");
		    divTag.setAttribute('class', 'commentPanelButtons'); 
		    divTag.setAttribute('style', 'padding-bottom: 5px;');
		    
		    var saveButton = document.createElement("Button");
		    saveButton.innerHTML = "Save";
		    saveButton.setAttribute('class', 'panelButton'); 
		    saveButton.setAttribute('onclick','saveReviewerComment(event)');
		    divTag.appendChild(saveButton);
		    
		    var editButton = document.createElement("Button");
		    editButton.innerHTML = "Edit";
		    editButton.setAttribute('class', 'panelButton');
		    editButton.setAttribute('style', 'display: none;'); 
		    editButton.setAttribute('onclick','editReviwerComment(event)');
		    divTag.appendChild(editButton);
		    
		    var cancelButton = document.createElement("Button");
		    cancelButton.innerHTML = "Cancel";
		    cancelButton.setAttribute('class', 'panelButton'); 
		    cancelButton.setAttribute('style', 'display: none;');
		    cancelButton.setAttribute('onclick','cancel(event)');
		    divTag.appendChild(cancelButton);
		    
		    var discardButton = document.createElement("Button");
		    discardButton.innerHTML = "Discard";
		    discardButton.setAttribute('class', 'panelButton'); 
		    discardButton.setAttribute('onclick','deleteTableRow(event)');
		    divTag.appendChild(discardButton);
		    
		    if( cellIndex == 1 ){
		    	cell2.appendChild(draft);
		        cell2.appendChild(textarea);
		        cell2.appendChild(paragraph);
		        cell2.appendChild(divTag);
		    }else{
		    	cell4.appendChild(draft);
		        cell4.appendChild(textarea);
		        cell4.appendChild(paragraph);
		        cell4.appendChild(divTag);
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