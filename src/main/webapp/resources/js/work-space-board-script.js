
/* 나가기 버튼 */
exitBtn.addEventListener('click',() => {location.href = '/work-spaces'});


const navbar = document.querySelector('.navbar');
navbar.style.zIndex = INDEX_NAV_BAR;
navBtn.addEventListener('click', () => {
    navBtn.classList.toggle('active');
    navbar.classList.toggle('active');
});