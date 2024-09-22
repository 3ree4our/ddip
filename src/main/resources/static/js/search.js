$(document).ready(function () {
    const $searchKeyword = $('#searchkeyword');
    const $autocompleteList = $('#autocomplete-list');
    const $searchType = $('#search-type');

    $searchKeyword.on('input', function () {
        const keyword = $(this).val().trim();
        const searchType = $searchType.val();

        if (keyword.length === 0) {
            $autocompleteList.hide();
            return;
        }

        let autocompleteUrl;
        switch (searchType) {
            case 'name':
                autocompleteUrl = `/products/name/auto-complete?keyword=${encodeURIComponent(keyword)}`;
                break;
            case 'category':
                autocompleteUrl = `/products/category/auto-complete?keyword=${encodeURIComponent(keyword)}`;
                break;
            case 'title':
                autocompleteUrl = `/products/title/auto-complete?keyword=${encodeURIComponent(keyword)}`;
                break;
            case 'region':
                autocompleteUrl = `/products/region/auto-complete?keyword=${encodeURIComponent(keyword)}`;
                break;
            case 'school':
                autocompleteUrl = `/products/school/auto-complete?keyword=${encodeURIComponent(keyword)}`;
                break;
            default:
                console.error('잘못된 검색 타입');
                return;
        }

        $.ajax({
            url: autocompleteUrl,
            method: 'GET',
            success: function (data) {
                if (data.length > 0) {
                    showAutocompleteList(data);
                } else {
                    $autocompleteList.hide();
                }
            },
            error: function () {
                console.error('자동완성 요청 실패');
            }
        });
    });

    function showAutocompleteList(items) {
        $autocompleteList.empty().show();
        items.forEach(item => {
            const $listItem = $('<li>')
                .text(item)
                .css({padding: '8px', cursor: 'pointer'})
                .on('click', function () {
                    $searchKeyword.val(item);
                    $autocompleteList.hide();
                });
            $autocompleteList.append($listItem);
        });
    }

    $(document).on('click', function (e) {
        if (!$(e.target).closest('.search-form').length) {
            $autocompleteList.hide();
        }
    });
});

function performSearch() {
    const keyword = $('#searchkeyword').val().trim();
    const searchType = $('#search-type').val();

    if (!keyword) {
        console.error('검색어를 입력하세요.');
        return;
    }

    let searchUrl;
    switch (searchType) {
        case 'name':
            searchUrl = `/products/name?keyword=${encodeURIComponent(keyword)}&paged=true&pageNumber=0&size=9&sort=createdAt,desc`;
            break;
        case 'category':
            searchUrl = `/products/category?keyword=${encodeURIComponent(keyword)}&paged=true&pageNumber=0&size=9&sort=createdAt,desc`;
            break;
        case 'title':
            searchUrl = `/products/title?keyword=${encodeURIComponent(keyword)}&paged=true&pageNumber=0&size=9&sort=createdAt,desc`;
            break;
        case 'region':
            searchUrl = `/products/region?keyword=${encodeURIComponent(keyword)}&paged=true&pageNumber=0&size=9&sort=createdAt,desc`;
            break;
        case 'school':
            searchUrl = `/products/school?keyword=${encodeURIComponent(keyword)}&paged=true&pageNumber=0&size=9&sort=createdAt,desc`;
            break;
        default:
            console.error('잘못된 검색 타입');
            return;
    }

    $.ajax({
        url: searchUrl,
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
        success: function () {
            window.location.href = '/product/search-list';
        },
        error: function (xhr, status, error) {
            console.error('검색 요청 실패:', error);
        }
    });
}
