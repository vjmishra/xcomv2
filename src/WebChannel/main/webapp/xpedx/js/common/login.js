var isguestuser = false;
var remUser = "";

function setFocusForLoginPanel(){
    if(isguestuser=="true"){
        if(remUser == null || remUser == ""){
            if(document.getElementById("DisplayUserID")!=null){
                document.getElementById("DisplayUserID").focus();
            }
        }
        else{
            if(document.getElementById("DisplayUserID")!=null){
                document.getElementById("Password").focus();
            }
        }
    }
}