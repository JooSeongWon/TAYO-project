'use strict'

/* 네비게이션 고정 */
let navActive = false;
const headerNav = document.querySelector('.header-nav');
const barsBtn = document.querySelector('.bars-button');

if(barsBtn != null) { //로그인 상태
    barsBtn.addEventListener('click', function () {
        if (navActive) {
            headerNav.classList.remove('active');
            navActive = false;
            return;
        }
        headerNav.classList.add('active');
        navActive = true;
    });
}

/* 홈, 로그아웃 버튼 */
const signOutBtn = document.querySelector('.control-box__icon.exit');
const homeBtn = document.querySelector('.control-box__icon.home');

homeBtn.addEventListener('click', () => {location.href = '/'});
if(signOutBtn != null) signOutBtn.addEventListener('click', () => {location.href = '/logout'});