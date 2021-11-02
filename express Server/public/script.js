
const socket = io()
const videoGrid = document.getElementById('video-grid')
const localVideo = document.getElementById('localVideo')


let localStream =null;
const myPeer = new Peer();


var textBox = document.querySelector(".textBox") // html 정리내용 박스에 적힌 문자 받아옴
var titleBox = document.querySelector(".titleBox") // html 회의 제목 박스에 적힌 문자 받아옴
var nickNameBox = document.querySelector(".nick") // html 닉네임 박스에 적힌 문자 받아옴


var userId; // 방입장시 생성되는 UUID값을 사용하기 위한 변수


const myVideo = document.createElement('video')
myVideo.className='localVideo'
myVideo.muted = true

let peers = {}
navigator.mediaDevices.getUserMedia({
  video: true,
  audio: true
}).then(stream => {
  
localStream = stream // 음소거 버튼 사용위해 내 스트림을 localStream으로 지정

  myVideo.srcObject = stream
  myVideo.addEventListener('loadedmetadata', () => {
  myVideo.play()
  })
  localVideo.append(myVideo)

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%==================> 브라우저 닫기 눌렀을때 실행되는 로직
window.addEventListener("beforeunload", function (event) {

  event.returnValue = "";

  var url = document.location.href; // 현재 url 가져옴

  var roomSection = url.substring(url.indexOf(':3000/')+6) // url에서 room UUID 값 가져옴

  // 스프링으로 데이터 넘기는 부분 json 사용
  fetch('https://192.168.0.27:8080/saveData',{ // =============================================> url 변경시 같이 변경( IP 변경되면 이부분도 변경해주어야 하는곳)
    method: 'post',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      nickName:nickNameBox.value,
      roomUUID:roomSection,
      text:textBox.value,
      userUUID : userId,
      title:titleBox.value
    })
  })
  .catch(error => console.log(error))
});
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

  myPeer.on('call', call => {
    call.answer(stream)

    const video = document.createElement('video')
    call.on('stream', userVideoStream => {
      addVideoStream(video, userVideoStream);
    });
  });

  socket.on('user-connected', userId => {
    console.log("new user connected", userId)
    connectToNewUser(userId, stream)
  })
}).catch(error=>{
  console.log("Error: ",error)
})

socket.on('user-disconnected', userId => {
  if (peers[userId]) peers[userId].close()
 
})


  myPeer.on('open', id => {
    socket.emit('join-room', ROOM_ID, id)
    this.userId = id; // 외부에서 사용자 UUID 값 사용하기위해 userId전역 변수에 id값 할당 
    
  })

// 음소거 버튼 만들기 위해 작성
var audio_button = document.createElement("audio_button");
audio_button.appendChild(document.createTextNode("Toggle hold"));

audio_button.audio_onclick = function(){
  stream.getAudioTracks()[0].enabled =!(stream.getAudioTracks[0].enabled);
}



function connectToNewUser(userId, stream) {
  const call = myPeer.call(userId, stream)
  const video = document.createElement('video')
  call.on('stream', userVideoStream => {
    addVideoStream(video, userVideoStream)
  })
  call.on('close', () => {
    video.remove()
  })

  peers[userId] = call
}

function addVideoStream(video, stream) {
  video.srcObject = stream
  video.addEventListener('loadedmetadata', () => {
    video.play()
  })
  videoGrid.append(video)
  video.className='video-grid'

}

// 음소거 on off 를 위해 작성
var a =0;
function toggleMute() {
  for (let index in localStream.getAudioTracks()) {
      localStream.getAudioTracks()[index].enabled = !localStream.getAudioTracks()[index].enabled
    
      if(a%2==1){
        img1.src="./on.png";
        a++;
      }else{
        img1.src="./Off.png";
        a++;
      }
      
      
      muteButton.className = localStream.getAudioTracks()[index].enabled ? "button primary": "button"
      // muteButton.innerText = localStream.getAudioTracks()[index].enabled ? "음소거(on)" : "음소거(off)"
  }
}
