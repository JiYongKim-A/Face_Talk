const express = require('express')
const app = express()
const fs = require('fs')
const dotenv = require('dotenv')


dotenv.config();

// https 사용하기 위해 키와 crt option으로 server.js 가동
const options = {
    key: fs.readFileSync('./ca.key'),
    cert: fs.readFileSync('./ca.crt')
  };

const server = require('https').Server(options,app)

const io = require('socket.io')(server)
const {v4:uuidV4} = require('uuid');


var cookieParser =require('cookie-parser');

app.set('view engine','ejs')

app.use(express.static('views'))
app.use(express.static('public'))

app.use(express.json());

app.use(cookieParser());

// :3030으로 접속시 뒤에 uuid 넣어서 redirect 로 밑에 /:room으로 넘어감 
app.get('/',(req, res)=>{
    res.redirect(`/${uuidV4()}`)
})

app.get('/:room', (req, res)=>{

// 여기서 쿠키값의 닉네임 받아옴
// 스프링 쿠키에서 닉네임 넣고 -> 노드js에서 닉네임빼서 사용
// 쿼리 스트링 쓰면 보안적 문제로 쿠키 사용
  var cookNickName = req.cookies.nickName
  res.render('room',{ roomId: req.params.room, nickName:cookNickName, SPRING_SERVER : process.env.SPRING_SERVER})
})


io.on('connection', socket =>{

  
    socket.on('join-room',(roomId, userId)=>{

        console.log("roomId = ",roomId)
        console.log("userId = ",userId)

        socket.join(roomId)
        socket.broadcast.to(roomId).emit('user-connected', userId)
        

      socket.on('disconnect', ()=>{
        socket.broadcast.to(roomId).emit('user-disconnected', userId)
      })  
    })
})

server.listen(process.env.PORT, () => console.log(process.env.PORT+`Port Server is open!!`))
module.exports = app;