var username = "";
var toWhomClick = null;
var choise=false;

var uniqueId = function() {
	var date = Date.now();
	var random = Math.random();

	return Math.floor(date * random).toString();
};

var theHistory = function(Name, booleanChanges, messageText) {
	return {
		personeName: Name,
		intChoose: booleanChanges,
        message : messageText,
		id: uniqueId()
	};
};

var HistoryList = [];

function run(){
    
	var appContainer = document.getElementById("buttonSend");
	appContainer.addEventListener('click', onAddButtonClick);
    
    var inputName = document.getElementById("userName");
    inputName.addEventListener('focusout', inputUserName);
    
    document.getElementsByClassName("glyphicon")[0].style.display = "none";
    document.getElementsByClassName("glyphicon")[1].style.display = "none";
    document.getElementsByClassName("glyphicon")[0].addEventListener('click', clickGlyphEdit);
    document.getElementsByClassName("glyphicon")[1].addEventListener('click', clickGlyphDel);

    var text=document.getElementById("tableMessage");
    text.addEventListener('keypress',ifTextInput);
    
    HistoryList = restore();
    if(HistoryList==null){
        HistoryList=[];
        HistoryList[0]='';
    }
    username=HistoryList[0];
    createAllHistory(HistoryList);
    
       
    connectionServer(false);
}
function createAllHistory(allHistory) {
   
	for(var i = 1; i < allHistory.length; i++)
		addRowsToTable(allHistory[i]);
    
    var inputName = document.getElementById("userName");
    inputName.value = allHistory[0];
}

function store(listToSave) {

	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}
	localStorage.setItem("Messages", JSON.stringify(listToSave));
}

function restore() {
	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}
	var item = localStorage.getItem("Messages");
	return item && JSON.parse(item);
}


function onAddButtonClick(){
    var inputName = document.getElementById("userName");
    while(username.length === 0 || username==null)
    {
       /* inputName.focus();
        return;*/
        username = prompt("Input your username!");
    }
    inputName.value = username;
    
	var todoText = document.getElementById('tableMessage');
    if(!/\S/.test(todoText.value)){
        todoText.value = '';
        return;
    }
    var task=theHistory(username, 0, todoText.value);
    if(toWhomClick==null)
    {
        HistoryList.push(task);
        store(HistoryList);

        addRowsToTable(task);
        
    }
    else{
     if(choise==true)
     {
        
         var idTemp=toWhomClick.getAttribute('id');
             for(var i=1;i<HistoryList.length;i++)
                 {
                     if(idTemp == HistoryList[i].id)
                         {
                            if(HistoryList[i].intChoose==2)
                                {
                                    toWhomClick.style.background='#FFFFE0';
                                    editable(false);
                                    toWhomClick=null;
                                    choise=false;
                                    todoText.value = '';
                                    return;
                                    }
            HistoryList[i].intChoose = 1;
            HistoryList[i].message = todoText.value;
                                }
                           }
         toWhomClick.childNodes[2].innerText=todoText.value;
         toWhomClick.childNodes[1].innerHTML='<i class="glyphicon glyphicon-pencil col-stage"></i>';
         toWhomClick.style.background='#FFFFE0';
         store(HistoryList);
         
         editable(false);
         toWhomClick=null;
         choise=false;
     }
     else{
         HistoryList.push(task);
        store(HistoryList);

         addRowsToTable(task);
     }
    }
	todoText.value = '';
} 

function addRowsToTable(value) {
    var scrolling=document.getElementsByClassName('my-table')[0];
    var scrollIsEnd=false;
    var heightTable = scrolling.clientHeight;
    if(scrolling.scrollHeight-scrolling.scrollTop<=heightTable+50)
        scrollIsEnd=true;

    var Mtable = document.getElementsByClassName('table')[0];
    var row = Mtable.insertRow(-1); 
    row.style.background = "#FFFFE0";
    row.addEventListener('click', clickMessage);

    createItem(value, row);
    if(scrollIsEnd==true)
        scrolling.scrollTop = scrolling.scrollHeight;

}

function createItem(value, row){
    
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    var tdGlyph = document.createElement("td");
   
    td1.classList.add("col-name");
   // tdGlyph.classList.add("col-stage");
    td2.classList.add("col-text");
   
    row.appendChild(td1);
    row.appendChild(tdGlyph);
    row.appendChild(td2);
    //
    row.setAttribute("id", value.id);
    
    td1.innerHTML = value.personeName;
    td2.innerText = value.message; 

    if(value.intChoose ==2) //2 - delete
    {
        tdGlyph.innerHTML='<i class="glyphicon glyphicon-trash col-stage"></i>';
    }
    if(value.intChoose ==1)
    {
        tdGlyph.innerHTML='<i class="glyphicon glyphicon-pencil col-stage"></i>';
    }
}

function inputUserName(){
    
    var inputName = document.getElementById("userName");
    username = inputName.value;
    HistoryList[0]=username;
    store(HistoryList);
}

function clickMessage(){
    if(toWhomClick == null){
       toWhomClick=this;
        toWhomClick.style.background='#E6E6FA';
        editable(true);
    }
   else{
   toWhomClick.style.background='#FFFFE0';
       editable(false);
       if(toWhomClick!=this){
       toWhomClick=this;
          toWhomClick.style.background='#E6E6FA';
           editable(true);
       }
       else
           toWhomClick=null;
   }
}
function editable(flag){
    if(flag==true)
    {
        document.getElementsByClassName("glyphicon")[0].style.display = "inline-block";
         document.getElementsByClassName("glyphicon")[1].style.display = "inline-block";
    }
    else{
        document.getElementsByClassName("glyphicon")[0].style.display = "none";
         document.getElementsByClassName("glyphicon")[1].style.display = "none";
    }
}
function clickGlyphEdit(){
    choise=true;
    var text=document.getElementById('tableMessage');
    text.value=toWhomClick.childNodes[2].innerText;
}
function clickGlyphDel(){
 
    toWhomClick.childNodes[2].innerHTML="The message was deleted";
    toWhomClick.childNodes[1].innerHTML='<i class="glyphicon glyphicon-trash col-stage"></i>';
    editable(false);
    toWhomClick.style.background='#FFFFE0';
    
    var idTemp = toWhomClick.getAttribute('id');
    
    for(var i=1; i<HistoryList.length; i++)
    {
        if(HistoryList[i].id == idTemp)
        {
            HistoryList[i].intChoose = 2;
            HistoryList[i].message="The message was deleted";
            break;
        }
    }
        store(HistoryList);
    
    toWhomClick=null;
}

function ifTextInput(event) {
var key = event.keyCode;
   if (key == 13) {
       if(event.shiftKey){
           var text=document.getElementById('tableMessage');
           var caretPosition=getCaretPosition(text);         
           text.value=text.value.slice(0,caretPosition)+'\n'+text.value.slice(caretPosition);
           setCaretPosition(text,caretPosition+1);
       }
       else{
        onAddButtonClick();
       }
        event.preventDefault();
    }
}
function getCaretPosition (text) {
    var caretPosition = 0;
    if (document.selection) {
        var select = document.selection.createRange ();
        select.moveStart ('character', -text.value.length);
        caretPosition = select.text.length;
    }
    else if (text.selectionStart || text.selectionStart == '0')
        caretPosition = text.selectionStart;
    return caretPosition;
}

function setCaretPosition(text, position){
    if(text.setSelectionRange)
    {
        text.focus();
        text.setSelectionRange(position, position);
    }
    else if (text.createTextRange) {
  var range = text.createTextRange();
        range.collapse(true);
  range.moveEnd('character', position);
  range.moveStart('character', position);
  range.select();
 }
}
function connectionServer(flag){
    var label=document.getElementById('connect');
    if(flag==true){
         label.classList.remove('label-success');
            label.classList.add('label-success');
            label.textContent="Connected";
    }
    else{
        label.classList.remove('label-success');
        label.classList.add('label-danger');
        label.textContent="Disconnected";

    }
}