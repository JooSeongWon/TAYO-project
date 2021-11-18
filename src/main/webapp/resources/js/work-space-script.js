//모달 배경 설정
modalBackGroundDomObj.style.position = 'fixed';
modalBackGroundDomObj.style.left = '0';
modalBackGroundDomObj.style.top = '0';
modalBackGroundDomObj.style.width = '100vw';
modalBackGroundDomObj.style.height = '100vh';

//노드
const miceBtn = document.getElementById('mice');
const camBtn = document.getElementById('cam');
const screenBtn = document.getElementById('screen');
const chatInput = document.querySelector('#chat');
const screen = document.querySelector('.screen');

const navBtn = document.querySelector('.fa-chevron-right');
const exitBtn = document.querySelector('.exit');
const bottomMenu = document.querySelector('.bottom-menu');
const chatHistory = document.querySelector('.chat-history');
const chatHistoryContent = document.querySelector('.chat-history__content');
const chatHistoryBtn = document.querySelector('.fa-arrow-up');

const myCam = document.querySelector('.my-cam');
const myCamWrap = document.querySelector('.my-cam-wrap');
const myCamFullScreenBtn = document.querySelector('.my-cam-full');

const shutter = document.createElement('div');

//z인덱스
const INDEX_MY_AVATAR = '50';
const INDEX_OTHER_AVATAR = '49';
const INDEX_SPEECH_BUBBLE = '53';
const INDEX_SYSTEM_UI = '100';
const INDEX_BOARD = '99';


//웹소켓 상수
const RELAY_CHAT = "chat";
const RELAY_MOVE = "move";
const RELAY_ENTER = "enter";
const SYSTEM_DUPLICATE = "duplicate";
const RELAY_JOIN = "join";
const RELAY_LEAVE = "leave";
const RELAY_SET_MUTE = "setMute";
const RELAY_SET_LIVE = "setLive";
const RELAY_OFFER = "offer";
const RELAY_ANSWER = "answer";
const RELAY_ICE = "ice";

let isMouseOverOnHistory;
let socket;

let myAudioStream;
let myVideoStream;

let miceOn = false;
let camOn = false;
let screenOn = false;

let useCamera = false;
let useAudio = false;

let preChatText = '';

//유저맵
const userMap = {};


//시스템UI 인덱스 설정
navBtn.style.zIndex = INDEX_SYSTEM_UI;
exitBtn.style.zIndex = INDEX_SYSTEM_UI;
bottomMenu.style.zIndex = INDEX_SYSTEM_UI;


//초기화
init();


async function onMyCamStream() {
        offMyVideoStream();

        //스트림 새로 생성 및 새 오퍼
        myVideoStream = await navigator.mediaDevices.getUserMedia({video: true});
        for (let user in userMap) {
            if (userMap[user] && userMap[user].inMediaRange) {
                userMap[user].newPeerConnection();
            }
        }

    //스트림 종료 이벤트
    myVideoStream.getVideoTracks()[0].onended = handleCamClick;
}

async function onMyScreenStream() {
    //새오퍼 생성
    offMyVideoStream();

    //스트림 새로 생성 및 새 오퍼
    try {
        myVideoStream = await navigator.mediaDevices.getDisplayMedia({video: true});
    } catch (e) {throw e;}
    if(!myVideoStream) {
        throw new Error('화면선택 거부');
    }

    for (let user in userMap) {
        if (userMap[user] && userMap[user].inMediaRange) {
            userMap[user].newPeerConnection();
        }
    }

    //스트림 종료 이벤트
    myVideoStream.getVideoTracks()[0].onended = handleScreenClick;
}

async function onMyAudioStream() {
    //트랙만 오픈
    myAudioStream.getTracks().forEach(track => {
        track.enabled = true;
    });

    myAudioStream.getAudioTracks()[0].onended = handleMiceClick;
}

function offMyVideoStream() {
    if (myVideoStream) {
        if (myVideoStream.getTracks()) {
            myVideoStream.getTracks().forEach(track => track.stop());
        }
        myVideoStream = null;
    }
}

function offMyAudioStream() {
    myAudioStream.getAudioTracks().forEach(track => {
        track.enabled = false;
    });
}

function openFullscreen(video) {
    if (video.requestFullscreen) {
        video.requestFullscreen();
    } else if (video.webkitRequestFullscreen) { /* Safari */
        video.webkitRequestFullscreen();
    } else if (video.msRequestFullscreen) { /* IE11 */
        video.msRequestFullscreen();
    }
}


miceBtn.addEventListener('click', handleMiceClick);
camBtn.addEventListener('click', handleCamClick);
screenBtn.addEventListener('click', handleScreenClick);
myCamFullScreenBtn.addEventListener('click', () => {
    openFullscreen(myCam)
});


async function handleMiceClick() {
    if(!useAudio) return;

    if (!miceOn) { //켬
        try {
            await onMyAudioStream();
        } catch (e) { //마이크 사용 거부
            return;
        }

    } else { //끔
        await offMyAudioStream();
    }

    miceBtn.classList.toggle('active');
    miceOn = !miceOn;
    myAvatar.setMute(!miceOn);
    sendMessage(RELAY_SET_MUTE, !miceOn);
}

async function handleCamClick() {
    if(!useCamera) return;

    let sendServer = true;
    if (!camOn) {//켬
        if (screenOn) {
            await handleScreenClick();
            sendServer = false;
        }

        camBtn.classList.toggle('fa-video');
        camBtn.classList.toggle('fa-spinner');
        camBtn.style.cursor = 'none';
        try {
            await onMyCamStream();
        } catch (e) { //화면공유 거절
            camBtn.classList.toggle('fa-spinner');
            camBtn.classList.toggle('fa-video');
            camBtn.style.cursor = 'pointer';
            return;
        }

        myCam.srcObject = myVideoStream;
        camBtn.classList.toggle('fa-spinner');
        camBtn.classList.toggle('fa-video');
        camBtn.style.cursor = 'pointer';
        myAvatar.setOnAir(true);
    } else { //끔
        await offMyVideoStream(true);
        myCam.srcObject = null;
        myAvatar.setOnAir(false);
    }

    camBtn.classList.toggle('active');
    camOn = !camOn;
    if (sendServer) sendMessage(RELAY_SET_LIVE, camOn);

    if (camOn && !myCamWrap.classList.contains('active')) {
        myCamWrap.classList.add('active');
    }
    if (!camOn && myCamWrap.classList.contains('active')) {
        myCamWrap.classList.remove('active');
    }
}

async function handleScreenClick() {
    let sendServer = true;
    if (!screenOn) {//켬
        if (camOn) {
            await handleCamClick();
            sendServer = false;
        }

        try {
            await onMyScreenStream();
        } catch (e) {
            return;
        }

        myCam.srcObject = myVideoStream;
        myAvatar.setOnAir(true);
    } else {//끔
        await offMyVideoStream();
        myCam.srcObject = null;
        myAvatar.setOnAir(false);
    }

    screenBtn.classList.toggle('active');
    screenOn = !screenOn;
    if (sendServer) sendMessage(RELAY_SET_LIVE, screenOn);

    if (screenOn && !myCamWrap.classList.contains('active')) {
        myCamWrap.classList.add('active');
    }
    if (!screenOn && myCamWrap.classList.contains('active')) {
        myCamWrap.classList.remove('active');
    }
}


/* 아바타 */
class Avatar {
    constructor(user) {
        this.userId = user.id;
        this.userName = user.name;
        this.userProfile = user.profile;
        this.x = user.x;
        this.y = user.y;

        this.isMe = (user.id === myId);
        this.isOther = !this.isMe;
        this.isLive = user.live;
        this.isMute = user.mute;
    }

    insertMyAvatar() {
        this.avatar = document.querySelector('.avatar.user');
        this.profile = document.querySelector('.avatar.user img');
        this.profileWrap = document.querySelector('.user .img-wrap')
        this.name = document.querySelector('.avatar.user > .avatar__name');
        this.speechBubble = document.querySelector('.speech-bubble-wrap.user');
        this.speechBubbleText = document.querySelector('.speech-bubble-wrap.user > .speech-bubble');
        this.onAir = document.querySelector('.user .on-air');
        this.mute = document.querySelector('.user .mute');
        this.range = document.querySelector('.range');

        this.profileWrap.addEventListener('click', () => {
            this.range.classList.toggle('active');
        })

        this.avatar.style.zIndex = INDEX_MY_AVATAR;
        this.speechBubble.style.zIndex = INDEX_SPEECH_BUBBLE;
    }

    render(isJoin) {
        //아바타
        const avatar = document.createElement('div');
        avatar.classList.add('avatar');
        avatar.style.zIndex = INDEX_OTHER_AVATAR;
        avatar.style.left = `${this.x}px`;
        avatar.style.top = `${this.y}px`;

        const profileWrap = document.createElement('div');
        profileWrap.classList.add('img-wrap');

        const profile = document.createElement('img');
        if (this.userProfile) {
            profile.src = `/img/${this.userProfile}`;
        } else {
            profile.src = '/resources/img/no-profile.png';
        }
        profile.classList.add('avatar__img');

        const mute = document.createElement('i');
        mute.classList.add('fas');
        mute.classList.add('fa-microphone-slash');
        mute.classList.add('mute');
        if (this.isMute) {
            mute.classList.add('active');
        }

        const onAir = document.createElement('i');
        onAir.classList.add('fas');
        onAir.classList.add('fa-video');
        onAir.classList.add('on-air');
        if (this.isLive) {
            onAir.classList.add('active');
        }

        const name = document.createElement('div');
        name.classList.add('avatar__name');
        name.innerText = this.userName;

        profileWrap.appendChild(profile);
        profileWrap.appendChild(mute);
        profileWrap.appendChild(onAir);
        avatar.appendChild(profileWrap);
        avatar.appendChild(name);

        this.avatar = avatar;
        this.name = name;
        this.profileWrap = profileWrap;
        this.profile = profile;
        this.onAir = onAir;
        this.mute = mute;

        //말풍선
        const speechBubble = document.createElement('div');
        speechBubble.classList.add('speech-bubble-wrap');
        speechBubble.classList.add('active');
        speechBubble.style.zIndex = INDEX_SPEECH_BUBBLE;
        speechBubble.style.left = `${this.x}px`;
        speechBubble.style.top = `${this.y - 15}px`;

        const speechBubbleText = document.createElement('div');
        speechBubbleText.classList.add('speech-bubble');

        const speechBubbleArrowDown = document.createElement('div');
        speechBubbleArrowDown.classList.add('arrow-down');

        speechBubble.appendChild(speechBubbleText);
        speechBubble.appendChild(speechBubbleArrowDown);

        this.speechBubble = speechBubble;
        this.speechBubbleText = speechBubbleText;

        //화면에 캐릭터 추가
        screen.appendChild(avatar);
        screen.appendChild(speechBubble);

        //오디오 추가
        const audio = document.createElement('audio');
        audio.controls = false;
        audio.autoplay = true;
        audio.style.position = 'fixed';
        audio.style.left = '100';
        audio.style.top = '100';
        audio.style.opacity = '0';

        document.body.appendChild(audio);
        this.audio = audio;

        if (this.isLive) this.profileWrap.classList.add('cursor');
        this.checkMediaRange();

        if (this.inMediaRange && isJoin) { //새 접속자가 범위내에 있음
            this.createConnection();
        }
    }

    talk(text) {
        //다시 넣어서 최신대화를 위로 끌어올리기
        if (this.speechTimeOut) {
            clearTimeout(this.speechTimeOut);
        }

        screen.removeChild(this.speechBubble);
        if (this.speechBubble.classList.contains('active')) {
            this.speechBubble.classList.remove('active');
        }

        this.speechBubbleText.innerText = text;

        screen.appendChild(this.speechBubble);
        this.speechTimeOut = setTimeout(() => this.speechBubble.classList.add('active'), 3000);

        //채팅기록에 추가
        const doScroll = !(isMouseOverOnHistory && chatHistoryContent.offsetHeight + chatHistoryContent.scrollTop < chatHistoryContent.scrollHeight);

        const history = document.createElement('div');
        history.classList.add('history');
        if (this.isMe) {
            history.classList.add('user');
        } else {

            const historyTitle = document.createElement('div');
            historyTitle.classList.add('history__title');

            const historyProfile = document.createElement('img');
            historyProfile.classList.add('history__profile');
            if (this.userProfile) {
                historyProfile.src = `/img/${this.userProfile}`;
            } else {
                historyProfile.src = '/resources/img/no-profile.png';
            }

            const historyUserName = document.createElement('div');
            historyUserName.classList.add('history__user-name');
            historyUserName.innerText = this.userName;

            historyTitle.appendChild(historyProfile);
            historyTitle.appendChild(historyUserName);
            history.appendChild(historyTitle);
        }

        const historyContent = document.createElement('div');
        historyContent.classList.add('history__content');
        historyContent.innerText = text;

        history.appendChild(historyContent);

        chatHistoryContent.appendChild(history);
        if (doScroll) chatHistoryContent.scroll(0, chatHistoryContent.scrollHeight);
    }

    move(x, y) {
        let posX = x;
        let posY = y;

        if (this.isMe) {
            posX = Math.max(-15, Math.min(1855, x - 60));
            posY = Math.max(0, Math.min(1030, y - 40));

            this.range.style.left = `${posX + 40}px`;
            this.range.style.top = `${posY + 22}px`;
        }

        this.avatar.style.left = `${posX}px`;
        this.avatar.style.top = `${posY}px`;
        this.speechBubble.style.left = `${posX}px`;
        this.speechBubble.style.top = `${posY - 15}px`;
        this.x = posX;
        this.y = posY;

        if (this.isMe) {
            for (let userId in userMap) {
                if (userMap[userId]) {
                    userMap[userId].checkMediaRange();
                }
            }
        } else {
            this.checkMediaRange();

            //다른사람이 진입했을때 기존 연결이 없다면
            if (this.inMediaRange && !this.peerConnection) {
                this.createConnection();
            }
        }
    }

    createConnection() {
        console.log(`새로운 피어 연결! 대상[${this.userName}]`);//test

        this.peerConnection = createPeerConnection(this.userId);
        this.peerConnection.onicecandidate = data => iceHandle(data, this.userId);
        this.peerConnection.addEventListener('addstream', data => this.setAudio(data, this.userId));

        //오퍼 생성, 전송
        let rtcSessionDescriptionInitPromise = createOffer(this.peerConnection);
        rtcSessionDescriptionInitPromise.then(offer => {
            console.log('새로 생성한 오퍼', offer); //test
            sendMessage(RELAY_OFFER, offer, this.userId);
        });
    }


    setOnAir(isLive) {
        if (isLive) {
            if (!this.onAir.classList.contains('active')) {
                this.onAir.classList.add('active');
            }
            if (!this.profileWrap.classList.contains('cursor') && this.isOther) {
                this.profileWrap.classList.add('cursor');
            }

            this.isLive = true;
            return;
        }

        if (this.onAir.classList.contains('active')) {
            this.onAir.classList.remove('active');
        }
        if (this.profileWrap.classList.contains('cursor') && this.isOther) {
            this.profileWrap.classList.remove('cursor');
        }
        this.isLive = false;
    }

    setMute(isMute) {
        if (isMute) {
            if (!this.mute.classList.contains('active')) {
                this.mute.classList.add('active');
            }
        } else {
            if (this.mute.classList.contains('active')) {
                this.mute.classList.remove('active');
            }
        }
        this.isMute = isMute;
    }

    checkMediaRange() {
        //기준좌표
        const myAvatarX = myAvatar.x + 40;
        const myAvatarY = myAvatar.y + 22;
        const thisX = this.x + 40;
        const thisY = this.y + 22;

        const disX = myAvatarX - thisX;
        const dixY = myAvatarY - thisY;

        const distance = Math.sqrt(Math.abs(disX * disX) + Math.abs(dixY * dixY));

        if (distance <= (350 / 2) + 3) {
            if (this.profile.classList.contains('active')) {
                return;
            }

            //새로 진입한 범위내 사용자
            this.profile.classList.add('active');
            this.inMediaRange = true;
        } else {
            if (this.profile.classList.contains('active')) { //떠난 사용자
                //커넥션이 존재하면 삭제
                if (this.peerConnection) {
                    this.peerConnection.close();
                    this.peerConnection = null;
                    console.log("유저이동 피어삭제!"); //test

                    if(this.audio) this.audio.srcObject = null;
                }
                this.profile.classList.remove('active');
                this.inMediaRange = false;
            }
        }
    }

    //오퍼수신
    receiveOffer(offer) {
        if (this.peerConnection) {
            try {
                this.peerConnection.close();
            } catch (e) {
                console.log(e);
            }
        }

        this.peerConnection = createPeerConnection(this.userId);
        this.peerConnection.onicecandidate = data => iceHandle(data, this.userId);
        this.peerConnection.addEventListener('addstream', data => this.setAudio(data, this.userId));

        //answer 생성, 전송
        let rtcSessionDescriptionInitPromise = createAnswer(this.peerConnection, offer);
        rtcSessionDescriptionInitPromise.then(answer => {
            sendMessage(RELAY_ANSWER, answer, this.userId);
        });
    }

    //answer 수신
    receiveAnswer(answer) {
        this.peerConnection.setRemoteDescription(answer);
    }

    //ice 수신
    receiveIce(ice) {
        if (this.peerConnection) {
            console.log('실제로 바듬!');
            this.peerConnection.addIceCandidate(ice);
        }
    }

    newPeerConnection() {
        if (this.peerConnection) {
            this.peerConnection.close;
            this.peerConnection = null;
        }

        this.createConnection();
    }

    setAudio(data, userId) {
        const user = userMap[userId];
        console.log('user', user); //test
        if(data.stream) {

            console.log('비디오트랙들', data.stream.getVideoTracks());
            console.log('오디오트랙들', data.stream.getAudioTracks());
            
            const audioStream = new MediaStream();
            data.stream.getAudioTracks().forEach(track => audioStream.addTrack(track));
            user.audio.srcObject = audioStream;
        }
    }

    remove() {
        userMap[this.userId] = undefined;
        if (this.peerConnection) {
            try {
                this.peerConnection.close();
            } catch (e) {
                console.log(e);
            } finally {
                this.peerConnection = null;
                console.log("유저떠남 피어삭제!"); //test
            }
        }
        if(this.audio){
            document.body.removeChild(this.audio);
        }
        screen.removeChild(this.avatar);
        screen.removeChild(this.speechBubble);
    }
}

//내 캐릭터
const myAvatar = new Avatar({
    id: myId,
    name: myName,
    profile: myProfile,
    x: 30,
    y: 30,
    live: false,
    mute: true
});
myAvatar.insertMyAvatar();


//이동 이벤트
window.oncontextmenu = function (e) {
    if (e.target === screen || isAvatar(e.target)) {
        myAvatar.move(e.pageX, e.pageY);
        sendMessage(RELAY_MOVE, {x: myAvatar.x, y: myAvatar.y});
        return false;
    }
};

//채팅 이벤트
window.document.body.addEventListener('keydown', function (e) {
    if (e.key === 'Enter') {
        if (document.activeElement !== chatInput) {
            chatInput.focus();
        } else {
            //채팅입력 처리
            if (!chatInput.value) {
                chatInput.blur();
                return;
            }
            myAvatar.talk(chatInput.value);
            preChatText = chatInput.value;
            sendMessage(RELAY_CHAT, chatInput.value); //서버전송

            chatInput.value = '';
        }
    }
});

// 이전채팅 이벤트
chatInput.addEventListener('keyup', (e) => {
    if (e.key === 'ArrowUp') {
        chatInput.value = preChatText;
        let len = chatInput.value.length;

        //포커스 맨뒤로 (크로스 브라우저)
        if (chatInput.createTextRange) {
            let range = chatInput.createTextRange();
            range.move('character', len);
            range.select();
        } else if (len.selectionStart) {
            len.setSelectionRange(len, len);
        }

    }
});

/* 우클릭 캐릭터 무시 */
function isAvatar(target) {
    const classList = target.classList;
    return classList.contains('speech-bubble') ||
        classList.contains('avatar') ||
        classList.contains('avatar__img') ||
        classList.contains('img-wrap') ||
        classList.contains('mute') ||
        classList.contains('on-air') ||
        classList.contains('avatar__name') ||
        classList.contains('arrow-down') ||
        classList.contains('speech-bubble-wrap') ||
        classList.contains('my-cam-wrap') ||
        classList.contains('my-cam-title') ||
        classList.contains('range')
}

/* 채팅기록 열기닫기 */
chatHistoryBtn.addEventListener('click', () => {
    chatHistoryBtn.classList.toggle('active');
    chatHistory.classList.toggle('active');
});

//채팅기록 조작 체크
chatHistoryContent.addEventListener('mouseenter', () => {
    isMouseOverOnHistory = true
});
chatHistoryContent.addEventListener('mouseleave', () => {
    isMouseOverOnHistory = false
});


/* 웹 소켓 */

//초기화
async function init() {
    //까만창으로 막기
    shutter.style.position = 'fixed';
    shutter.style.left = '0';
    shutter.style.top = '0';
    shutter.style.width = '100%';
    shutter.style.height = '100%';
    shutter.style.zIndex = '9997';
    shutter.style.backgroundColor = 'rgba(0,0,0,0.3)';

    screen.appendChild(shutter);


    //미디어 초기화
    try {
        myAudioStream = await navigator.mediaDevices.getUserMedia({audio: true});
    } catch (e) {
        console.log(e);
    }
    try {
        myVideoStream = await navigator.mediaDevices.getUserMedia({video: true});
    } catch (e) {
        console.log(e);
    }
    if (myVideoStream) {
        useCamera = true;
        offMyVideoStream(true);
    }
    if (myAudioStream) {
        useAudio = true;
        offMyAudioStream();
    }

    //웹소켓 오픈
    socket = new SockJS('/workspace/relay');

    //이벤트 설정
    socket.onopen = () => callbackOpenSocket(shutter);
    socket.onerror = () => showModal('에러', '서버와 연결이 끊어졌습니다. 재연결을 시도합니다.', () => {
        location.href = `/work-spaces/${roomId}`
    });
    socket.onmessage = receiveMessage;
    socket.onclose = () => showModal('에러', '서버와 연결이 끊어졌습니다. 재연결을 시도합니다.', () => {
        location.href = `/work-spaces/${roomId}`
    });
}


function callbackOpenSocket() {
    sendMessage(RELAY_ENTER);
}

function sendMessage(type, object, receiver) {
    const message = {
        type,
        sender: myId,
        receiver,
        roomId,
        message: object
    };

    socket.send(JSON.stringify(message));
}

function receiveMessage(event) {
    const message = JSON.parse(event.data);

    switch (message.type) {
        //누군가 중복접속
        case SYSTEM_DUPLICATE:
            showModal('중복접속', '다른 브라우저로 접속했습니다.', () => {
                location.href = '/work-spaces'
            });
            break;
        //접속승인
        case RELAY_ENTER:
            const userList = message.message;
            for (const user of userList) {
                const avatar = new Avatar(user);
                avatar.render();
                userMap[avatar.userId] = avatar;
            }
            screen.removeChild(shutter);
            break;
        //새 유저 접속
        case RELAY_JOIN:
            const avatar = new Avatar(message.message);
            avatar.render(true);
            userMap[avatar.userId] = avatar;
            break;
        //접속종료
        case RELAY_LEAVE:
            const leaveUserId = message.sender;
            userMap[leaveUserId].remove();
            userMap[leaveUserId] = null;
            break;
        //채팅
        case RELAY_CHAT:
            const chatUserId = message.sender;
            userMap[chatUserId].talk(message.message);
            break;
        //이동
        case RELAY_MOVE:
            const moveUserId = message.sender;
            const position = message.message;
            userMap[moveUserId].move(position.x, position.y);
            break;
        //뮤트
        case RELAY_SET_MUTE:
            const muteUserId = message.sender;
            userMap[muteUserId].setMute(message.message);
            break;
        //라이브
        case RELAY_SET_LIVE:
            const liveUserId = message.sender;
            userMap[liveUserId].setOnAir(message.message);
            break;
        //오퍼
        case RELAY_OFFER:
            const offerId = message.sender;
            userMap[offerId].receiveOffer(message.message);
            console.log('받은오퍼', message.message);//test
            break;
        //answer
        case RELAY_ANSWER:
            const answerId = message.sender;
            userMap[answerId].receiveAnswer(message.message);
            console.log('받은엔서', message.message);//test
            break;
        //ice
        case RELAY_ICE:
            const iceId = message.sender;
            userMap[iceId].receiveIce(message.message);
            console.log('받은ice', message.message);//test
            break;
    }
}


/* webRTC code */
const turnServer =
    {
        urls: 'turn:turn.tayo.fun:3478?transport=udp',
        username: 'tayo',
        credential: 'a263203'
    };

//피어커넥션 생성
function createPeerConnection() {
    const peerConnection = new RTCPeerConnection({
        iceServers: [turnServer]
    });

    const mediaStream = new MediaStream();

    if (myVideoStream) {
        myVideoStream.getVideoTracks().forEach(track => {
            mediaStream.addTrack(track);
        });
    }
    if (myAudioStream) {
        myAudioStream.getAudioTracks().forEach(track => {
            mediaStream.addTrack(track);
        });
    }
    mediaStream.getTracks().forEach(track => {
        peerConnection.addTrack(track, mediaStream);
    });

    return peerConnection;
}

//오퍼생성
async function createOffer(peerConnection) {
    const offer = await peerConnection.createOffer();
    peerConnection.setLocalDescription(offer);
    return offer;
}

//answer 생성
async function createAnswer(peerConnection, offer) {
    await peerConnection.setRemoteDescription(offer);
    const answer = await peerConnection.createAnswer();
    await peerConnection.setLocalDescription(answer);
    console.log("answer 생성", answer);//test

    return answer;
}

//ice candidate 릴레이
function iceHandle(data, userId) {
    sendMessage(RELAY_ICE, data.candidate, userId);
    console.log('ice 전송', data.candidate);//test
}