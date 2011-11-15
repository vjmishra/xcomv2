
function funDivOpenClose(selectId,txtAreaId){
 var selectBox = document.getElementById(selectId);
 var txtArea = document.getElementById(txtAreaId);
 for(i=0;i<selectBox.options.length;i++){
	  if(selectBox.selectedIndex == selectBox.options.length-1)
		txtArea.style.display="block";
	  else
	    txtArea.style.display="none";
	}	}

function check()
{ 
 
if(document.getElementById("returnsph").value=="")
    {
        alert('Please enter Your Ph No.');
        document.getElementById("returnsph").focus();
        return false;    
    }
 
 	
	var email=document.getElementById("returns-email").value;
    len=email.length;
    var no=0;
    for(var i=0;i<len;i++)
    {
        if(email.charAt(i)=="@" || email.charAt(i)==",")
        {
            //alert(i);
            no=no+1;
            //alert(no);
        }
    }
    var dat=Date();
    //alert(dat);
    document.getElementById("txtstran").value=dat;
    if (email == 0 || email=="" )
    {
        alert("Please enter your email address!");
        document.getElementById("returns-email").focus();
        return false;
    }
    else if (email.indexOf("@") < 0)
    {
        alert("Please enter valid Email address!");
        document.getElementById("returns-email").focus();
        return false;
    }
    // Email Checker
    else if (email.indexOf(".") < 0)
    {
        alert("Incorrect email address. Please re-enter!");
        document.getElementById("returns-email").focus();
        return false;
    }
    // Email Checker
    else if (email.indexOf(" ") >= 0)
    {
        alert("Incorrect email address. Please re-enter!");
        document.getElementById("returns-email").focus();
        return false;
    }
    else if (len > 50)
    {
        //length1=email.length;
        //alert(length1);
        alert("Email should not exceed more than 50 characters!");
        document.getElementById("returns-email").focus();
        return false;
    }
    else if (no >= 2)
    {
        //length1=email.length;
        //alert(length1);
        alert("Please do not enter more than one email address!");
        document.getElementById("returns-email").focus();
        return false;
    }
	
	if(document.getElementById("returnsqty").value=="")
    {
        alert('Please enter Qty to Return');
        document.getElementById("returnsqty").focus();
        return false;    
    }
	
	if(document.getElementById("reasonreturn").value=="")
    {
        alert('Please Select Reason to Return');
        document.getElementById("reasonreturn").focus();
        return false;    
    }
	
	if(document.getElementById("returnsqty2").value=="")
    {
        alert('Please enter Qty to Return');
        document.getElementById("returnsqty2").focus();
        return false;    
    }
	
		if(document.getElementById("reasonreturn2").value=="")
    {
        alert('Please Select Reason to Return');
        document.getElementById("reasonreturn2").focus();
        return false;    
    }
	
   
    document.getElementById("CMS_form").action="act_contactus.php";
    document.getElementById("CMS_form").submit();
    return true;
}
function formatPhone(AddingText,objTxtBox)
{
    y=objTxtBox.value;
    abc=/-/gi;
    rep=y.replace(abc,0)
    if (isNaN(rep))
    {
        ab=/(a*)(b*)(c*)(d*)(e*)(f*)(g*)(h*)(i*)(j*)(k*)(l*)(m*)(n*)(o*)(p*)(q*)(r*)(s*)(t*)(u*)(v*)(w*)(x*)(y*)(z*)/gi;
        re=y.replace(ab,"")
        objTxtBox.value="";
        strMsg=AddingText+" Should be numeric";
        intCheckError=1;
        alert(strMsg);
        objTxtBox.focus();
    }
}

function clearr()
{
document.getElementById("txtstran").value="";
}
