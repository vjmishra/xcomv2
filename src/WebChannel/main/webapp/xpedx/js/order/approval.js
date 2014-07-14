
function openHistoryPopup(panelId)
{
    DialogPanel.show(panelId);
    svg_classhandlers_decoratePage();
}

function setLightBoxPosition(panelId) {
    var d = document.getElementById(panelId);
    d.style.top=100;
    d.style.left=300;
 }

function  openNotePanel(id, actionValue){

    DialogPanel.show(id);
    svg_classhandlers_decoratePage();
	 if(actionValue == "Accept")
	     document.forms["approval"].elements["ApprovalAction"].value = "1300";
	 if(actionValue == "Reject")
	     document.forms["approval"].elements["ApprovalAction"].value = "1200";
}

