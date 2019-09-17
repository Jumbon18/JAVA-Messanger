const socket = io();
// Elements
const form = document.querySelector('form');
const formInput = document.querySelector('.form-input');
const formButton = document.querySelector('button');
const locationButton = document.getElementById('send-location');
const divMessages  = document.getElementById('messages');
const divLocation =  document.getElementById('location');

//Options
const {username,room}  =  Qs.parse(location.search,{ignoreQueryPrefix:true});

const autoscroll = ()=>{
const newMessage = divMessages.lastElementChild;

const newMessageStyles = getComputedStyle(newMessage);
const newMessageMargin = parseInt(newMessageStyles.marginBottom);
const newMessageHeight = newMessage.offsetHeight + newMessageMargin;

const visibleHeight = divMessages.offsetHeight;

const containerHeight = divMessages.scrollHeight;

const scrollOffset = divMessages.scrollTop + visibleHeight;

if(containerHeight - newMessageHeight <= scrollOffset){
    divMessages.scrollTop = divMessages.scrollHeight;

}

};

socket.on('message',(message)=>{
   console.log(message);
    const markUp = `
  <div class="message">
                <p>
                    <span class="message__name">{{username}}</span>
                    <span class="message__meta">{{createdAt}}</span>
                </p>
                  <p>{{message}}</p>
</div>`;
    const html = Mustache.render(markUp,{
        username:message.username,
        message:message.text,
        createdAt:moment(message.createdAt).format('H : mm')
    });
        divMessages.insertAdjacentHTML("beforeend", html);
autoscroll();
});

socket.on('roomData',({room,users})=>{
    const markUp = `
    <h2 class="room-title">{{room}}</h2>
    <h3 class="list-title">Users</h3>
    <ul class="users" >
    {{#users}}
    <li>{{username}}</li>
    {{/users}}
</ul>
    `;
    const html  = Mustache.render(markUp,{
        room,users
    });
    document.querySelector('.chat__sidebar').innerHTML = html;

    console.log(room);
    console.log(users);

});

socket.on('locationMessage',(url)=>{
const markUp = `<div>
<p>{{createdAt}} - <a href={{url}}>My current location</a></p>
</div>`;
const html = Mustache.render(markUp,{url:url.url,createdAt:moment(url.createdAt).format('H : mm a')});
divLocation.insertAdjacentHTML("beforeend",html);
autoscroll();
});
form.addEventListener('submit',(e)=>{
    e.preventDefault();

formButton.setAttribute('disabled','disabled');

    const message =document.querySelector('.form-input').value;

   socket.emit('sendMessage',message,(error)=>{
       formButton.removeAttribute('disabled');
       formInput.value = '';
       formInput.focus();

     if(error){
         return console.log(error);
     }
     console.log('The message was delivered');
   });
});

document.getElementById('send-location').addEventListener('click',()=>{
    locationButton.setAttribute('disabled','disabled');
    if(!navigator.geolocation){
return alert('Geolocation is not supported by your browser');
    }
  navigator.geolocation.getCurrentPosition((position)=>{
      socket.emit('sendLocation',{latitude:position.coords.latitude,
      longitude:position.coords.longitude},error=>{
          if(error){
              return console.log(error);
          }
          locationButton.removeAttribute('disabled');
          console.log('Location shared!');
      }); });

});

socket.emit('join',{username,room},(error)=>{
if(error){
    alert(error);
    window.location = '/';
}
});