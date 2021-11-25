'use strict'
const CATEGORY_ID_ISSUE = '1';
const CATEGORY_ID_WORK_PLAN = '2';
const CATEGORY_ID_QNA = '3';

let lastPage;
let currentPostId;

const oEditors = [];
const categoryButtons = {};
categoryButtons[CATEGORY_ID_ISSUE] = document.querySelector('.issue-btn');
categoryButtons[CATEGORY_ID_WORK_PLAN] = document.querySelector('.plan-btn');
categoryButtons[CATEGORY_ID_QNA] = document.querySelector('.qna-btn');

/* 나가기 버튼 */
exitBtn.addEventListener('click', () => {
    location.href = '/work-spaces'
});


const navbar = document.querySelector('.navbar');
navbar.style.zIndex = INDEX_NAV_BAR;
navBtn.addEventListener('click', () => {
    navBtn.classList.toggle('active');
    navbar.classList.toggle('active');
});


/* 게시판 띄우기 */
const board = document.createElement('div');
board.classList.add('board');
board.classList.add('all-shadow');

let showBoard = false;

function displayBoard(categoryId, pageNo = 1) {
    $.ajax({
        type: 'GET',
        url: `/work-spaces/${roomId}/board`,
        data: {categoryId, pageNo},
        dataType: 'html',
        success: (data) => {
            lastPage = () => {
                displayBoard(categoryId, pageNo);
            };

            displaySubWindow(data);

            //새글 버튼 이벤트
            const insertBtn = document.querySelector('.board__insert-button');
            insertBtn.addEventListener('click', () => displayCreateForm(categoryId));
        },
        error: () => showModal('실패', '요청을 처리할 수 없습니다.')
    });
}

function displaySubWindow(data) {
    $(board).html(data);

    if (!showBoard) {
        showBoard = true;
        screen.appendChild(board);
    }

    //닫기 이벤트
    const exitBtn = document.querySelector('.board__exit-button');
    if (exitBtn) {
        exitBtn.addEventListener('click', () => {
            screen.removeChild(board);
            showBoard = false;
        });
    }

    //뒤로가기 이벤트
    const backBtn = document.querySelector('.board__back-button');
    if (backBtn) {
        backBtn.addEventListener('click', lastPage);
    }
}

/* 포스트 상세보기 */
function displayPost(postId, noRead, categoryId) {
    $.ajax({
        type: 'GET',
        url: `/work-spaces/${roomId}/board/${postId}`,
        dataType: 'html',
        data: {noRead},
        success: (data) => {
            displaySubWindow(data);
            currentPostId = postId;

            //코멘트 등록 이벤트
            const commentsInputBox = document.querySelector('.comments-input-box');
            const commentsInputBtn = document.querySelector('.comments-input-btn');
            commentsInputBtn.addEventListener('click', () => {
                putComments(postId, commentsInputBox.value);
                commentsInputBox.value = '';
            });

            //수정, 삭제 이벤트 추가할것

            if (noRead) { //새글 갱신
                checkNewPost(categoryId);
            }
        },
        error: console.log
    });
}


/* 새글 확인 */
function checkNewPost(categoryId) {
    $.ajax({
        type: 'GET',
        url: `/work-spaces/${roomId}/board/read-status`,
        data: {categoryId},
        dataType: 'json',
        success: (data) => {
            for (let categoryId in data) {
                setNewPostIcon(categoryId, data[categoryId]);
            }
        },
        error: console.log
    });
}

checkNewPost();

function setNewPostIcon(categoryId, hasNewPost) {
    const targetNode = categoryButtons[categoryId];
    if (hasNewPost) {
        if (!targetNode.classList.contains('active')) {
            targetNode.classList.add('active');
        }
    } else {
        if (targetNode.classList.contains('active')) {
            targetNode.classList.remove('active');
        }
    }
}


/* 댓글 작성 */
function putComments(postId, content) {
    if (!content || content.length < 1 || content.length > 100) {
        showModal('입력 확인', '입력값이 바르지 않습니다.');
        return;
    }

    $.ajax({
        type: 'POST',
        url: `/work-spaces/${roomId}/board/${postId}/comments`,
        data: {content},
        dataType: 'json',
        success: (data) => {
            if (!data.result) {
                showModal('실패', data.message);
                return;
            }

            const commentsList = document.querySelector('.comments-list');
            if (commentsList) {
                const comments = document.createElement('div');
                comments.classList.add('comments');
                const commentsContent = document.createElement('div');
                commentsContent.classList.add('comments__content');
                commentsContent.innerText = content;

                const commentsInfo = document.createElement('div');
                commentsInfo.classList.add('comments-info');
                const commentsProfile = document.createElement('div');
                commentsProfile.classList.add('comments-profile');
                const profileImg = document.createElement('img');
                profileImg.classList.add('comments-profile__img');
                profileImg.src = myAvatar.profile.src;
                const profileName = document.createElement('div');
                profileName.classList.add('comments-profile__name');
                profileName.innerText = `${myName} #${myId.fillZero(4)}`;

                const writeDate = document.createElement('div');
                writeDate.classList.add('comments-write-date');
                writeDate.innerText = moment(new Date()).format('MM/DD HH:mm');

                const commentsDelBtn = document.createElement('i');
                commentsDelBtn.classList.add('fas');
                commentsDelBtn.classList.add('fa-trash-alt');
                commentsDelBtn.classList.add('comments-delete-btn');

                commentsProfile.appendChild(profileImg);
                commentsProfile.appendChild(profileName);
                commentsInfo.appendChild(commentsProfile);
                commentsInfo.appendChild(writeDate);
                comments.appendChild(commentsContent);
                comments.appendChild(commentsInfo);
                comments.appendChild(commentsDelBtn);

                commentsList.appendChild(comments);

                commentsDelBtn.onclick = () => deleteComments(Number(data.message), commentsDelBtn);
            }
        },
        error: () => {
            showModal('실패', '요청을 처리할 수 없습니다.');
        }
    });
}

/* 댓글 삭제 */
function deleteComments(commentsId, node, checkDel = false) {
    if (!checkDel) {
        showModal(
            '삭제확인',
            '삭제 후 복구할수 없습니다. <br>정말 삭제하시겠습니까?',
            () => deleteComments(commentsId, node, true),
            () => {
            }
        );
        return;
    }

    $.ajax({
        type: 'DELETE',
        url: `/work-spaces/${roomId}/board/${currentPostId}/comments/${commentsId}`,
        dataType: 'json',
        success: (data) => {
            if (!data.result) {
                showModal('실패', data.message);
                return;
            }

            const commentsNode = node.parentNode;
            const commentsList = document.querySelector('.comments-list');
            commentsList.removeChild(commentsNode);
        },
        error: () => showModal('실패', '요청을 처리할 수 없습니다.')
    });
}

/* 새글 작성 폼 */
function displayCreateForm(categoryId) {

    $.ajax({
        type: 'GET',
        url: `/work-spaces/${roomId}/board/create`,
        dataType: 'html',
        success: (data) => {
            displaySubWindow(data);

            //카테고리 설정
            const category = document.querySelector('#create__post-category');
            category.value = categoryId;

            const planDatePanel = document.querySelector('.create__plan-date');
            if (categoryId.toString() === CATEGORY_ID_WORK_PLAN && !planDatePanel.classList.contains('active')) {
                planDatePanel.classList.add('active');
            }

            category.addEventListener('change', () => {
                if (category.value === CATEGORY_ID_WORK_PLAN && !planDatePanel.classList.contains('active')) {
                    planDatePanel.classList.add('active');
                }
                if (category.value !== CATEGORY_ID_WORK_PLAN && planDatePanel.classList.contains('active')) {
                    planDatePanel.classList.remove('active');
                }
            });

            //스마트 에디터
            nhn.husky.EZCreator.createInIFrame({
                oAppRef: oEditors,
                elPlaceHolder: "create__post-content",
                sSkinURI: "/resources/se2/SmartEditor2Skin.html",
                fCreator: "createSEditor2",
                htParams: {
                    bUseToolbar: true,
                    bUseVerticalResizer: false,
                    bUseModeChanger: true
                }
            });

            //작성 버튼 이벤트
            const acceptBtn = document.querySelector('.create__accept-btn');
            acceptBtn.addEventListener('click', () => {
                //스마트에디터 값 textarea에 적용
                oEditors.getById["create__post-content"].exec("UPDATE_CONTENTS_FIELD", []);

                //입력값 검증

                //데이터 전송
                const formData = new FormData(document.querySelector('#form'));
                $.ajax({
                    type: 'POST',
                    enctype: 'multipart/form-data',
                    data: formData,
                    processData: false,
                    contentType: false,
                    cache: false,
                    url: `/work-spaces/${roomId}/board/create`,
                    dataType: 'json',
                    success: (data) => {
                        if(!data.result) {
                            showModal('실패', data.message);
                            return;
                        }

                        displayPost(data.message);
                    },
                    error: () => showModal('실패', '요청을 처리할 수 없습니다.')
                });

            });

        },
        error: console.log
    });

}

/* 파일 용량 체크 */
function checkSize(input) {
    if (input.files && input.files[0].size > (1024 * 1024)) {
        showModal('실패', '최대 1mb까지만 업로드 가능합니다.');
        input.value = null;
    }
}