function sample4_execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      var roadAddr = data.roadAddress; // 도로명 주소 변수
      var extraRoadAddr = ''; // 참고 항목 변수

      if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
        extraRoadAddr += data.bname;
      }
      if (data.buildingName !== '' && data.apartment === 'Y') {
        extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
      }
      if (extraRoadAddr !== '') {
        extraRoadAddr = ' (' + extraRoadAddr + ')';
      }

      console.log(data);
      document.getElementById('sample4_postcode').value = data.zonecode; //우편번호
      document.getElementById("sample4_roadAddress").value = roadAddr; //도로명 주소
    }
  }).open();
}

$(document).ready(function(){
  const keywordInput = $("#keyword").val();
  if(keywordInput != null && keywordInput != ""){
    search_school();
  }
});

let apiUrl = "https://open.neis.go.kr/hub/schoolInfo?";
let apiKey = "ab990bdc7bdf4826925509ded40029dd";

function search_school(){
  const keyword = $('#keyword').val();

  const url = apiUrl + "KEY=" + apiKey + "&Type=json&pIndex=1&pSize=1000&SCHUL_NM=" + keyword;

  console.log(url);

  $.ajax({
    url: url,
    async: true,
    type: "POST",
    dataType: 'json',
    success: function(data){
      display(data);
    },
    error: function(request, textStatus){
      console.log("학교 검색 오류" + textStatus);
    }
  });
}

function display(data){
  const select = $('#result_select');
  const rows = data.schoolInfo[1].row;

  select.empty();

  $.each(rows, function(index, rowData){
    const schoolName = rowData["SCHUL_NM"];
    const option = $("<option>").text(schoolName).val(schoolName);

    select.append(option);
  });
}