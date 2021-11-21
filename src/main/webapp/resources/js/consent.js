'use strict'

const consentChkAll = document.querySelector('input[name=consentall]');

	consentChkAll.addEventListener('change', (e) => {
    let consentChk = document.querySelectorAll('input[name=consent]');
    for(let i = 0; i < consentChk.length; i++){
    	consentChk[i].checked = e.target.checked;
    }
});

