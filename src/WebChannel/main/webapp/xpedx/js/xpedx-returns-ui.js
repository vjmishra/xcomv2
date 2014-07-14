
function funDivOpenClose(selectId,txtAreaId){
 var selectBox = document.getElementById(selectId);
 var txtArea = document.getElementById(txtAreaId);
 for(i=0;i<selectBox.options.length;i++){
	  if(selectBox.selectedIndex == selectBox.options.length-1)
		txtArea.style.display="block";
	  else
	    txtArea.style.display="none";
	}	}

function check(itemSize)
{ 
 
if(document.getElementById("returnsph").value=="")
    {
		alert('Please enter your phone number.');
        document.getElementById("returnsph").focus();
        return false;    
    }
 
 	
	var email=document.getElementById("returnsEmail").value;
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
    //var dat=Date();
    //alert(dat);
    //document.getElementById("txtstran").value=dat;
    if (email == 0 || email=="" )
    {
    	alert("Please enter your email address.");
        document.getElementById("returnsEmail").focus();
        return false;
    }
    else if (email.indexOf("@") < 0)
    {
    	alert("The email address entered is not in the proper format. Please revise and try again.");
        document.getElementById("returnsEmail").focus();
        return false;
    }
    // Email Checker
    else if (email.indexOf(".") < 0)
    {
        alert("Incorrect email address. Please re-enter!");
        document.getElementById("returnsEmail").focus();
        return false;
    }
    // Email Checker
    else if (email.indexOf(" ") >= 0)
    {
        alert("Incorrect email address. Please re-enter!");
        document.getElementById("returnsEmail").focus();
        return false;
    }
    else if (len > 50)
    {
        //length1=email.length;
        //alert(length1);
        alert("Email should not exceed more than 50 characters!");
        document.getElementById("returnsEmail").focus();
        return false;
    }
    else if (no >= 2)
    {
        //length1=email.length;
        //alert(length1);
        alert("Please do not enter more than one email address!");
        document.getElementById("returnsEmail").focus();
        return false;
    }
	var atLeastOneSelected=false;
    for(var i=0;i<itemSize;i++) {
    	var selectedItems="selectedItems[" + i + "]";
    	var checkBox=document.getElementById(selectedItems);
    	if(checkBox.checked){
    		atLeastOneSelected=true;
	    	var returnqty = "returnsqty[" + i + "]";
	    	returnqty=document.getElementById(returnqty).value;
	    	returnqty = Number(returnqty);
	    	var orderLineQuantity="orderLineQuantity[" + i + "]";
	    	orderLineQuantity=document.getElementById(orderLineQuantity).value;
	    	orderLineQuantity = Number(orderLineQuantity);
	    	
	    	if (returnqty == "" || returnqty <= 0
					|| orderLineQuantity < returnqty) {
				alert('Please enter a valid return quantity and try again');
				returnqty = "returnsqty[" + i + "]";
				document.getElementById(returnqty).focus();
				return false;
			}
	    	
	    	var reasonreturn = "reasonreturn[" + i + "]";
	    	
	    	if(document.getElementById(reasonreturn).value=="1")
	        {
	            alert('Please Select Reason to Return');
	            document.getElementById(reasonreturn).focus();
	            return false;    
	        }
	    	
    	}
     }
    if(!atLeastOneSelected){
		alert("Please select at least one item to return.");
		return false;
	}
    
    document.getElementById("returnItemForm").submit();
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

function formatQty(AddingText,objTxtBox)
{
	var qty = document.getElementById(objTxtBox);
    y=qty.value;
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
