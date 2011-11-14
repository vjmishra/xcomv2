
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

////////////////////////////////////////////////////////////
function check2()
{ 
 
if(document.getElementById("ccname").value=="")
    {
        alert('Please enter Your Company Name.');
        document.getElementById("ccname").focus();
        return false;    
    }

if(document.getElementById("cname").value=="")
    {
        alert('Please enter Your Contact Name');
        document.getElementById("cname").focus();
        return false;    
    }
	
if(document.getElementById("ccity").value=="")
    {
        alert('Please enter Your City Name');
        document.getElementById("ccity").focus();
        return false;    
    }
 
if(document.getElementById("cstate").value=="")
    {
        alert('Please enter Your State/Province Name');
        document.getElementById("cstate").focus();
        return false;    
    } 

if(document.getElementById("czipcode").value=="")
    {
        alert('Please enter Your Postal Code');
        document.getElementById("czipcode").focus();
        return false;    
    } 

if(document.getElementById("cphone").value=="")
    {
        alert('Please enter Your Phone No.');
        document.getElementById("cphone").focus();
        return false;    
    } 
	
	var email=document.getElementById("cmail").value;
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
        document.getElementById("cmail").focus();
        return false;
    }
    else if (email.indexOf("@") < 0)
    {
        alert("Please enter valid Email address!");
        document.getElementById("cmail").focus();
        return false;
    }
    // Email Checker
    else if (email.indexOf(".") < 0)
    {
        alert("Incorrect email address. Please re-enter!");
        document.getElementById("cmail").focus();
        return false;
    }
    // Email Checker
    else if (email.indexOf(" ") >= 0)
    {
        alert("Incorrect email address. Please re-enter!");
        document.getElementById("cmail").focus();
        return false;
    }
    else if (len > 50)
    {
        //length1=email.length;
        //alert(length1);
        alert("Email should not exceed more than 50 characters!");
        document.getElementById("cmail").focus();
        return false;
    }
    else if (no >= 2)
    {
        //length1=email.length;
        //alert(length1);
        alert("Please do not enter more than one email address!");
        document.getElementById("cmail").focus();
        return false;
    }
	 
	 
	
		 if(document.getElementById("csubj").value=="")
    {
        alert('Please Select Your subject');
        document.getElementById("csubj").focus();
        return false;    
    }
	
	if(document.getElementById("cQuest").value=="")
    {
        alert('Please lease Your Question/Comments');
        document.getElementById("cQuest").focus();
        return false;    
    }
   
    document.getElementById("contact_form").action="../../index.html";
    document.getElementById("contact_form").submit();
    return true;
}
/////////////////////////////////////////////////////////// 
function check3()
{ 
 
if(document.getElementById("rcontact").value=="")
    {
        alert('Please enter Your Contact Name.');
        document.getElementById("rcontact").focus();
        return false;    
    }

if(document.getElementById("raddress1").value=="")
    {
        alert('Please enter Your Address 1');
        document.getElementById("raddress1").focus();
        return false;    
    }
	
if(document.getElementById("rcity").value=="")
    {
        alert('Please enter Your City Name');
        document.getElementById("rcity").focus();
        return false;    
    }
 
if(document.getElementById("rstate").value=="")
    {
        alert('Please enter Your State/Province Name');
        document.getElementById("rstate").focus();
        return false;    
    } 

if(document.getElementById("rzipcode").value=="")
    {
        alert('Please enter Your Postal Code');
        document.getElementById("rzipcode").focus();
        return false;    
    } 

if(document.getElementById("rphone").value=="")
    {
        alert('Please enter Your Phone No.');
        document.getElementById("rphone").focus();
        return false;    
    } 
	
	var email=document.getElementById("rmail").value;
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
        document.getElementById("rmail").focus();
        return false;
    }
    else if (email.indexOf("@") < 0)
    {
        alert("Please enter valid Email address!");
        document.getElementById("rmail").focus();
        return false;
    }
    // Email Checker
    else if (email.indexOf(".") < 0)
    {
        alert("Incorrect email address. Please re-enter!");
        document.getElementById("rmail").focus();
        return false;
    }
    // Email Checker
    else if (email.indexOf(" ") >= 0)
    {
        alert("Incorrect email address. Please re-enter!");
        document.getElementById("rmail").focus();
        return false;
    }
    else if (len > 50)
    {
        //length1=email.length;
        //alert(length1);
        alert("Email should not exceed more than 50 characters!");
        document.getElementById("rmail").focus();
        return false;
    }
    else if (no >= 2)
    {
        //length1=email.length;
        //alert(length1);
        alert("Please do not enter more than one email address!");
        document.getElementById("rmail").focus();
        return false;
    }
	 
	if(document.getElementById("rFedEx").value=="")
    {
        alert('Please provide your UPS or FedEx number. ');
        document.getElementById("rFedEx").focus();
        return false;    
    } 
	
	if(document.getElementById("rmfg1").value=="")
    {
        alert('Please lease Your Mfg. Item');
        document.getElementById("rmfg1").focus();
        return false;    
    } 
	if(document.getElementById("rManufacturer1").value=="")
    {
        alert('Please Enter Product Manufacturer');
        document.getElementById("rManufacturer1").focus();
        return false;    
    }
	
	if(document.getElementById("rDescription1").value=="")
    {
        alert('Please enter Your Product Description');
        document.getElementById("rDescription1").focus();
        return false;    
    } 
	
	if(document.getElementById("rdiscription").value=="")
    {
        alert('Please Enter Your product Discription under paper tab');
        document.getElementById("rsubj").focus();
        return false;    
    }
	if(document.getElementById("rQty").value=="")
    {
        alert('Please Enter Your Qty under paper tab');
        document.getElementById("rQty").focus();
        return false;    
    }

   
    document.getElementById("request_form").action="../../index.html";
    document.getElementById("request_form").submit();
    return true;
}
///////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////// 
function check4()
{ 
  

if(document.getElementById("calcwidth").value=="")
    {
        alert('Please enter Roll Weight');
        document.getElementById("calcwidth").focus();
        return false;    
    } 
	   

if(document.getElementById("calcheight").value=="")
    {
        alert('Please enter Page height');
        document.getElementById("calcheight").focus();
        return false;    
    } 
	
if(document.getElementById("calcpages").value=="")
    {
        alert('Please enter Number of pages');
        document.getElementById("calcpages").focus();
        return false;    
    } 
	
if(document.getElementById("calcpieces").value=="")
    {
        alert('Please enter Number of pieces');
        document.getElementById("calcpieces").focus();
        return false;    
    } 	
	
if(document.getElementById("calcWaste").value=="")
    {
        alert('Please enter Waste');
        document.getElementById("calcWaste").focus();
        return false;    
    } 
	
if(document.getElementById("calcBweight").value=="")
    {
        alert('Please enter Basis weight');
        document.getElementById("calcBweight").focus();
        return false;    
    } 	
	
if(document.getElementById("calcBsize").value=="")
    {
        alert('Please enter Basis weight');
        document.getElementById("calcBsize").focus();
        return false;    
    }
	
 
	
    document.getElementById("form_calc").action="../../index.html";
    document.getElementById("form_calc").submit();
    return true;
}

function calcPhone(AddingText,objTxtBox)
{
    y=objTxtBox.value;
    abc=/-/gi;
    rep=y.replace(abc,0)
    if (isNaN(rep))
    {
        ab=/(a*)(b*)(c*)(d*)(e*)(f*)(g*)(h*)(i*)(j*)(k*)(l*)(m*)(n*)(o*)(p*)(q*)(r*)(s*)(t*)(u*)(v*)(w*)(x*)(y*)(z*)/gi;
        re=y.replace(ab,"")
        objTxtBox.value="";
        strMsg=AddingText+" Should be numeric and Do not enter numbers with commas";
        intCheckError=1;
        alert(strMsg);
        objTxtBox.focus();
    }
}

function clearr()
{
document.getElementById("txtstran").value="";
}
/////////////////////////////////////////////////////////// 
function check5()
{ 
  

if(document.getElementById("calcwidth").value=="")
    {
        alert('Please enter Given Size (Length)');
        document.getElementById("calcwidth").focus();
        return false;    
    } 
	   

if(document.getElementById("calcheight").value=="")
    {
        alert('Please enter Given Size (Width)');
        document.getElementById("calcheight").focus();
        return false;    
    } 
	
if(document.getElementById("calcpages").value=="")
    {
        alert('Please enter Basis Weight');
        document.getElementById("calcpages").focus();
        return false;    
    } 
	
if(document.getElementById("calcpieces").value=="")
    {
        alert('Please enter Basic Size Length )');
        document.getElementById("calcpieces").focus();
        return false;    
    } 	
	
if(document.getElementById("calcWaste").value=="")
    {
        alert('Please enter Basic Size Length Width');
        document.getElementById("calcWaste").focus();
        return false;    
    } 
	 
 
	
    document.getElementById("form_Ream").action="../../index.html";
    document.getElementById("form_Ream").submit();
    return true;
}

function calcPhone(AddingText,objTxtBox)
{
    y=objTxtBox.value;
    abc=/-/gi;
    rep=y.replace(abc,0)
    if (isNaN(rep))
    {
        ab=/(a*)(b*)(c*)(d*)(e*)(f*)(g*)(h*)(i*)(j*)(k*)(l*)(m*)(n*)(o*)(p*)(q*)(r*)(s*)(t*)(u*)(v*)(w*)(x*)(y*)(z*)/gi;
        re=y.replace(ab,"")
        objTxtBox.value="";
        strMsg=AddingText+" Should be numeric and Do not enter numbers with commas";
        intCheckError=1;
        alert(strMsg);
        objTxtBox.focus();
    }
}

function clearr()
{
document.getElementById("txtstran").value="";
}
///////////////////////////////////////////////////////////

$(document).ready(function() {
		$(document).pngFix();
		$('#purposeofmails').bt({
			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-content',
			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
			fill: '#ebebeb',
			cssStyles: {color: 'black'},
			padding: 20,
			spikeLength: 10,
  			spikeGirth: 15,
			cornerRadius: 6,
			shadow: true,
			shadowOffsetX: 0,
			shadowOffsetY: 3,
			shadowBlur: 3,
			shadowColor: 'rgba(0,0,0,.4)',
			shadowOverlap: false,
			strokeWidth: 1,
  			strokeStyle: '#FFFFFF',
			noShadowOpts:     {strokeStyle: '#969696'},
			positions: ['top']
		});
		$('#purposeofmails2').bt({
			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-content',
			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
			fill: '#ebebeb',
			cssStyles: {color: 'black'},
			padding: 20,
			spikeLength: 10,
  			spikeGirth: 15,
			cornerRadius: 6,
			shadow: true,
			shadowOffsetX: 0,
			shadowOffsetY: 3,
			shadowBlur: 3,
			shadowColor: 'rgba(0,0,0,.4)',
			shadowOverlap: false,
			strokeWidth: 1,
  			strokeStyle: '#FFFFFF',
			noShadowOpts:     {strokeStyle: '#969696'},
			positions: ['top']
		});
		$('#purposeofmails3').bt({
			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-content',
			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
			fill: '#ebebeb',
			cssStyles: {color: 'black'},
			padding: 20,
			spikeLength: 10,
  			spikeGirth: 15,
			cornerRadius: 6,
			shadow: true,
			shadowOffsetX: 0,
			shadowOffsetY: 3,
			shadowBlur: 3,
			shadowColor: 'rgba(0,0,0,.4)',
			shadowOverlap: false,
			strokeWidth: 1,
  			strokeStyle: '#FFFFFF',
			noShadowOpts:     {strokeStyle: '#969696'},
			positions: ['top']
		});
		$('#purposeofmails4').bt({
			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-content',
			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
			fill: '#ebebeb',
			cssStyles: {color: 'black'},
			padding: 20,
			spikeLength: 10,
  			spikeGirth: 15,
			cornerRadius: 6,
			shadow: true,
			shadowOffsetX: 0,
			shadowOffsetY: 3,
			shadowBlur: 3,
			shadowColor: 'rgba(0,0,0,.4)',
			shadowOverlap: false,
			strokeWidth: 1,
  			strokeStyle: '#FFFFFF',
			noShadowOpts:     {strokeStyle: '#969696'},
			positions: ['top']
		});

	});
