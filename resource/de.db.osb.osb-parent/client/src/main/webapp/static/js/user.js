function toUpperFirst(text){
	var first = text.substring(0,1);
	var tail = text.substring(1);
	return first.toUpperCase() + tail;
}

function autoFillOut(){
	if($('userId').value == 0)
		$('email').value = strReplace($('firstName').value) + '.' + ($('middleInitial').value != '' ? $('middleInitial').value.toUpperCase() + '.' : '') + strReplace($('lastName').value) + '@deutschebahn.com';
	if($('changeLogin') != null && $('changeLogin').checked == true)
		$('loginName').value = strReplace($('firstName').value) + $('middleInitial').value.toUpperCase() + strReplace($('lastName').value);
}
		
function strReplace(text){   
	if (text == null) return null;
    if (text.length == 0) return text;
	for (var i=0; i<7; i++)
		{
		if (i==0)
			{
			var searchText = 'ä';
			var replaceText = 'ae';
			}
		else if (i==1)
			{
			var searchText = 'ö';
			var replaceText = 'oe';
			}
		else if (i==2)
			{
			var searchText = 'ü';
			var replaceText = 'ue';
			}
		else if (i==3)
			{
			var searchText = 'Ä';
			var replaceText = 'Ae';
			}
		else if (i==4)
			{
			var searchText = 'Ö';
			var replaceText = 'Oe';
			}
		else if (i==5)
			{
			var searchText = 'Ü';
			var replaceText = 'Ue';
			}
		else if (i==6)
			{
			var searchText = 'ß';
			var replaceText = 'ss';
			}
           var pos = text.indexOf(searchText, 0);
           while (pos >= 0)
  	        	{
      	        text = text.substring(0, pos) + replaceText + text.substring(pos + 1);
          		pos = text.indexOf(searchText, pos + 2);
			}
		}
		var first = text.substring(0,1);
		var tail = text.substring(1);
		text = first.toUpperCase() + tail;
	return text;
}