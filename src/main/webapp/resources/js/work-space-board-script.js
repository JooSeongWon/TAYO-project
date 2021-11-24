'use strict'
const CATEGORY_ID_ISSUE = '1';
const CATEGORY_ID_WORK_PLAN = '2';
const CATEGORY_ID_QNA = '3';

let lastPage;

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
        url: '/work-spaces/board',
        data: {categoryId, pageNo, workSpaceId: roomId},
        dataType: 'html',
        success: (data) => {
            lastPage = () => {
                displayBoard(categoryId, pageNo);
            };

            displaySubWindow(data);
            //새글 버튼 이벤트 추가할것
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
        url: `/work-spaces/board/${postId}`,
        dataType: 'html',
        data: {noRead, workSpaceId: roomId},
        success: (data) => {
            displaySubWindow(data);

            //수정, 삭제, 코멘트 등록 등 이벤트 추가할것

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
        url: '/work-spaces/board/read-status',
        data: {categoryId, workSpaceId: roomId},
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
