package org.threefour.ddip.product.category.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CategoryName {
    // 여성 의류
    WOMEN_CLOTH("여성 의류", 1),

    OUTER("아우터", 2),
    PADDING("패딩", 3),
    JUMPER("점퍼", 3),
    COAT("코트", 3),
    JACKET("자켓", 3),
    CARDIGAN("가디건", 3),
    VEST("조끼/베스트", 3),

    TOP("상의", 2),
    KNIT_SWEATER("니트/스웨터", 3),
    HOODIE_ZIPUP("후드티/후드집업", 3),
    SWEATSHIRT("맨투맨", 3),
    BLOUSE("블라우스", 3),
    SHIRT("셔츠", 3),
    SHORT_SLEEVE_TSHIRT("반팔 티셔츠", 3),
    LONG_SLEEVE_TSHIRT("긴팔 티셔츠", 3),
    SLEEVELESS_TSHIRT("민소매 티셔츠", 3),

    PANTS("바지", 2),
    DENIM_JEANS("데님/청바지", 3),
    SLACKS("슬랙스", 3),
    COTTON_PANTS("면바지", 3),
    SHORTS("반바지", 3),
    JOGGER_PANTS("트레이닝/조거팬츠", 3),
    LEGGINGS("레깅스", 3),

    SKIRT("치마", 2),
    LONG_SKIRT("롱 스커트", 3),
    MIDI_SKIRT("미디 스커트", 3),
    MINI_SKIRT("미니 스커트", 3),

    DRESS("원피스", 2),
    LONG_DRESS("롱 원피스", 3),
    MIDI_DRESS("미디 원피스", 3),
    MINI_DRESS("미니 원피스", 3),

    // 남성 의류
    MEN_CLOTH("남성 의류", 1),

    MEN_OUTER("아우터", 2),
    MEN_PADDING("패딩", 3),
    MEN_JUMPER("점퍼", 3),
    MEN_COAT("코트", 3),
    MEN_JACKET("자켓", 3),
    MEN_CARDIGAN("가디건", 3),
    MEN_VEST("조끼/베스트", 3),

    MEN_TOP("상의", 2),
    MEN_HOODIE_ZIPUP("후드티/후드집업", 3),
    MEN_SWEATSHIRT("맨투맨", 3),
    MEN_KNIT_SWEATER("니트/스웨터", 3),
    MEN_SHIRT("셔츠", 3),
    MEN_SHORT_SLEEVE_TSHIRT("반팔 티셔츠", 3),
    MEN_LONG_SLEEVE_TSHIRT("긴팔 티셔츠", 3),
    MEN_SLEEVELESS_TSHIRT("민소매 티셔츠", 3),

    MEN_PANTS("바지", 2),
    MEN_DENIM_JEANS("데님/청바지", 3),
    MEN_COTTON_PANTS("면바지", 3),
    MEN_SLACKS("슬랙스", 3),
    MEN_JOGGER_PANTS("트레이닝/조거팬츠", 3),
    MEN_SHORTS("반바지", 3),

    // 신발
    SHOES("신발", 1),

    SNEAKERS("스니커즈", 2),
    CASUAL_SNEAKERS("캐주얼 스니커즈", 3),
    HIGH_TOP_SNEAKERS("하이탑 스니커즈", 3),

    MEN_SHOES("남성화", 2),
    MEN_SANDAL_SLIPPER("샌들/슬리퍼", 3),
    MEN_DRESS_SHOES("구두/로퍼", 3),
    MEN_BOOTS("워커/부츠", 3),

    WOMEN_SHOES("여성화", 2),
    WOMEN_SANDAL_SLIPPER("샌들/슬리퍼", 3),
    WOMEN_DRESS_SHOES("구두", 3),
    WOMEN_FLATS("단화/플랫슈즈", 3),
    WOMEN_BOOTS("워커/부츠", 3),

    SPORTS_SHOES("스포츠화", 2),
    BASKETBALL_SHOES("농구화", 3),
    GOLF_SHOES("골프화", 3),
    FOOTBALL_SHOES("축구/풋살화", 3),
    TENNIS_SHOES("테니스화", 3),
    HIKING_SHOES("등산화/트레킹화", 3),
    BASEBALL_SHOES("야구화", 3),

    BAG_WALLET("가방/지갑", 1),
    WATCH("시계", 1),
    JEWELRY("쥬얼리", 1),
    ACCESSORY("패션 액세서리", 1),
    DIGITAL("디지털", 1),
    APPLIANCE("가전제품", 1),
    SPORT_LEISURE("스포츠/레저", 1),
    VEHICLE_MOTORCYCLE("차량/오토바이", 1),
    STAR_GOODS("스타굿즈", 1),
    KIDULT("키덜트", 1),
    ART_RARE_COLLECTIBLES("예술/희귀/수집품", 1),
    RECORD_INSTRUMENT("음반/악기", 1),
    BOOK_TICKET_STATIONERY("도서/티켓/문구", 1),
    BEAUTY("뷰티/미용", 1),
    FURNITURE_INTERIOR("가구/인테리어", 1),
    LIVING_KITCHENWARE("생활/주방용품", 1),
    TOOL_SUPPLY("공구/산업용품", 1),
    FOOD("식품", 1),
    CHILD_CHILDBIRTH("유아동/출산", 1),
    PET("반려동물용품", 1),
    ETC("기타", 1);

    private final String description;
    private final int degree;
}
