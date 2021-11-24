
$(".grade").change(function() {
	
	let grade = $(this).val();
	let memberId = $(this).attr('id');
	
	console.log(grade)
	console.log(memberId)
    $.ajax({
       type: "post"
       , url: "/admin/members/"+ memberId
       , data: {
          grade : grade
       }
    ,  dataType:"text" //응답 데이터 형식
    ,  success: function( res ) {
    	console.log(res)

    }
   , error: function() {
      console.log("에러 발생")
   }
       
    })
   
}); //$(".grade").change

$(".ban").change(function() {
	
	let ban = $(this).val();
	let memberId = $(this).attr('id');
	
	console.log(ban)
	console.log(memberId)
    $.ajax({
       type: "post"
       , url: "/admin/members/"+ memberId
       , data: {
          ban : ban
       }
    ,  dataType:"text" //응답 데이터 형식
    ,  success: function( res ) {
    	console.log(res)

    }
   , error: function() {
      console.log("에러 발생")
   }
       
    })
   
}); //$(".ban").change

$("#btnSearch").click(function() {
	location.href="/admin/members/?search="+$("#search").val();
});

$("#search").val("");
$("#search").focus();

function enterkey() {
	if (window.event.keyCode == 13) {
		location.href="/admin/members/?search="+$("#search").val();
    }
}

