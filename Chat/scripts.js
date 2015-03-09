var username = "";
var toWhomClick = null;
var choise=false;

function run(){
	var appContainer = document.getElementById("buttonSend");
	appContainer.addEventListener('click', onAddButtonClick);
    
    var inputName = document.getElementById("userName");
    inputName.addEventListener('focusout', inputUserName);
    
    // it is better to define element IDs and fetch them using getElementById if you have no need in managing 
    // several elements in the same way or select dynamically created nodes
    document.getElementsByClassName("glyphicon")[0].style.display = "none";
    document.getElementsByClassName("glyphicon")[1].style.display = "none";
    document.getElementsByClassName("glyphicon")[0].addEventListener('click', clickGlyphEdit);
    document.getElementsByClassName("glyphicon")[1].addEventListener('click', clickGlyphDel);

    var text=document.getElementById("tableMessage");
    text.addEventListener('keypress',ifTextInput);
    
    connectionServer(false);
}

function onAddButtonClick(){
    var inputName = document.getElementById("userName");
    while(username.length === 0 || username==null)
    {
    	// while (!username) { ... }
       /* inputName.focus();
        return;*/
        username = prompt("Input your username!");
    }
    inputName.value = username;
    
	var todoText = document.getElementById('tableMessage');
	// define regexp in separate variable please, e.g. var myRegexp = ...
    if(!/\S/.test(todoText.value)){
        todoText.value = '';
        return;
    }
    
    if(toWhomClick==null)
	addTodo(todoText.value);
    else{
     if(choise==true)
     {
         toWhomClick.childNodes[1].innerText=todoText.value;
         toWhomClick.style.background='#FFFFE0';
         editable(false);
         toWhomClick=null;
         choise=false;
     }
     else{
            addTodo(todoText.value);
     }
    }
	todoText.value = '';
} 

function addTodo(value) {
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

function createItem(text, row){
    
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
   
    td1.classList.add("col-name");
    td2.classList.add("col-text");
   
    row.appendChild(td1);
    row.appendChild(td2);
    
    td1.innerHTML = username;
    td2.innerText = text; 

}

function inputUserName(){
    
    var inputName = document.getElementById("userName");
    username = inputName.value;
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
    choise=!choise;
    var text=document.getElementById('tableMessage');
    text.value=toWhomClick.childNodes[1].innerText;
}
function clickGlyphDel(){
   toWhomClick.parentNode.removeChild(toWhomClick);
    editable(false);
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
