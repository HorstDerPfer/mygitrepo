/* Vergleicht 2 Datumsangaben ( Datum1 < Datum2 ? true : false) *************************************/
function isDate1BeforeDate2(date1, date2) {
	var date1int = 0;
	var date2int = 0;
	date1 = document.getElementById(date1);
	date2 = document.getElementById(date2);
	date1int = parseInt(date1.value.substring(6,10) + date1.value.substring(3,5) + date1.value.substring(0,2));
	date2int = parseInt(date2.value.substring(6,10) + date2.value.substring(3,5) + date2.value.substring(0,2));
	if(date1int < date2int) {
		return true;
	} else {
		return false;
	}
}