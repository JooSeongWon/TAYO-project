'use strict'

/*
* 일부로 이름 잘 안겹치게 길게길게 지었어요
*  */

//모달 사용중
let isShowModal = false;

//모달 z인덱스
const modalZIndex = 9999;

//다이얼로그 윈도우
const modalStyleObj = {
    display: 'flex',
    flexDirection: 'column',
    zIndex: modalZIndex,
    width: '300px',
    padding: '20px 20px',
    backgroundColor: '#fefefe',
};
const modalBackGroundStyleObj = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    position: 'fixed',
    zIndex: modalZIndex - 1,
    left: '0px',
    top: '0px',
    width: '100%',
    height: '100%',
    backgroundColor: 'rgba(0,0,0,0.4)'
};

const modalBackGroundDomObj = document.createElement('div');
const modalDomObj = document.createElement('div');
const modalTitleDomObj = document.createElement('h3');
const modalDescriptionDomObj = document.createElement('p');
const modalButtonSetDomObj = document.createElement('div');
const modalOkBtnDomObj = document.createElement('div');
const modalCancelBtnDomObj = document.createElement('div');

//돔 트리 구성
modalDomObj.classList.add('all-shadow');
modalButtonSetDomObj.appendChild(modalOkBtnDomObj);
modalButtonSetDomObj.appendChild(modalCancelBtnDomObj);
modalDomObj.appendChild(modalTitleDomObj);
modalDomObj.appendChild(modalDescriptionDomObj);
modalDomObj.appendChild(modalButtonSetDomObj);
modalBackGroundDomObj.appendChild(modalDomObj);

//스타일 적용
modalOkBtnDomObj.innerText = '확인';
modalCancelBtnDomObj.innerText = '취소';
$(modalBackGroundDomObj).css(modalBackGroundStyleObj);
$(modalDomObj).css(modalStyleObj);
$(modalTitleDomObj).css({
    textAlign: 'center',
});
$(modalDescriptionDomObj).css({
    color: 'var(--color-dark-grey)'
});
$(modalButtonSetDomObj).css({
    display: 'flex',
    justifyContent: 'center',
    marginTop: '15px'
});
$(modalOkBtnDomObj).css({
    display: 'inline-block',
    width: '70px',
    height: '30px',
    lineHeight: '30px',
    textAlign: 'center',
    color: 'var(--color-blue)',
    background: 'none',
    border: '1px solid var(--color-blue)',
    borderRadius: '4px',
    cursor: 'pointer',
    outline:'none'
});
$(modalCancelBtnDomObj).css({
    display: 'inline-block',
    width: '70px',
    height: '30px',
    lineHeight: '30px',
    textAlign: 'center',
    color: 'var(--color-red)',
    background: 'none',
    border: '1px solid var(--color-red)',
    borderRadius: '4px',
    marginLeft: '20px',
    cursor: 'pointer'
});

//버튼 콜백함수 지정
let modalOkEvent;
let modalCancelEvent;

function modalBtnClicked(callback) {
    if (!isShowModal) {
        return;
    }

    callback();

    if (modalCancelEvent !== undefined) {
        modalCancelBtnDomObj.removeEventListener('click', modalCancelEvent);
        modalCancelEvent = undefined;
    }
    modalOkBtnDomObj.removeEventListener('click', modalOkEvent);
    document.body.removeChild(modalBackGroundDomObj);

    isShowModal = false;
    return false;
}

//모달 띄우기 - 사용시 이 함수만 고려
function showModal(title, msg, okCallBack, cancelCallBack) {
    if (isShowModal) {
        return;
    }

    const okCallBackT = (okCallBack === undefined) ? () => {
    } : okCallBack;

    modalTitleDomObj.innerText = title;
    modalDescriptionDomObj.innerText = msg;

    modalOkEvent = () => modalBtnClicked(okCallBackT);
    modalOkBtnDomObj.addEventListener('click', modalOkEvent);
    modalCancelBtnDomObj.style.display = 'none';

    if (cancelCallBack !== undefined) {

        modalCancelEvent = () => modalBtnClicked(cancelCallBack);
        modalCancelBtnDomObj.addEventListener('click', modalCancelEvent);
        modalCancelBtnDomObj.style.display = 'inline-block';
    }

    document.body.appendChild(modalBackGroundDomObj);
    modalOkBtnDomObj.focus();
    isShowModal = true;
}

//엔터키 이벤트
modalOkBtnDomObj.setAttribute('tabindex', '-1');
modalOkBtnDomObj.addEventListener('keydown', e => {
    if (e.key === 'Enter') {
        modalOkBtnDomObj.click();
    }
});