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
const validateInputData = function (name, count) {
    if (!name ||
        !count ||
        name === '' ||
        count === ''
    ) { //빈 데이터
        showModal('입력오류', '이름과 팀원 수를 정확히 입력하세요!');
        return false;
    }

    const nameRegex = /^[a-zA-Z0-9가-힣 ]{2,20}$/;

    if (!nameRegex.test(name)) { //이름 형식
        showModal('입력오류', '이름은 영문, 숫자, 한글 2~20자 사이로 입력하세요!');
        return false;
    }

    if (count < 2 || count > 10) { //숫자 범위
        showModal('입력오류', '인원은 2~10인 까지 설정 가능합니다.');
        return false;
    }

    return true;
}

//가상공간 이름 인풋 노드 생성
const createNameInputField = function () {
    const inputNode = document.createElement('input');
    inputNode.setAttribute('type', 'text');
    inputNode.setAttribute('placeholder', 'workspace name');
    inputNode.setAttribute('maxlength', '20');
    inputNode.classList.add('tayo-input');
    inputNode.classList.add('form__input');

    return inputNode;
}

//가상공간 인원수 인풋노드 생성
const createCountInputField = function () {
    const inputNode = document.createElement('input');
    inputNode.setAttribute('type', 'number');
    inputNode.setAttribute('placeholder', 'head count');
    inputNode.setAttribute('min', '2');
    inputNode.setAttribute('max', '10');
    inputNode.classList.add('tayo-input');
    inputNode.classList.add('form__input');

    return inputNode;
}

//숫자 프로토타입으로 입력 길이만큼 앞에 0을 채운 문자열 반환
Number.prototype.fillZero = function (width) {
    let n = String(this);//문자열 변환
    return n.length >= width ? n : new Array(width - n.length + 1).join('0') + n;//남는 길이만큼 0으로 채움
}

/* 가상공간 생성 폼 */

let creationForm;
let creationNameInput;
let creationCountInput;

function showCreationForm() {
    //초기화 한적 없을경우 초기화
    if (creationForm === undefined) {

        /* 가상공간 생성 사용 함수 */
        //취소
        const cancelCreation = function () {
            creationNameInput.value = '';
            creationCountInput.value = '';

            section.removeChild(formBackground);
            formBackground.removeChild(creationForm);
        };

        //생성
        const createWorkSpace = function () {
            if (!validateInputData(creationNameInput.value, creationCountInput.value)) {
                return;
            }

            const name = creationNameInput.value;
            const headCount = creationCountInput.value;

            //서버 요청
            $.ajax({
                type: 'POST',
                url: '/work-spaces',
                data: {name: name, headCount: headCount},
                dataType: 'json',
                success: noticeCreationResult,
                error: () => showModal('오류', '해당 요청을 처리할 수 없습니다.')
            });

        }

        //생성 결과
        const noticeCreationResult = function (data) {
            if (!data.result) { //실패
                showModal('입력오류', data.message);
                return;
            }

            //성공
            location.reload();
        }


        //dom node 만들기
        creationForm = document.createElement('div');
        creationForm.style.zIndex = String(formZIndex);
        creationForm.classList.add('all-shadow');
        creationForm.classList.add('form');
        creationForm.classList.add('creation');

        const creationTitle = document.createElement('h3');
        creationTitle.classList.add('tayo-under-line');
        creationTitle.classList.add('form__title');
        creationTitle.innerText = 'Create Workspace';

        creationNameInput = createNameInputField();
        creationCountInput = createCountInputField();

        const buttons = document.createElement('div');
        buttons.classList.add('form__buttons');

        const createBtn = document.createElement('div');
        createBtn.innerText = 'Create';
        createBtn.classList.add('tayo-button');

        const cancelBtn = document.createElement('div');
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
    }

    formBackground.innerHTML = '';
    formBackground.appendChild(creationForm);
    section.appendChild(formBackground);

}

const addWorkSpaceBtn = document.querySelector('.add-work-space');
addWorkSpaceBtn.addEventListener('click', showCreationForm);


/* 가상공간 수정 폼 */
let updateForm;
let updateNameInput;
let updateCountInput;
let updateMemberList;
let invitationCodeText;
let invitationCodeWrap;
let workSpace = {};

let oldName;
let oldCount;

function showUpdateForm(id) {


    //초기화 한적 없을경우 초기화
    if (updateForm === undefined) {

        /* 가상공간 수정 사용 함수 */
        //취소
        const cancelUpdate = function () {

            if (invitationCodeWrap.classList.contains('copied')) {
                invitationCodeWrap.classList.remove('copied');
            }

            updateNameInput.value = '';
            updateCountInput.value = '';
            invitationCodeText.value = '';
            updateMemberList.innerHTML = '';

            section.removeChild(formBackground);
            formBackground.removeChild(updateForm);
        };

        //수정
        const updateWorkSpace = function () {
            //수정내용 없으면 아무일도 안함
            if (updateNameInput.value === oldName && updateCountInput.value === oldCount) {
                showModal('오류', '변경내역이 없습니다.');
                return;
            }

            if (!validateInputData(updateNameInput.value, updateCountInput.value)) {
                return;
            }

            const name = updateNameInput.value;
            const headCount = updateCountInput.value;

            //서버 요청
            $.ajax({
                type: 'PUT',
                url: `/work-spaces/${workSpace.id}`,
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify({
                        name: name,
                        headCount: Number(headCount)
                    }
                ),
                dataType: 'json',
                success: noticeUpdateResult,
                error: () => showModal('오류', '해당 요청을 처리할 수 없습니다.')
            });

        }

        //삭제
        const deleteWorkSpace = function () {

            //삭제 요청
            $.ajax({
                type: 'DELETE',
                url: `/work-spaces/${workSpace.id}`,
                dataType: 'json',
                success: noticeUpdateResult,
                error: () => showModal('오류', '해당 요청을 처리할 수 없습니다.')
            });

        }

        //결과
        const noticeUpdateResult = function (data) {
            if (!data.result) { //실패
                showModal('실패', data.message);
                return;
            }

            //성공
            showModal('완료', '성공적으로 반영되었습니다.', () => location.reload());
        }

        //초대코드 복사
        const copyInvitationCode = function () {
            if (invitationCodeText.value) {
                //선택 - 복사 - 선택해제
                invitationCodeText.select();
                document.execCommand('copy');
                invitationCodeText.setSelectionRange(0, 0);

                //copied class 추가
                if (!invitationCodeWrap.classList.contains('copied')) {
                    invitationCodeWrap.classList.add('copied');
                }
            }
        }

        //초대코드 갱신
        const changeInvitationCode = function () {
            $.ajax({
                type: 'POST',
                url: `/work-spaces/${workSpace.id}/invitation-code`,
                dataType: 'json',
                success: data => {
                    if (!data.result) { //실패
                        showModal('오류', data.message);
                        return;
                    }

                    //성공
                    invitationCodeText.value = data.message;
                    if (invitationCodeWrap.classList.contains('copied')) {
                        invitationCodeWrap.classList.remove('copied');
                    }
                },
                error: () => showModal('오류', '해당 요청을 처리할 수 없습니다.')
            });
        };


        //dom node 만들기

        updateForm = document.createElement('div');
        updateForm.style.zIndex = String(formZIndex);
        updateForm.classList.add('all-shadow');
        updateForm.classList.add('form');
        updateForm.classList.add('update');

        const updateFormHeader = document.createElement('div');
        updateFormHeader.classList.add('update-header');
        updateFormHeader.classList.add('tayo-under-line');

        const updateFormHeaderTitle = document.createElement('div');
        updateFormHeaderTitle.classList.add('update-header__title');
        updateFormHeaderTitle.innerText = 'Update workspace';

        const updateFormHeaderDelBtn = document.createElement('div');
        updateFormHeaderDelBtn.classList.add('fas');
        updateFormHeaderDelBtn.classList.add('fa-trash-alt');

        const updateFormBody = document.createElement('div');
        updateFormBody.classList.add('update-body');

        const updateFormLeft = document.createElement('div');
        updateFormLeft.classList.add('update-left');

        updateNameInput = createNameInputField();
        updateCountInput = createCountInputField();

        const invitationCodeBtn = document.createElement('div');
        invitationCodeBtn.classList.add('tayo-button');
        invitationCodeBtn.classList.add('purple');
        invitationCodeBtn.classList.add('update-left__inv-btn');
        invitationCodeBtn.innerText = 'change invcode';

        const invitationCodeTitle = document.createElement('div');
        invitationCodeTitle.classList.add('inv-title');
        invitationCodeTitle.innerText = 'Invitation code'

        invitationCodeWrap = document.createElement('div');
        invitationCodeWrap.classList.add('inv-wrap');

        const copiedDescription = document.createElement('div');
        copiedDescription.classList.add('copied-description');
        copiedDescription.innerText = 'copied!';

        const invitationCode = document.createElement('div');
        invitationCode.classList.add('inv');

        invitationCodeText = document.createElement('input');
        invitationCodeText.setAttribute('type', 'text');
        invitationCodeText.setAttribute('readonly', 'readonly');
        invitationCodeText.setAttribute('placeholder', 'invitation code');
        invitationCodeText.classList.add('inv__input');
        invitationCodeText.classList.add('tayo-input');

        const invitationCodeCopyBtn = document.createElement('i');
        invitationCodeCopyBtn.classList.add('fas');
        invitationCodeCopyBtn.classList.add('fa-clone');

        const invitationCodeUnderLine = document.createElement('div');
        invitationCodeUnderLine.classList.add('inv-under-line');

        const updateFormRightWrap = document.createElement('div');
        updateFormRightWrap.classList.add('update-right-wrap');

        const updateFormRightTitle = document.createElement('div');
        updateFormRightTitle.classList.add('update-right-title');
        updateFormRightTitle.innerText = 'Member list';

        const updateFormRight = document.createElement('div');
        updateFormRight.classList.add('update-right');
        updateFormRight.classList.add('tayo-scroll-bar');

        updateMemberList = document.createElement('div');
        updateMemberList.classList.add('member-list');

        const buttons = document.createElement('div');
        buttons.classList.add('form__buttons');

        const updateBtn = document.createElement('div');
        updateBtn.innerText = 'Accept';
        updateBtn.classList.add('tayo-button');

        const cancelBtn = document.createElement('div');
        cancelBtn.innerText = 'Cancel';
        cancelBtn.classList.add('tayo-button');

        //버튼 이벤트
        cancelBtn.addEventListener('click', cancelUpdate);
        invitationCodeCopyBtn.addEventListener('click', copyInvitationCode);
        invitationCodeBtn.addEventListener('click', changeInvitationCode);
        updateBtn.addEventListener('click', () =>
            showModal('수정 확인', '수정내용을 반영하시겠 습니까?', updateWorkSpace, () => {
            }));
        updateFormHeaderDelBtn.addEventListener('click', () =>
            showModal('삭제 확인', '삭제시에 복구가 불가능합니다.<br>정말 삭제하시겠습니까?', deleteWorkSpace, () => {
            }));

        //돔트리 구성
        buttons.appendChild(updateBtn);
        buttons.appendChild(cancelBtn);

        invitationCode.appendChild(invitationCodeText);
        invitationCode.appendChild(invitationCodeCopyBtn);
        invitationCodeWrap.appendChild(invitationCode);
        invitationCodeWrap.appendChild(invitationCodeUnderLine);
        invitationCodeWrap.appendChild(copiedDescription);

        updateFormLeft.appendChild(updateNameInput);
        updateFormLeft.appendChild(updateCountInput);
        updateFormLeft.appendChild(invitationCodeTitle);
        updateFormLeft.appendChild(invitationCodeWrap);
        updateFormLeft.appendChild(invitationCodeBtn);

        updateFormHeader.appendChild(updateFormHeaderTitle);
        updateFormHeader.appendChild(updateFormHeaderDelBtn);

        updateFormRight.appendChild(updateMemberList);
        updateFormRightWrap.appendChild(updateFormRightTitle);
        updateFormRightWrap.appendChild(updateFormRight);

        updateFormBody.appendChild(updateFormLeft);
        updateFormBody.appendChild(updateFormRightWrap);

        updateForm.appendChild(updateFormHeader);
        updateForm.appendChild(updateFormBody);
        updateForm.appendChild(buttons);

    }

    workSpace.id = id;
    let responseData;

    //데이터 받아오기 -동기
    $.ajax({
        type: 'POST',
        url: `/work-spaces/${workSpace.id}`,
        dataType: 'json',
        async: false,
        success: data => {
            responseData = data
        },
        error: () => showModal('오류', '해당 요청을 처리할 수 없습니다.')
    });

    //상세정보 파싱 실패
    if (!responseData.result) {
        showModal('오류', responseData.object);
        return;
    }

    //데이터 채우기
    workSpace = responseData.object;
    updateNameInput.value = workSpace.name;
    updateCountInput.value = workSpace.headCount;
    if (workSpace.invitationCode) invitationCodeText.value = workSpace.invitationCode;

    //멤버목록 채우기 및 멤버노드등록
    const myNode = document.createElement('div');
    myNode.classList.add('member');
    const myNodeName = document.createElement('div');
    myNodeName.classList.add('member-name');
    myNodeName.innerText = `${workSpace.members[0].memberName}#${workSpace.members[0].memberId.fillZero(4)}`;
    const myNodeIcon = document.createElement('div');
    myNodeIcon.innerText = 'L';
    myNode.appendChild(myNodeName);
    myNode.appendChild(myNodeIcon);

    updateMemberList.appendChild(myNode);

    for (let i = 1; i < workSpace.members.length; i++) {
        const memberNode = document.createElement('div');
        memberNode.classList.add('member');
        const memberNodeName = document.createElement('div');
        memberNodeName.classList.add('member-name');
        memberNodeName.innerText = `${workSpace.members[i].memberName}#${workSpace.members[i].memberId.fillZero(4)}`;
        const memberNodeExile = document.createElement('div');
        memberNodeExile.innerText = 'X';
        memberNodeExile.classList.add('member-exile');
        memberNode.appendChild(memberNodeName);
        memberNode.appendChild(memberNodeExile);

        updateMemberList.appendChild(memberNode);

        //추방이벤트
        memberNodeExile.addEventListener('click', () => {
            showModal('추방확인', '추방은 즉시 반영되며 되돌릴 수 없습니다.<br>정말 추방하시겠습니까?', () => {
                $.ajax({
                    type: 'DELETE',
                    url: `/work-spaces/${workSpace.id}/members/${workSpace.members[i].memberId}`,
                    dataType: 'json',
                    success: data => {
                        if (data.result) {
                            showModal('성공', '멤버를 추방했습니다.');
                            updateMemberList.removeChild(memberNode);
                            return;
                        }
                        showModal('실패', data.message);
                    },
                    error: () => showModal('오류', '해당 요청을 처리할 수 없습니다.')
                });
            }, () => {
            });
        });
    }

    //바뀐내용 없으면 ajax 요청 안하기 위해 기존 값들 저장
    oldName = updateNameInput.value;
    oldCount = updateCountInput.value;

    //보여주기
    formBackground.innerHTML = '';
    formBackground.appendChild(updateForm);
    section.appendChild(formBackground);

}

//설정 버튼
const updateWorkSpaceBtnList = document.querySelectorAll('.fa-cog');
for (const updateWorkSpaceBtn of updateWorkSpaceBtnList) {
    updateWorkSpaceBtn.addEventListener('click', () => showUpdateForm(updateWorkSpaceBtn.getAttribute('data-workspaceId')));
}

/* 초대코드로 팀 가입 */
const invitationCodeInput = document.querySelector('.invitation-code-input');
const invitationCodeSubmit = document.querySelector('.invitation-code-submit');

const submitInvitationCode = function () {
    //유효성 검사
    if (!invitationCodeInput.value) {
        showModal('실패', '코드를 확인하세요!');
        invitationCodeInput.value = '';
        return;
    }
    if (invitationCodeInput.value.length !== 12) {
        showModal('실패', '코드를 확인하세요!');
        invitationCodeInput.value = '';
        return;
    }

    const invJoinResult = function (data) {
        if (!data.result) {
            showModal('실패', data.message);
            invitationCodeInput.value = '';
            return;
        }

        showModal('성공', data.message, () => location.reload());
    }

    //서버 요청
    $.ajax({
        type: 'POST',
        url: '/work-spaces/invitation/join',
        data: {invitationCode: invitationCodeInput.value},
        dataType: 'json',
        success: invJoinResult,
        error: () => {
            showModal('실패', '해당 요청을 처리할 수 없습니다.');
            invitationCodeInput.value = '';
        }
    });
};

//초대코드 입력
invitationCodeSubmit.addEventListener('click', submitInvitationCode);
invitationCodeInput.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') invitationCodeSubmit.click();
})

//탈퇴 버튼
const exitTeamBtnList = document.querySelectorAll('.fa-sign-out-alt');
for (const exitTeamBtn of exitTeamBtnList) {
    exitTeamBtn.addEventListener('click', () => {
        showModal('탈퇴확인', '정말 탈퇴하시겠습니까?', () => {
            $.ajax({
                type: 'DELETE',
                url: `/work-spaces/${exitTeamBtn.getAttribute('data-workspaceId')}/members`,
                dataType: 'json',
                success: (data) => {
                    if (!data.result) {
                        showModal('실패', data.message);
                        return;
                    }

                    showModal('성공', '팀을 탈퇴하였습니다.', () => {
                        location.reload()
                    });
                },
                error: () => showModal('실패', '해당 요청을 처리할 수 없습니다.')
            });
        }, () => {
        });
    });
}