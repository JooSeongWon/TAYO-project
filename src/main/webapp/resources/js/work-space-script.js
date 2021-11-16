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
}

async function handleCamClick() {
    if (!camOn) {//켬
        await onMyCamStream();

        if (screenOn) {
            await handleScreenClick();
        }
        myCam.srcObject = myCamStream;
    } else { //끔
        await offMyCamStream();
        myCam.srcObject = null;
    }

    camBtn.classList.toggle('active');
    camOn = !camOn;
}

async function handleScreenClick() {
    if (!screenOn) {//켬
        await onMyScreenStream();

        if (camOn) {
            await handleCamClick();
        }
        myCam.srcObject = myScreenStream;
    } else {//끔
        await offMyScreenStream();
        myCam.srcObject = null;
    }

    screenBtn.classList.toggle('active');
    screenOn = !screenOn;
}


//테스트용
document.body.appendChild(myCam);

function test(on) {
    if (on) {
        myAudio.srcObject = myAudioStream;
    } else {
        myAudio.srcObject = null;
    }
}