var isChanged = false;

function setChangesFlag(){
    isChanged = true;
}

function checkChanges(strLink, strForm){
    if(isChanged == true){
        if(confirm("Es wurden Werte geändert, sollen diese gespeichert werden?")){
            document.getElementById(strForm).submit();
        }
        else window.location.href = strLink;
    }
    else window.location.href = strLink;
}

