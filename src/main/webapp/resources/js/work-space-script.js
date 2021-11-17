/*오디오 테스트용*/
const myAudio = document.createElement('audio');
myAudio.controls = true;
myAudio.autoplay = true;
/*오디오 테스트용*/


//미디어 설정
const myCam = document.createElement('video');
myCam.playsInline = true;
myCam.autoplay = true;
myCam.width = 160;
myCam.height = 90;
myCam.muted = true;
myCam.controls = true;

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

let isMouseOverOnHistory;

//스트림
let myAudioStream;
let myCamStream;
let myScreenStream;

let miceOn = false;
let camOn = false;
let screenOn = false;

let preChatText = '';

//유저맵
const users = [];


//시스템UI 인덱스 설정
navBtn.style.zIndex = INDEX_SYSTEM_UI;
exitBtn.style.zIndex = INDEX_SYSTEM_UI;
bottomMenu.style.zIndex = INDEX_SYSTEM_UI;


async function onMyCamStream() {
    myCamStream = await navigator.mediaDevices.getUserMedia({video: true});
}

async function onMyScreenStream() {
    myScreenStream = await navigator.mediaDevices.getDisplayMedia({video: true});
}

async function onMyAudioStream() {
    myAudioStream = await navigator.mediaDevices.getUserMedia({audio: true});
}

function offMyCamStream() {
    myCamStream.getTracks().forEach(track => track.stop());
    myCamStream = null;
}

function offMyScreenStream() {
    myScreenStream.getTracks().forEach(track => track.stop());
    myScreenStream = null;
}

function offMyAudioStream() {
    myAudioStream.getTracks().forEach(track => track.stop());
    myAudioStream = null;
}


miceBtn.addEventListener('click', handleMiceClick);
camBtn.addEventListener('click', handleCamClick);
screenBtn.addEventListener('click', handleScreenClick);


async function handleMiceClick() {
    if (!miceOn) { //켬
        try {
            await onMyAudioStream();
        } catch (e) { //마이크 사용 거부
            return;
        }

        test(true);
    } else { //끔
        await offMyAudioStream();
        test(false);
    }

    miceBtn.classList.toggle('active');
    miceOn = !miceOn;
    myAvatar.toggleMute();
}

async function handleCamClick() {
    if (!camOn) {//켬
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


        if (screenOn) {
            await handleScreenClick();
        }
        myCam.srcObject = myCamStream;
        camBtn.classList.toggle('fa-spinner');
        camBtn.classList.toggle('fa-video');
        camBtn.style.cursor = 'pointer';
        myAvatar.setOnAir(true);
    } else { //끔
        await offMyCamStream();
        myCam.srcObject = null;
        myAvatar.setOnAir(false);
    }

    camBtn.classList.toggle('active');
    camOn = !camOn;
}

async function handleScreenClick() {
    if (!screenOn) {//켬
        try {
            await onMyScreenStream();
        } catch (e) {
            return;
        }

        if (!myScreenStream) { //화면공유 거부
            return;
        }

        if (camOn) {
            await handleCamClick();
        }
        myCam.srcObject = myScreenStream;
        myAvatar.setOnAir(true);
    } else {//끔
        await offMyScreenStream();
        myCam.srcObject = null;
        myAvatar.setOnAir(false);
    }

    screenBtn.classList.toggle('active');
    screenOn = !screenOn;
}


/* 미디어 테스트용 */
document.body.appendChild(myCam);

function test(on) {
    if (on) {
        myAudio.srcObject = myAudioStream;
    } else {
        myAudio.srcObject = null;
    }
}

/* 미디어 테스트용 */


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

        this.avatar.style.zIndex = INDEX_MY_AVATAR;
        this.speechBubble.style.zIndex = INDEX_SPEECH_BUBBLE;
    }

    render() {
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

        const onAir = document.createElement('i');
        onAir.classList.add('fas');
        onAir.classList.add('fa-microphone-slash');
        onAir.classList.add('mute');
        onAir.classList.add('active');

        const mute = document.createElement('i');
        mute.classList.add('fas');
        mute.classList.add('fa-video');
        mute.classList.add('on-air');


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
        }

        this.avatar.style.left = `${posX}px`;
        this.avatar.style.top = `${posY}px`;
        this.speechBubble.style.left = `${posX}px`;
        this.speechBubble.style.top = `${posY - 15}px`;
        this.x = posX;
        this.y = posY;
    }


    setOnAir(isLive) {
        if (isLive) {
            if (!this.onAir.classList.contains('active')) {
                this.onAir.classList.add('active');
            }
            if (!this.profileWrap.classList.contains('cursor') && this.isOther) {
                this.profileWrap.classList.add('cursor');
            }
            return;
        }
        if (this.onAir.classList.contains('active')) {
            this.onAir.classList.remove('active');
        }
        if (this.profileWrap.classList.contains('cursor') && this.isOther) {
            this.profileWrap.classList.remove('cursor');
        }
    }

    toggleMute() {
        this.mute.classList.toggle('active');
    }

    remove() {
        screen.removeChild(this.avatar);
        screen.appendChild(this.speechBubble);
    }
}

//내 캐릭터
const myAvatar = new Avatar({
    id: myId,
    name: myName,
    profile: myProfile,
    x: 30,
    y: 30
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
        classList.contains('speech-bubble-wrap')
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
socket = new SockJS('/workspace/relay');

//이벤트 설정
socket.onopen = callbackOpenSocket;
socket.onerror = () => showModal('에러', '서버와 연결이 끊어졌습니다. 재연결을 시도합니다.', () => {
    location.href = `/work-spaces/${roomId}`
});
socket.onmessage = receiveMessage;
socket.onclose = () => showModal('에러', '서버와 연결이 끊어졌습니다. 재연결을 시도합니다.', () => {
    location.href = `/work-spaces/${roomId}`
});


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
                users[avatar.userId] = avatar;
            }
            break;
        //새 유저 접속
        case RELAY_JOIN:
            const avatar = new Avatar(message.message);
            avatar.render();
            users[avatar.userId] = avatar;
            break;
        //접속종료
        case RELAY_LEAVE:
            const leaveUserId = message.sender;
            users[leaveUserId].remove();
            users[leaveUserId] = null;
            break;
        //채팅
        case RELAY_CHAT:
            const chatUserId = message.sender;
            users[chatUserId].talk(message.message);
            break;
        //이동
        case RELAY_MOVE:
            const moveUserId = message.sender;
            const position = message.message;
            users[moveUserId].move(position.x, position.y);
            break;
    }
}