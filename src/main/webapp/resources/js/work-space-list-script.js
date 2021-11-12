'use strict'

//기존 모달 알림창 배경색 조정 (완전 투명)
modalBackGroundDomObj.style.backgroundColor = 'rgba(0,0,0,0)';
//부모노드 변경
setModalParentNode(document.querySelector('.page-content'));

//z인덱스
const formZIndex = 9990;

//모든 모달 폼 공용 배경
const formBackground = document.createElement('div');
$(formBackground).css(modalBackGroundStyleObj);
formBackground.style.position = 'absolute';
formBackground.style.zIndex = String(formZIndex - 1);

//섹션영역
const section = document.querySelector('.page-content');

/* 글로벌 사용 함수 */
//입력값 검증
const validateInputData = function () {
    if(!creationNameInput.value ||
        !creationCountInput.value ||
        creationNameInput.value === '' ||
        creationCountInput.value === ''
    ){ //빈 데이터
        showModal('입력오류', '이름과 팀원 수를 정확히 입력하세요!');
        return false;
    }

    const nameRegex = /^[a-zA-Z0-9가-힣]{2,20}$/;

    if(!nameRegex.test(creationNameInput.value)) { //이름 형식
        showModal('입력오류', '이름은 영문, 숫자, 한글 2~20자 사이로 입력하세요!');
        return false;
    }

    if(creationCountInput.value < 2 || creationCountInput.value > 10) { //숫자 범위
        showModal('입력오류', '인원은 2~10인 까지 설정 가능합니다.');
        return false;
    }

    return true;
}

/* 가상공간 생성 폼 */

let creationForm;
let creationNameInput;
let creationCountInput;

function showCreationForm() {
    //초기화 한적 없을경우 초기화
    if(creationForm === undefined) {

        /* 가상공간 생성 사용 함수 */
        //취소
        const cancelCreation = function (){
            section.removeChild(formBackground);
            formBackground.removeChild(creationForm);
        };

        //생성
        const createWorkSpace = function (){
            if(!validateInputData()){
                return;
            }
            
            const name = creationNameInput.value;
            const headCount = creationCountInput.value;

            //서버 요청
            $.ajax({
                type: 'POST',
                url: '/work-spaces',
                data: {name:name, headCount:headCount},
                dataType: 'json',
                success: noticeCreationResult,
                error: () => showModal('서버오류', '해당 요청을 처리할 수 없습니다.')
            });

        }

        //생성 결과
        const noticeCreationResult = function (data){
            if(!data.result) { //실패
                showModal('입력오류', data.object);
                return;
            }

            //성공
            creationNameInput.value = '';
            creationCountInput.value = '';
            location.reload();
        }


        //dom node 만들기
        creationForm = document.createElement('div');
        creationForm.style.zIndex = String(formZIndex);
        creationForm.classList.add('all-shadow');
        creationForm.classList.add('form');
        creationForm.classList.add('creation');

        let creationTitle = document.createElement('h3');
        creationTitle.classList.add('tayo-under-line');
        creationTitle.classList.add('form__title');
        creationTitle.innerText = 'Create Workspace';

        creationNameInput = document.createElement('input');
        creationNameInput.setAttribute('type', 'text');
        creationNameInput.setAttribute('placeholder', 'workspace name');
        creationNameInput.setAttribute('maxlength', '20');
        creationNameInput.classList.add('tayo-input');
        creationNameInput.classList.add('form__input');

        creationCountInput = document.createElement('input');
        creationCountInput.setAttribute('type', 'number');
        creationCountInput.setAttribute('placeholder', 'head count');
        creationCountInput.setAttribute('min', '2');
        creationCountInput.setAttribute('max', '10');
        creationCountInput.classList.add('tayo-input');
        creationCountInput.classList.add('form__input');

        let buttons = document.createElement('div');
        buttons.classList.add('form__buttons');

        let createBtn = document.createElement('div');
        createBtn.innerText = 'Create';
        createBtn.classList.add('tayo-button');

        let cancelBtn = document.createElement('div');
        cancelBtn.innerText = 'Cancel';
        cancelBtn.classList.add('tayo-button');

        createBtn.addEventListener('click', createWorkSpace);
        cancelBtn.addEventListener('click', cancelCreation);

        buttons.appendChild(createBtn);
        buttons.appendChild(cancelBtn);

        creationForm.appendChild(creationTitle);
        creationForm.appendChild(creationNameInput);
        creationForm.appendChild(creationCountInput);
        creationForm.appendChild(buttons);
        formBackground.innerHTML = '';

    }

    formBackground.appendChild(creationForm);
    section.appendChild(formBackground);

}

const addWorkSpaceBtn = document.querySelector('.add-work-space');
addWorkSpaceBtn.addEventListener('click', showCreationForm);


/* 가상공간 수정 폼 */
let updateForm;
let updateNameInput;
let updateCountInput;
let invitationCode;

function showUpdateForm(workspaceId) {
    //초기화 한적 없을경우 초기화
    if(updateForm === undefined) {

        /* 가상공간 수정 사용 함수 */
        //취소
        const cancelUpdate = function (){
            section.removeChild(formBackground);
            formBackground.removeChild(updateForm);
        };

        //수정
        const updateWorkSpace = function (){
            if(!validateInputData()){
                return;
            }

            const name = updateNameInput.value;
            const headCount = updateCountInput.value;

                //서버 요청
            $.ajax({
                type: 'PUT',
                url: '/work-spaces',
                data: {name:name, headCount:headCount},
                dataType: 'json',
                success: noticeUpdateResult,
                error: () => showModal('서버오류', '해당 요청을 처리할 수 없습니다.')
            });

        }

        //생성 결과
        const noticeUpdateResult = function (data){
            if(!data.result) { //실패
                showModal('입력오류', data.object);
                return;
            }

            //성공
            updateNameInput.value = '';
            updateCountInput.value = '';
            invitationCode.value = '';
            location.reload();
        }


        //dom node 만들기


    }



}

const updateWorkSpaceBtnList = document.querySelectorAll('.fa-cog');
for (const updateWorkSpaceBtn of updateWorkSpaceBtnList) {
    updateWorkSpaceBtn.addEventListener('click', () => showUpdateForm(updateWorkSpaceBtn.dataset.workspaceId));
}