'use strict'

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
            $(board).html(data);
            if(!showBoard) {
                showBoard = true;
                screen.appendChild(board);
            }

            const exitBtn = document.querySelector('.board__exit-button');
            exitBtn.addEventListener('click', () => {
                screen.removeChild(board);
                showBoard = false;
            });
        },
        error: () => showModal('실패', '요청을 처리할 수 없습니다.')
    });
}