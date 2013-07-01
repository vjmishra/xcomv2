
function tbTest_focus(e,o){
if(o.firstTime){return}
o.firstTime=true
o.value=""
}
function tbTests_focus(e,o){ 
if(o.firstTime){return}
o.firstTime=true
o.value=""
}

function Toggle(thediv) {
document.getElementById("div1").style.display = "none";
document.getElementById("div2").style.display = "none";
document.getElementById("div3").style.display = "none";
document.getElementById(thediv).style.display = "block";
} 

function resetPromptForImposCalc () {
	document.getElementById("sheetSizeW").style.borderColor="";	
	document.getElementById("sheetSizeH").style.borderColor="";	
	document.getElementById("trimSizeW").style.borderColor="";
	document.getElementById("trimSizeH").style.borderColor="";
	
}
function checkCalculatorValues() { 
	/*Start- Jira 3109 */
	resetPromptForImposCalc (); 
	var errorflag= false;
	var valuelesserror =  false;
	if(document.getElementById("sheetSizeW").value=="" || document.getElementById("sheetSizeH").value=="" || document.getElementById("sheetSizeW").value < 1 || document.getElementById("sheetSizeH").value < 1 )
    {   
		if(document.getElementById("sheetSizeW").value=="" || document.getElementById("sheetSizeW").value < 1)
        	{
        	document.getElementById("sheetSizeW").style.borderColor="#FF0000";
        	document.getElementById("sheetSizeW").focus();
            }
        if(document.getElementById("sheetSizeH").value=="" || document.getElementById("sheetSizeH").value < 1)
    	{
    	document.getElementById("sheetSizeH").style.borderColor="#FF0000";
    	document.getElementById("sheetSizeH").focus();
    	}
        if(document.getElementById("sheetSizeW").value=="" || document.getElementById("sheetSizeH").value=="")
        	errorflag= true; 
        else if(document.getElementById("sheetSizeW").value < 1 || document.getElementById("sheetSizeH").value < 1)        	
        	valuelesserror =  true;		
    }
	
	if( document.getElementById("trimSizeW").value=="" || document.getElementById("trimSizeH").value=="" || document.getElementById("trimSizeW").value < 1 || document.getElementById("trimSizeH").value < 1)
    {
        if(document.getElementById("trimSizeW").value=="" || document.getElementById("trimSizeW").value < 1)
    	{
    	document.getElementById("trimSizeW").style.borderColor="#FF0000";
    	document.getElementById("trimSizeW").focus();
    	//return false; 
    	}
	    if(document.getElementById("trimSizeH").value=="" || document.getElementById("trimSizeH").value < 1)
		{
		document.getElementById("trimSizeH").style.borderColor="#FF0000";
		document.getElementById("trimSizeH").focus();
		//return false; 
		}     
	    if(document.getElementById("trimSizeW").value=="" || document.getElementById("trimSizeH").value=="")
        	errorflag= true; 
        else if(document.getElementById("trimSizeW").value < 1 || document.getElementById("trimSizeH").value < 1)        	
        	valuelesserror =  true;	
    }
	if(errorflag){
		alert('Required fields missing. Please review and try again.');
		return false;
	}
	if(valuelesserror){
		alert('Sheet Size and Trim Size must be greater than 0.');
		return false;
	}
	/*End- Jira 3109 */
	if(document.getElementById("gripperWidth").value < 0)
    {
		alert('Gripper Width can not be less than 0');
        document.getElementById("gripperWidth").focus();
        return false;    
    }
	   

	if(document.getElementById("colorBarWidth").value < 0)
    {
        alert('Color Bar Width can not be less than 0');
        document.getElementById("colorBarWidth").focus();
        return false;    
    } 
	
	if(document.getElementById("sideGuide").value < 0)
    {
        alert('Side Guide can not be less than 0');
        document.getElementById("sideGuide").focus();
        return false;    
    } 
	
	if(document.getElementById("gutter").value < 0)
    {
        alert('Gutter can not be less than 0');
        document.getElementById("gutter").focus();
        return false;    
    }

	 
    document.getElementById("impCalculatorForm").submit();
    return true;
}



function funDivOpenClose(selectId,txtAreaId){
 var selectBox = document.getElementById(selectId);
 var txtArea = document.getElementById(txtAreaId);
 for(i=0;i<selectBox.options.length;i++){
	  if(selectBox.selectedIndex == selectBox.options.length-1)
		txtArea.style.display="block";
	  else
	    txtArea.style.display="none";
	}	}
	

 function show(obj) {
      no = obj.options[obj.selectedIndex].value;
      count = obj.options.length;
      for(i=1;i<count;i++)
        document.getElementById('myDiv'+i).style.display = 'none';
      if(no>0)
        document.getElementById('myDiv'+no).style.display = 'block';
    }

 function shows(obj) {
      no = obj.options[obj.selectedIndex].value;
      count = obj.options.length;
      for(i=1;i<count;i++)
        document.getElementById('mySer'+i).style.display = 'none';
      if(no>0)
        document.getElementById('mySer'+no).style.display = 'block';
    }

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

function clearValues() {
	document.impCalculatorForm.reset();
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
        strMsg=AddingText+" Enter numeric values only, please do not include special characters or commas.";
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
    abc=/^\.|-/gi;
    rep=y.replace(abc,"0.");
    if (isNaN(rep))
    {
        ab=/(a*)(b*)(c*)(d*)(e*)(f*)(g*)(h*)(i*)(j*)(k*)(l*)(m*)(n*)(o*)(p*)(q*)(r*)(s*)(t*)(u*)(v*)(w*)(x*)(y*)(z*)/gi;
        re=y.replace(ab,"")
        objTxtBox.value="";
        strMsg=AddingText+" Enter numeric values only, please do not include special characters or commas.";
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

/*$(document).ready(function() {
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

	});*/


///////////////////////////////////////////////////////
//
//$(document).ready(function() {
//		$(document).pngFix();
//		$('#purposeofmails-u').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content1',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['right']
//		});
//		$('#purposeofmails-u03').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content-up',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['right']
//		});
//		$('#purposeofmails-u1').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content1',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		$('#purposeofmails-u2').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content2',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		$('#purposeofmails-u3').bt({
//			ajaxPath:'../tool-tips/purpose-of-mail.html div#tool-tip-user-content3',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		$('#purposeofmails-u4').bt({
//			ajaxPath:'../tool-tips/purpose-of-mail.html div#tool-tip-user-content4',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		$('#purposeofmails-u5').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content5',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		$('#purposeofmails-u6').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content6',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		$('#purposeofmails-u7').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content7',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		$('#purposeofmails-u8').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content8',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		$('#purposeofmails-u9').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content9',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['left']
//		});
//		$('#purposeofmails-u10').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content10',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		$('#purposeofmails-u11').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-user-content11',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['right']
//		});
//
//		$('#purposeofmails-al1').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-al-content11',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//
//		$('#purposeofmails-al2').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-al-content12',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//
//		$('#purposeofmails-al3').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-al-content13',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		$('#purposeofmails-sp1').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sp-content1',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		
//		$('#purposeofmails-sp2').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sp-content2',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		
//		$('#purposeofmails-sp3').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sp-content3',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['right']
//		});
//		
//		$('#purposeofmails-sp4').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sp-content4',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		$('#purposeofmails-sp5').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sp-content5',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		$('#purposeofmails-sp6').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sp-content6',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		$('#purposeofmails-sp7').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sp-content7',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		$('#purposeofmails-sp8').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sp-content8',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		$('#purposeofmails-sp9').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sp-content9',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//		 
//		$('#purposeofmails-sla').bt({
//			ajaxPath: '../tool-tips/purpose-of-mail.html div#tool-tip-sla-content',
//			ajaxError: "There was a problem getting this content. Here's what we know: <em>%error</em>.",
//			fill: '#ebebeb',
//			cssStyles: {color: 'black'},
//			padding: 20,
//			spikeLength: 10,
//  			spikeGirth: 15,
//			cornerRadius: 6,
//			shadow: true,
//			shadowOffsetX: 0,
//			shadowOffsetY: 3,
//			shadowBlur: 3,
//			shadowColor: 'rgba(0,0,0,.4)',
//			shadowOverlap: false,
//			strokeWidth: 1,
//  			strokeStyle: '#FFFFFF',
//			noShadowOpts:     {strokeStyle: '#969696'},
//			positions: ['top']
//		});
//		
//	});
//


////////////////////////////////////////////<!-- This script is based on the javascript code of Roman Feldblum (web.developer@programmer.net) -->
<!-- Original script : http://javascript.internet.com/forms/format-phone-number.html -->
<!-- Original script is revised by Eralper Yilmaz (http://www.eralper.com) -->
<!-- Revised script : http://www.kodyaz.com/content/HowToAutoFormatTelephoneNumber.aspx -->

var zChar = new Array(' ', '-', '.');
var maxphonelength = 12;
var phonevalue1;
var phonevalue2;
var cursorposition;

function ParseForNumber1(object){
	phonevalue1 = ParseChar(object.value, zChar);
}
function ParseForNumber2(object){
	phonevalue2 = ParseChar(object.value, zChar);
}

function backspacerUP(object,e) { 
	if(e){ 
		e = e 
	} else {
		e = window.event 
	} 
	if(e.which){ 
		var keycode = e.which 
	} else {
		var keycode = e.keyCode 
	}

	ParseForNumber1(object)

	if(keycode >= 48){
		ValidatePhone(object)
	}
}

function backspacerDOWN(object,e) { 
	if(e){ 
		e = e 
	} else {
		e = window.event 
	} 
	if(e.which){ 
		var keycode = e.which 
	} else {
		var keycode = e.keyCode 
	}
	ParseForNumber2(object)
} 

function GetCursorPosition(){
    
	var t1 = phonevalue1;
	var t2 = phonevalue2;
	var bool = false
    for (i=0; i<t1.length; i++)
    {
    	if (t1.substring(i,1) != t2.substring(i,1)) {
    		if(!bool) {
    			cursorposition=i
    			bool=true
    		}
    	}
    }
}

function ValidatePhone(object){
	
	var p = phonevalue1
	
	p = p.replace(/[^\d]*/gi,"") 
	if (p.length < 3) {
		object.value=p
	} else if(p.length==3){
		pp=p;
		d4=p.indexOf('')
		d5=p.indexOf(' ')
		if(d4==-1){
			pp=""+pp;
		}
		if(d5==-1){
			pp=pp+" ";
		}
		object.value = pp;
	} else if(p.length>3 && p.length < 7){
		p ="" + p;	
		l30=p.length;
		p30=p.substring(0,3);
		p30=p30+" "

		p31=p.substring(3,l30);
		pp=p30+p31;

		object.value = pp;	
		
	} else if(p.length >= 7){
		p ="" + p;	
		l30=p.length;
		p30=p.substring(0,4);
		p30=p30+" "
		
		p31=p.substring(3,l30);
		pp=p30+p31;
		
		l40 = pp.length;
		p40 = pp.substring(1,8);
		p40 = p40 + "-"
		
		p41 = pp.substring(8,l40);
		ppp = p40 + p41;
		
		object.value = ppp.substring(0, maxphonelength);
	}
	
	GetCursorPosition()
	
	if(cursorposition >= 0){
		if (cursorposition == 0) {
			cursorposition = 2
		} else if (cursorposition <= 2) {
			cursorposition = cursorposition + 1
		} else if (cursorposition <= 5) {
			cursorposition = cursorposition + 2
		} else if (cursorposition == 6) {
			cursorposition = cursorposition + 2
		} else if (cursorposition == 7) {
			cursorposition = cursorposition + 4
			e1=object.value.indexOf(')')
			e2=object.value.indexOf('-')
			if (e1>-1 && e2>-1){
				if (e2-e1 == 4) {
					cursorposition = cursorposition - 1
				}
			}
		} else if (cursorposition < 11) {
			cursorposition = cursorposition + 3
		} else if (cursorposition == 11) {
			cursorposition = cursorposition + 1
		} else if (cursorposition >= 12) {
			cursorposition = cursorposition
		}

        var txtRange = object.createTextRange();
        txtRange.moveStart( "character", cursorposition);
		txtRange.moveEnd( "character", cursorposition - object.value.length);
        txtRange.select();
    }

}

function ParseChar(sStr, sChar)
{
    if (sChar.length == null) 
    {
        zChar = new Array(sChar);
    }
    else zChar = sChar;
    
    for (i=0; i<zChar.length; i++)
    {
        sNewStr = "";
    
        var iStart = 0;
        var iEnd = sStr.indexOf(sChar[i]);
    
        while (iEnd != -1)
        {
            sNewStr += sStr.substring(iStart, iEnd);
            iStart = iEnd + 1;
            iEnd = sStr.indexOf(sChar[i], iStart);
        }
        sNewStr += sStr.substring(sStr.lastIndexOf(sChar[i]) + 1, sStr.length);
        
        sStr = sNewStr;
    }
    
    return sNewStr;
}


////////////////////////////////////////////
