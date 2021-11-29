'use strict'

setModalParentNode(document.querySelector('.page-content'));

let consentall = document.querySelector('.consentall');
let consentcheckbox = document.querySelectorAll('.consentcheckbox');
let nextBtn = document.querySelector('.nextBtn');

consentall.addEventListener('click', function(){
	$(consentcheckbox).prop('checked', this.checked);
	console.log($(consentcheckbox).is(":checked"))
})

nextBtn.addEventListener('click',function() {
	if($('.consentcheckbox:checked').length > 2){
		console.log("성공")
		location.href = '/join';
	}else{
		console.log("실패")
		showModal("Tayo", "이용약관을 확인해주세요")
	}
	
});
