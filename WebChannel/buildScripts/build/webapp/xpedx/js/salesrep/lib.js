
function copyToClipBoard(sContents)
{
window.clipboardData.setData("Text", sContents);
}




//Value vanishes from text box

		function inputOnFocus(element, name)
		{
			
			if(element.value == name)
			{
				element.value='';
				
			}
		}

//Value re-appears in text box

		function inputOnBlur(element, name)
		{
			if(element.value=='')
			{
				element.value = name;
			}	
		}
 