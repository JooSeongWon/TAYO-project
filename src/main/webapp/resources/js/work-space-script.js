/* 미디어 설정 */
const myCam = document.createElement('video');
myCam.playsInline = true;
myCam.autoplay = true;
myCam.width = 160;
myCam.height = 90;
myCam.muted = true;
myCam.controls = true;

/*오디오 테스트용*/
const myAudio = document.createElement('audio');
myAudio.controls = true;
myAudio.autoplay = true;
/*오디오 테스트용*/

const miceBtn = document.getElementById('mice');
const camBtn = document.getElementById('cam');
const screenBtn = document.getElementById('screen');

let myAudioStream;
let myCamStream;
let myScreenStream;

let miceOn = false;
let camOn = false;
let screenOn = false;


async function onMyCamStream() {
    try {
        myCamStream = await navigator.mediaDevices.getUserMedia({video: true});
    } catch (e) {
        console.log(e);
    }
}

async function onMyScreenStream() {
    try {
        myScreenStream = await navigator.mediaDevices.getDisplayMedia({video: true});
    } catch (e) {
        console.log(e);
    }
}

async function onMyAudioStream() {
    try {
        myAudioStream = await navigator.mediaDevices.getUserMedia({audio: true});
    } catch (e) {
        console.log(e);
    }
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
        await onMyAudioStream();
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
        await onMyCamStream();

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
        await onMyScreenStream();
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


/* 아바타 관련 */
//인덱스
const INDEX_MY_AVATAR = '50';
const INDEX_OTHER_AVATAR = '49';
const INDEX_SPEECH_BUBBLE = '53';


const screen = document.querySelector('.screen');

class Avatar {
    constructor(user) {
        this.userId = user.id;
        this.userName = user.name;
        this.userProfile = user.profile;
    }

    insertMyAvatar() {
        this.avatar = document.querySelector('.avatar.user');
        this.profile = document.querySelector('.avatar.user img');
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

        const profileWrap = document.createElement('div');
        profileWrap.classList.add('img-wrap');

        const profile = document.createElement('img');
        if (this.profile) {
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
        this.profile = profile;
        this.onAir = onAir;
        this.mute = mute;

        //말풍선
        const speechBubble = document.createElement('div');
        speechBubble.classList.add('speech-bubble-wrap');
        speechBubble.classList.add('active');
        speechBubble.style.zIndex = INDEX_SPEECH_BUBBLE;
        speechBubble.style.left = '30px';
        speechBubble.style.top = '15px';

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
        screen.removeChild(this.speechBubble);
        this.speechBubble.classList.remove('active');

        this.speechBubbleText.innerText = text;

        screen.appendChild(this.speechBubble);
        setTimeout(() => this.speechBubble.classList.add('active'), 3000);
    }

    move(x, y) {
        const posX = Math.max(-15, Math.min(1855, x - 60));
        const posY = Math.max(0, Math.min(1030, y - 40));
        this.avatar.style.left = `${posX}px`;
        this.avatar.style.top = `${posY}px`;
        this.speechBubble.style.left = `${posX}px`;
        this.speechBubble.style.top = `${posY - 15}px`;
    }

    setOnAir(isLive) {
        if (isLive) {
            if (!this.onAir.classList.contains('active')) {
                this.onAir.classList.add('active');
            }
            return;
        }
        if (this.onAir.classList.contains('active')) {
            this.onAir.classList.remove('active');
        }
    }

    toggleMute() {
        this.mute.classList.toggle('active');
    }
}


//내 캐릭터
const myAvatar = new Avatar({
    userId: myId,
    userName: myName,
    userProfile: myProfile
});
myAvatar.insertMyAvatar();

/* 마우스 우클릭 테스트 */
window.oncontextmenu = function (e) {
    if (e.target === document.querySelector('.screen') || isAvatar(e.target)) {
        myAvatar.move(e.pageX, e.pageY);
        return false;
    }
};
/* 채팅 포커스 테스트 */
window.document.body.addEventListener('keydown', function (e) {
    if (e.key === 'Enter') {
        if (document.activeElement !== document.querySelector('#chat')) {
            document.querySelector('#chat').focus();
        } else {
            //채팅입력 처리
            if (!document.querySelector('#chat').value) {
                document.querySelector('#chat').blur();
                return;
            }
            myAvatar.talk(document.querySelector('#chat').value);
            document.querySelector('#chat').value = '';
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
        classList.contains('avatar__name');
}