'use strict'

const consentChkAll = document.querySelector('.consentall');

	consentChkAll.addEventListener('change', (e) => {
    let consentChk = document.querySelectorAll('.consentcheckbox');
    for(let i = 0; i < consentChk.length; i++){
    	consentChk.checked = e.target.checked;
    }
});

