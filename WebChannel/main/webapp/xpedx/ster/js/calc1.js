// JavaScript Document
 

function calc()

{

	if (isNaN(document.calculator.pagew.value))

		{

			width = 0;

			document.calculator.pagew.value = '';

		}

	else

		{

			width = document.calculator.pagew.value;

		}

		

	if (isNaN(document.calculator.pageh.value))

		{

			height = 0;

			document.calculator.pageh.value = '';

		}

	else

		{

			height = document.calculator.pageh.value;

		}

	if (isNaN(document.calculator.weight.value))

		{

			weight = 0;

			document.calculator.weight.value = '';

		}

	else

		{

			weight = document.calculator.weight.value;

		}

	if (isNaN(document.calculator.pages.value))

		{

			pages = 0;

			document.calculator.pages.value = '';

		}

	else

		{

			pages = document.calculator.pages.value;

		}

	if (isNaN(document.calculator.pieces.value))

		{

			pieces = 0;

			document.calculator.pieces.value = '';

		}

	else

		{

			pieces = document.calculator.pieces.value;

		}

	if (isNaN(document.calculator.waste.value))

		{

			waste = 0;

			document.calculator.waste.value = '';

		}

	else

		{

			waste = 1 + (document.calculator.waste.value / 100);

		}

	basis_size = document.calculator.size.value

	result = ((width * height)/ basis_size ) * (weight / 1000) * pages * pieces * waste;

	result = Math.ceil(result * 100);

	result = result / 100
	//usd-3634239 Prashant Pandey/EDS
	//if ((obj=MM_findObj('Resultsall'))!=null) with (obj)

	//innerHTML = unescape('Paper Estimate: ' + result + ' pounds');
	document.all['Resultsall'].innerHTML='Paper Estimate: ' + result + ' pounds';
	

}



function MM_findObj(n, d) { //v3.0

  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {

    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}

  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];

  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document); return x;

}



function MM_setTextOfLayer(objName,x,newText) { //v3.0

  if ((obj=MM_findObj(objName))!=null) with (obj)

   // if (navigator.appName=='Netscape') {document.write(unescape(newText)); document.close();}

    //else 

	 innerHTML = unescape(newText);

}  