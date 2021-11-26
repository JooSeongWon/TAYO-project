'use strict'
const CATEGORY_ID_ISSUE = '1';
const CATEGORY_ID_WORK_PLAN = '2';
const CATEGORY_ID_QNA = '3';

let lastPage;
let currentPostId;

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
    const postBackBtn = document.querySelector('.post__back-button');
    if (postBackBtn) {
        postBackBtn.addEventListener('click', () => displayPost(currentPostId));
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

            //수정 이벤트
            const updateBtn = document.querySelector('.control-edit-btn');
            if (updateBtn) {
                updateBtn.addEventListener('click', () => {
                    displayUpdateForm(postId, categoryId);
                });
            }

            //삭제 이벤트
            const deleteBtn = document.querySelector('.control-delete-btn');
            if (deleteBtn) {
                deleteBtn.addEventListener('click', () => {
                    showModal('삭제 확인', '삭제시에 복구가 불가능합니다. <br>정말 삭제하시겠습니까?', () => {
                        $.ajax({
                            type: 'DELETE',
                            url: `/work-spaces/${roomId}/board/${postId}`,
                            dataType: 'json',
                            success: (data) => {
                                if (!data.result) {
                                    showModal('실패', data.message);
                                    return;
                                }

                                lastPage();
                            },
                            error: () => showModal('실패', '요청을 처리할 수 없습니다.')
                        });
                    }, () => {
                    });
                });
            }

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

                //댓글이 없었을경우 안내 삭제
                const noticeNoComments = document.querySelector('.no-comments');
                if(noticeNoComments) {
                    commentsList.removeChild(noticeNoComments);
                }

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

            if(!commentsList.querySelector('.comments')) {
                const noComments = document.createElement('div');
                noComments.classList.add('no-comments');
                noComments.innerText = '댓글이 없습니다.';
                commentsList.appendChild(noComments);
            }
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

            const oEditors = [];
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
                const categoryIdInput = document.querySelector('#create__post-category');
                if (categoryIdInput.value === CATEGORY_ID_WORK_PLAN) {
                    const planStart = document.querySelector('#create__plan-date-first');
                    const planEnd = document.querySelector('#create__plan-date-second');

                    if (planStart.value.length < 1 || planEnd.value.length < 1) {
                        showModal('실패', '일정을 입력하세요.');
                        return;
                    }
                }

                const title = document.querySelector('#create__post-title');
                const content = document.querySelector('#create__post-content');
                if (title.value.length < 2 || title.value.length > 30) {
                    showModal('실패', '제목은 2-30자리 사이로 입력하세요.');
                    return;
                }
                if (content.value.length < 1) {
                    showModal('실패', '내용을 입력하세요.');
                    return;
                }
                if (content.value.length > 1000) {
                    showModal('실패', '내용이 너무 길어요!');
                    return;
                }

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
                        if (!data.result) {
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

/* 수정 폼 */
function displayUpdateForm(postId) {
    $.ajax({
        type: 'GET',
        url: `/work-spaces/${roomId}/board/${postId}/update`,
        dataType: 'html',
        success: (data) => {
            displaySubWindow(data);

            const oEditors = [];
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


            //수정값 체크
            const formData = new FormData();

            const contentNode = document.querySelector('#create__post-content');
            const oldContent = contentNode.value;
            const titleNode = document.querySelector('#create__post-title');
            const oldFileName = document.querySelector('.update__post-old-file');
            const oldFileDelBtn = document.querySelector('.old-file__delete');
            const fileNode = document.querySelector('#create__post-file');
            const planFirst = document.querySelector('#create__plan-date-first');
            const planSecond = document.querySelector('#create__plan-date-second');

            //수정 이벤트
            if (planFirst) {
                planFirst.addEventListener('change', () => {
                    formData.delete(planFirst.getAttribute('name'));
                    formData.delete(planSecond.getAttribute('name'));

                    formData.append(planFirst.getAttribute('name'), planFirst.value);
                    formData.append(planSecond.getAttribute('name'), planSecond.value);
                })
            }
            if (planSecond) {
                planSecond.addEventListener('change', () => {
                    formData.delete(planFirst.getAttribute('name'));
                    formData.delete(planSecond.getAttribute('name'));

                    formData.append(planFirst.getAttribute('name'), planFirst.value);
                    formData.append(planSecond.getAttribute('name'), planSecond.value);
                })
            }
            titleNode.addEventListener('change', () => {
                formData.delete(titleNode.getAttribute('name'));

                formData.append(titleNode.getAttribute('name'), titleNode.value);
            });
            fileNode.addEventListener('change', () => {
                checkSize(fileNode);
                if (fileNode.value != null) {
                    formData.delete(fileNode.getAttribute('name'));

                    formData.append(fileNode.getAttribute('name'), fileNode.files[0], fileNode.files[0].name);
                }
            });

            if (oldFileDelBtn) {
                oldFileDelBtn.addEventListener('click', () => {
                    formData.delete('deleteFile');

                    formData.append('deleteFile', 'true');
                    oldFileName.removeChild(oldFileDelBtn);
                    oldFileName.classList.add('delete');
                })
            }

            //수정 버튼 이벤트
            const acceptBtn = document.querySelector('.create__accept-btn');
            acceptBtn.addEventListener('click', () => {
                //스마트에디터 값 textarea에 적용
                oEditors.getById["create__post-content"].exec("UPDATE_CONTENTS_FIELD", []);

                //입력값 검증
                if (planFirst && planSecond) {
                    if (planFirst.value.length < 1 || planSecond.value.length < 1) {
                        showModal('실패', '일정을 입력하세요.');
                        return;
                    }
                }

                const title = document.querySelector('#create__post-title');
                const content = document.querySelector('#create__post-content');
                if (title.value.length < 2 || title.value.length > 30) {
                    showModal('실패', '제목은 2-30자리 사이로 입력하세요.');
                    return;
                }
                if (content.value.length < 1) {
                    showModal('실패', '내용을 입력하세요.');
                    return;
                }
                if (content.value.length > 1000) {
                    showModal('실패', '내용이 너무 길어요!');
                    return;
                }

                //content 수정 감지
                if (oldContent !== contentNode.value) {
                    formData.delete(contentNode.getAttribute('name'));

                    formData.append(contentNode.getAttribute('name'), contentNode.value);
                }

                //데이터 전송
                $.ajax({
                    type: 'POST',
                    enctype: 'multipart/form-data',
                    data: formData,
                    processData: false,
                    contentType: false,
                    cache: false,
                    url: `/work-spaces/${roomId}/board/${postId}/update`,
                    dataType: 'json',
                    success: (data) => {
                        if (!data.result) {
                            showModal('실패', data.message);
                            return;
                        }

                        displayPost(postId);
                    },
                    error: () => showModal('실패', '요청을 처리할 수 없습니다.')
                });

            });

        },
        error: console.log
    });

}