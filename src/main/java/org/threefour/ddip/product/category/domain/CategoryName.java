package org.threefour.ddip.product.category.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

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

    // 가방 지갑
    BAG_WALLET("가방/지갑", 1),

    WOMEN_BAG("여성가방", 2),
    CLUTCH_BAG("클러치백", 3),
    SHOULDER_BAG("숄더백", 3),
    CROSS_BAG("크로스백", 3),
    TOTE_BAG("토트백", 3),
    BACKPACK("백팩", 3),
    OTHER_WOMEN_BAG("기타 여성가방", 3),

    MEN_BAG("남성가방", 2),
    MEN_CLUTCH("클러치", 3),
    MEN_SHOULDER_BAG("숄더백", 3),
    MEN_CROSS_BAG("크로스백", 3),
    BRIEFCASE("브리프케이스", 3),
    MEN_BACKPACK("백팩", 3),
    OTHER_MEN_BAG("기타 남성가방", 3),

    TRAVEL_BAG("여행용 가방", 2),
    CARRIER("캐리어", 3),
    OTHER_TRAVEL_BAG("기타 여행용 가방", 3),

    // 시계
    WATCH("시계", 1),

    MEN_WATCH("남성시계", 2),
    PREMIUM_MEN_WATCH("프리미엄 시계", 3),
    DIGITAL_MEN_WATCH("일반시계(디지털)", 3),
    ANALOG_MEN_WATCH("일반시계(아날로그)", 3),

    WOMEN_WATCH("여성시계", 2),
    PREMIUM_WOMEN_WATCH("프리미엄 시계", 3),
    DIGITAL_WOMEN_WATCH("일반시계(디지털)", 3),
    ANALOG_WOMEN_WATCH("일반시계(아날로그)", 3),

    WATCH_ACCESSORY("시계 용품", 2),
    WATCH_STRAP("스트랩", 3),
    OTHER_WATCH_ACCESSORY("기타 시계용품", 3),

    // 쥬얼리
    JEWELRY("쥬얼리", 1),

    EARRING_PIERCING("귀걸이/피어싱", 2),
    DIAMOND_EARRING("다이아몬드 귀걸이", 3),
    GOLD_EARRING("금 귀걸이", 3),
    SILVER_EARRING("은 귀걸이", 3),
    PEARL_COLORED_EARRING("진주/유색보석 귀걸이", 3),
    FASHION_EARRING("패션 귀걸이", 3),
    PIERCING("피어싱", 3),
    CLIP_EARRING("귀찌/이어커프", 3),

    NECKLACE_PENDANT("목걸이/팬던트", 2),
    DIAMOND_NECKLACE("다이아몬드 목걸이", 3),
    GOLD_NECKLACE("금 목걸이", 3),
    SILVER_NECKLACE("은 목걸이", 3),
    PEARL_COLORED_NECKLACE("진주/유색보석 목걸이", 3),
    FASHION_NECKLACE("패션 목걸이", 3),
    CHOKER("초커", 3),

    BRACELET("팔찌", 2),
    GOLD_BRACELET("금 팔찌", 3),
    SILVER_BRACELET("은 팔찌", 3),
    FASHION_BRACELET("패션 팔찌", 3),

    ANKLET("발찌", 2),
    GOLD_ANKLET("금 발찌", 3),
    SILVER_ANKLET("은 발찌", 3),
    FASHION_ANKLET("패션 발찌", 3),

    RING("반지", 2),
    DIAMOND_RING("다이아몬드 반지", 3),
    GOLD_RING("금 반지", 3),
    SILVER_RING("은 반지", 3),
    PEARL_COLORED_RING("진주/유색보석 반지", 3),
    FASHION_RING("패션 반지", 3),

    OTHER_JEWELRY("기타 쥬얼리", 2),

    // 패션 액세서리
    ACCESSORY("패션 액세서리", 1),

    HAT("모자", 2),
    BALLCAP("볼캡", 3),
    BUCKET_HAT("버킷", 3),
    SNAPBACK("스냅백", 3),
    BEANIE("비니", 3),
    OTHER_HAT("기타(모자)", 3),

    GLASSES_SUNGLASSES("안경/선글라스", 2),
    SUNGLASSES("선글라스", 3),
    GLASSES("안경", 3),

    SCARF_TIE("스카프/넥타이", 2),
    SCARF("스카프", 3),
    TIE("넥타이", 3),

    BELT("벨트", 2),
    MEN_BELT("남성 벨트", 3),
    WOMEN_BELT("여성 벨트", 3),

    SOCKS_STOCKINGS("양말/스타킹", 2),
    SOCKS("양말", 3),
    STOCKINGS("스타킹", 3),

    UMBRELLA("우산/양산", 2),

    KEYRING_CASE("키링/키케이스", 2),

    OTHER_ACCESSORY("기타 악세서리", 2),

    // 디지털
    DIGITAL("디지털", 1),

    PHONE("휴대폰", 2),
    SMARTPHONE("스마트폰", 3),
    FEATURE_PHONE("일반폰(피처폰)", 3),
    CASE_ACCESSORIES("케이스/보호필름/액세서리", 3),
    CABLE_CHARGER_ACCESSORIES("케이블/충전기/주변기기", 3),
    OTHER_PHONE("기타 휴대폰", 3),

    TABLET("태블릿", 2),
    TABLET_DEVICE("태블릿", 3),
    TABLET_CASE_ACCESSORIES("케이스/보호필름/액세서리", 3),
    TABLET_CABLE_CHARGER_ACCESSORIES("케이블/충전기/주변기기", 3),

    WEARABLE("웨어러블(워치/밴드)", 2),
    SMART_WATCH_BAND("스마트워치/밴드", 3),
    WEARABLE_CASE_ACCESSORIES("케이스/보호필름/액세서리", 3),
    WEARABLE_CABLE_CHARGER_ACCESSORIES("케이블/충전기/주변기기", 3),

    AUDIO_VIDEO_EQUIPMENT("오디오/영상/관련기기", 2),
    EARPHONES("이어폰", 3),
    HEADPHONES("헤드폰", 3),
    SPEAKER_AMP("스피커/앰프", 3),
    MP3_PMP("MP3/PMP", 3),
    VIDEO_PROJECTOR("비디오/프로텍터", 3),
    AUDIO_HOME_THEATER("오디오/홈시어터", 3),
    OTHER_AUDIO_VIDEO_EQUIPMENT("기타 오디오/영상/관련기기", 3),

    PC_LAPTOP("PC/노트북", 2),
    DESKTOP("데스크탑", 3),
    LAPTOP_NETBOOK("노트북/넷북", 3),
    MONITOR("모니터", 3),
    KEYBOARD("키보드", 3),
    MOUSE("마우스", 3),
    OTHER_PC_ACCESSORY("기타 PC 주변기기", 3),
    LAPTOP_BAG_ACCESSORY("노트북 가방/액세서리", 3),
    OTHER_PC_LAPTOP("기타 PC/노트북", 3),

    GAME_TITLE("게임/타이틀", 2),
    NINTENDO_WII("닌텐도/NDS/Wii", 3),
    SONY_PLAYSTATION("소니/플레이스테이션", 3),
    XBOX("XBOX", 3),
    PC_GAME("PC게임", 3),
    OTHER_GAME_TITLE("기타 게임/타이틀", 3),

    CAMERA_DSLR("카메라/DSLR", 2),
    FILM_CAMERA("필름카메라/중형카메라", 3),
    DSLR_MIRRORLESS("DSLR/미러리스", 3),
    LENS_FILTER_CONVERTER("렌즈/필터/컨버터", 3),
    DIGITAL_CAMERA("일반디카/토이카메라", 3),
    TRIPOD_FLASH_LIGHT("삼각대/플래시/조명", 3),
    DIGITAL_CAMCORDER("디지털 캠코더", 3),
    MEMORY_BATTERY_BAG("메모리/배터리/가방", 3),
    OTHER_CAMERA("기타 카메라", 3),

    PC_COMPONENT_STORAGE("PC부품/저장장치", 2),
    CPU_MOTHERBOARD("CPU/메인보드", 3),
    HDD_ODD_SSD("HDD/ODD/SSD", 3),
    USB_CABLE_SPEAKER("USB/케이블/스피커", 3),
    PRINTER("복합기/프린터", 3),
    NETWORK_EQUIPMENT("네트워크장비", 3),
    COOLER_POWER_SUPPLY("쿨러/파워서플라이", 3),
    MEMORY_VGA("메모리/VGA", 3),
    CONSUMABLES("소모품", 3),

    // 가전제품
    APPLIANCE("가전제품", 1),

    HOUSEHOLD_APPLIANCE("생활가전", 2),
    MASSAGER("마사지기", 3),
    CONSUMABLES_APPLIANCE("소모품", 3),
    VACUUM("청소기", 3),
    AIR_PURIFIER("공기청정기", 3),
    HUMIDIFIER("가습기", 3),
    DEHUMIDIFIER("제습기", 3),
    FAN_COOLER("선풍기/냉풍기", 3),
    HEATER("히터/온풍기", 3),
    ELECTRIC_MAT("전기매트/장판", 3),
    IRON("다리미", 3),
    SEWING_MACHINE("미싱/재봉틀", 3),

    KITCHEN_APPLIANCE("주방가전", 2),
    INDUCTION_RANGE("인덕션/전기레인지", 3),
    RICE_COOKER("전기밥솥", 3),
    COFFEE_MACHINE("커피머신", 3),
    AIR_FRYER("에어프라이어", 3),
    BLENDER("믹서기/블렌더", 3),
    DISHWASHER("식기세척기", 3),
    WATER_PURIFIER("정수기", 3),
    OVEN("오븐", 3),
    ELECTRIC_KETTLE("전기포트", 3),
    TOASTER("토스터", 3),
    MICROWAVE("전자레인지", 3),
    FOOD_DISPOSER("음식물 처리기", 3),

    BEAUTY_APPLIANCE("미용가전", 2),
    SKIN_CARE_DEVICE("피부케어기기", 3),
    HAIR_IRON("고데기", 3),
    HAIR_DRYER("드라이기", 3),
    SHAVER_CLIPPER("면도기/이발기", 3),
    EPILATOR("제모기", 3),

    REFRIGERATOR("냉장고", 2),

    AIR_CONDITIONER("에어컨", 2),

    WASHER_DRYER("세탁기/건조기", 2),

    TV("TV", 2),

    OFFICE_EQUIPMENT("사무기기(복사기/팩스 등)", 2),

    OTHER_APPLIANCE("기타 가전제품", 2),

    // 스포츠/레저
    SPORT_LEISURE("스포츠/레저", 1),

    GOLF("골프", 2),
    GOLF_CLUB("골프 채", 3),
    GOLF_MEN_CLOTH("골프 남성 의류", 3),
    GOLF_WOMEN_CLOTH("골프 여성 의류", 3),
    GOLF_BAG("골프백", 3),
    GOLF_BALL("골프공", 3),
    GOLF_COVER("골프 커버", 3),
    OTHER_GOLF("기타 골프 용품", 3),

    CAMPING("캠핑", 2),
    CAMPING_CHAIR_TABLE("캠핑 의자/테이블", 3),
    CAMPING_COOKWARE("캠핑 취사용품", 3),
    CAMPING_LANTERN("캠핑 랜턴", 3),
    SLEEPING_BAG_MAT("침낭/매트/해먹", 3),
    TENT_SHELTER("텐트/그늘막", 3),
    OTHER_CAMPING("기타 캠핑 용품", 3),

    FISHING("낚시", 2),
    FISHING_EQUIPMENT("낚시 장비", 3),
    FISHING_CLOTH_ACCESSORY("낚시 의류 및 기타 용품", 3),

    FOOTBALL("축구", 2),
    FOOTBALL_CLOTH_ACCESSORY("축구 의류/잡화", 3),
    FOOTBALL_EQUIPMENT("축구 용품", 3),
    FOOTBALL_GOODS("굿즈(카드/사인볼 등)", 3),

    BASEBALL("야구", 2),
    BASEBALL_CLOTH_ACCESSORY("야구 의류/잡화", 3),
    BASEBALL_EQUIPMENT("야구 용품", 3),
    BASEBALL_GLOVE("글러브", 3),
    BASEBALL_GOODS("굿즈(카드/사인볼 등)", 3),

    BASKETBALL("농구", 2),
    BASKETBALL_CLOTH_ACCESSORY("농구 의류/잡화", 3),
    BASKETBALL_EQUIPMENT("농구 용품", 3),
    BASKETBALL_GOODS("굿즈(카드/사인볼 등)", 3),

    BICYCLE("자전거", 2),
    BICYCLE_CLOTH_ACCESSORY("자전거 의류 및 액세서리", 3),
    BICYCLE_PARTS("자전거 부품", 3),
    CLASSIC_FIXIE("클래식/픽시", 3),
    ROAD_BMX("로드/BMX", 3),
    MTB("MTB/산악", 3),
    ELECTRIC_HYBRID("전동/하이브리드", 3),
    MINIVELO_FOLDABLE("미니벨로/접이식", 3),
    OTHER_BICYCLE("기타 자전거", 3),

    HIKING_CLIMBING("등산/클라이밍", 2),
    MEN_HIKING_CLOTH("남성 등산복", 3),
    WOMEN_HIKING_CLOTH("여성 등산복", 3),
    HIKING_BAG("등산 가방", 3),
    CLIMBING("암벽/클라이밍", 3),
    OTHER_HIKING("기타 등산 용품", 3),

    FITNESS_YOGA_PILATES("헬스/요가/필라테스", 2),
    FITNESS_YOGA_EQUIPMENT("헬스/요가/필라테스 장비", 3),
    FITNESS_YOGA_CLOTH("헬스/요가/필라테스 의류", 3),
    OTHER_FITNESS_YOGA("기타 용품", 3),

    INLINE_SKATEBOARD("인라인/스케이트보드", 2),

    ELECTRIC_SCOOTER("전동킥보드/전동휠", 2),

    TENNIS("테니스", 2),

    BADMINTON("배드민턴", 2),

    BOWLING("볼링", 2),

    TABLE_TENNIS("탁구", 2),

    BILLIARDS("당구", 2),

    WINTER_SPORTS("겨울 스포츠", 2),
    SKI_SNOWBOARD_CLOTH_ACCESSORY("스키/보드 의류 및 잡화", 3),
    SNOWBOARD_EQUIPMENT("스노우보드 장비", 3),
    SKI_EQUIPMENT("스키 장비", 3),
    OTHER_WINTER_SPORTS("기타 겨울 스포츠", 3),

    WATER_SPORTS("수상스포츠", 2),
    MEN_SWIMWEAR("남성 수영복/래쉬가드", 3),
    WOMEN_SWIMWEAR("여성 수영복/래쉬가드", 3),
    SWIMMING_EQUIPMENT("수영/물놀이 용품", 3),
    SURFING("서핑", 3),
    OTHER_WATER_SPORTS("기타 수상 스포츠", 3),

    FIGHTING_MARTIAL_ARTS("격투/무술", 2),
    BOXING("복싱", 3),
    JIU_JITSU("주짓수", 3),
    OTHER_FIGHTING_MARTIAL_ARTS("기타 격투/무술", 3),

    OTHER_SPORTS("기타 스포츠", 2),

    // 차량/오토바이
    VEHICLE_MOTORCYCLE("차량/오토바이", 1),

    CAR_ACCESSORY_PART("차량 용품/부품", 2),
    TIRE_WHEEL("타이어/휠", 3),
    CAR_PART("차량 부품", 3),
    CAR_TUNING_ACCESSORY("차량/튜닝 용품", 3),
    NAVIGATION_BLACKBOX("네비게이션/블랙박스", 3),
    CAR_AUDIO_VIDEO("카오디오/영상", 3),

    MOTORCYCLE_SCOOTER("오토바이/스쿠터", 2),
    MOTORCYCLE_UNDER_125CC("오토바이(125cc 이하)", 3),
    MOTORCYCLE_OVER_125CC("오토바이(125cc 초과)", 3),

    MOTORCYCLE_ACCESSORY_PART("오토바이 용품/부품", 2),
    RIDER_ACCESSORY("라이더 용품", 3),
    MOTORCYCLE_PART("오토바이 부품", 3),
    MOTORCYCLE_TUNING_ACCESSORY("오토바이/튜닝 용품", 3),
    OTHER_MOTORCYCLE_ACCESSORY_PART("기타(오토바이 용품/부품)", 3),

    // 스타굿즈
    STAR_GOODS("스타굿즈", 1),

    BOY_GROUP("보이그룹", 2),
    BOY_GROUP_ALBUM_VIDEO("음반/영상물", 3),
    BOY_GROUP_FANCY_CARD("팬시/포토카드", 3),
    BOY_GROUP_POSTER_PHOTOBOOK("포스터/화보", 3),
    BOY_GROUP_FIGURE("인형/피규어", 3),
    BOY_GROUP_CHEER_TOOL("응원도구", 3),
    BOY_GROUP_CLOTH_ACCESSORY("의류/패션잡화", 3),
    OTHER_BOY_GROUP("기타(보이그룹)", 3),

    GIRL_GROUP("걸그룹", 2),
    GIRL_GROUP_ALBUM_VIDEO("음반/영상물", 3),
    GIRL_GROUP_FANCY_CARD("팬시/포토카드", 3),
    GIRL_GROUP_POSTER_PHOTOBOOK("포스터/화보", 3),
    GIRL_GROUP_FIGURE("인형/피규어", 3),
    GIRL_GROUP_CHEER_TOOL("응원도구", 3),
    GIRL_GROUP_CLOTH_ACCESSORY("의류/패션잡화", 3),
    OTHER_GIRL_GROUP("기타(걸그룹)", 3),

    MALE_SOLO("솔로(남)", 2),
    MALE_SOLO_ALBUM_VIDEO("음반/영상물", 3),
    MALE_SOLO_FANCY_CARD("팬시/포토카드", 3),
    MALE_SOLO_POSTER_PHOTOBOOK("포스터/화보", 3),
    MALE_SOLO_FIGURE("인형/피규어", 3),
    MALE_SOLO_CHEER_TOOL("응원도구", 3),
    MALE_SOLO_CLOTH_ACCESSORY("의류/패션잡화", 3),
    OTHER_MALE_SOLO("기타(솔로-남)", 3),

    FEMALE_SOLO("솔로(여)", 2),
    FEMALE_SOLO_ALBUM_VIDEO("음반/영상물", 3),
    FEMALE_SOLO_FANCY_CARD("팬시/포토카드", 3),
    FEMALE_SOLO_POSTER_PHOTOBOOK("포스터/화보", 3),
    FEMALE_SOLO_FIGURE("인형/피규어", 3),
    FEMALE_SOLO_CHEER_TOOL("응원도구", 3),
    FEMALE_SOLO_CLOTH_ACCESSORY("의류/패션잡화", 3),
    OTHER_FEMALE_SOLO("기타(솔로-여)", 3),

    MALE_ACTOR("배우(남)", 2),
    MALE_ACTOR_ALBUM_VIDEO("음반/영상물", 3),
    MALE_ACTOR_FANCY_CARD("팬시/포토카드", 3),
    MALE_ACTOR_POSTER_PHOTOBOOK("포스터/화보", 3),
    MALE_ACTOR_FIGURE("인형/피규어", 3),
    MALE_ACTOR_CHEER_TOOL("응원도구", 3),
    MALE_ACTOR_CLOTH_ACCESSORY("의류/패션잡화", 3),
    OTHER_MALE_ACTOR("기타(배우-남)", 3),

    FEMALE_ACTOR("배우(여)", 2),
    FEMALE_ACTOR_ALBUM_VIDEO("음반/영상물", 3),
    FEMALE_ACTOR_FANCY_CARD("팬시/포토카드", 3),
    FEMALE_ACTOR_POSTER_PHOTOBOOK("포스터/화보", 3),
    FEMALE_ACTOR_FIGURE("인형/피규어", 3),
    FEMALE_ACTOR_CHEER_TOOL("응원도구", 3),
    FEMALE_ACTOR_CLOTH_ACCESSORY("의류/패션잡화", 3),
    OTHER_FEMALE_ACTOR("기타(배우-여)", 3),

    TV_ENTERTAINMENT_CHARACTER("방송/예능/캐릭터", 2),
    TV_ENTERTAINMENT_ALBUM_VIDEO("음반/영상물", 3),
    TV_ENTERTAINMENT_FANCY_CARD("팬시/포토카드", 3),
    TV_ENTERTAINMENT_POSTER_PHOTOBOOK("포스터/화보", 3),
    TV_ENTERTAINMENT_FIGURE("인형/피규어", 3),
    TV_ENTERTAINMENT_CHEER_TOOL("응원도구", 3),
    TV_ENTERTAINMENT_CLOTH_ACCESSORY("의류/패션잡화", 3),
    OTHER_TV_ENTERTAINMENT("기타(방송인)", 3),

    // 키덜트
    KIDULT("키덜트", 1),

    FIGURE_DOLL("피규어/인형", 2),
    LEGO_BLOCK("레고/블럭", 2),
    PLAMODEL("프라모델", 2),
    RC_DRONE("RC/드론", 2),
    BOARD_GAME("보드게임", 2),
    SURVIVAL_GUN("서바이벌건", 2),
    OTHER_KIDULT("기타(키덜트)", 2),

    // 예술/희귀/수집품
    ART_RARE_COLLECTIBLES("예술/희귀/수집품", 1),

    RARE_COLLECTIBLES("희귀/수집품", 2),

    ANTIQUE("골동품", 2),

    ARTWORK("예술작품", 2),

    // 음반/악기
    RECORD_INSTRUMENT("음반/악기", 1),

    CD_DVD_LP("CD/DVD/LP", 2),

    INSTRUMENT("악기", 2),
    WIND_INSTRUMENT("관악기", 3),
    STRING_INSTRUMENT("현악기", 3),
    PERCUSSION_INSTRUMENT("타악기", 3),
    KEYBOARD_INSTRUMENT("건반악기", 3),
    ELECTRIC_INSTRUMENT("전자악기", 3),
    INSTRUMENT_ACCESSORY("악기 용품", 3),
    OTHER_RECORD_INSTRUMENT("기타(음반/악기)", 3),

    // 도서/티켓/문구
    BOOK_TICKET_STATIONERY("도서/티켓/문구", 1),

    BOOK("도서", 2),
    POETRY_NOVEL("시/소설", 3),
    SELF_DEVELOPMENT("자기계발", 3),
    HUMANITIES("교양/인문", 3),
    ECONOMY_BUSINESS("경제/경영", 3),
    STUDY_DICTIONARY("학습/사전/참고서", 3),
    CHILDREN_BOOK("아동/어린이", 3),
    COMICS("만화", 3),
    ART_DESIGN("예술/디자인", 3),
    TRAVEL_HOBBY("여행/취미/레저/건강", 3),
    SOCIAL_POLITICS_LAW("사회/정치/법", 3),
    SCIENCE_IT("과학/IT", 3),
    PERIODICAL("간행물", 3),
    OTHER_BOOK("기타(도서)", 3),

    STATIONERY("문구", 2),
    LEARNING_TOOL("학습도구/문구/필기류", 3),
    ART_SUPPLIES("미술/화방용품", 3),

    GIFTICON_COUPON("기프티콘/쿠폰", 2),
    FOOD_COUPON("치킨/피자/햄버거", 3),
    BAKERY_COUPON("베이커리/도넛/아이스크림", 3),
    COFFEE_COUPON("커피(음료)", 3),
    CONVENIENCE_STORE("편의점", 3),
    OTHER_GIFTICON_COUPON("기타(키프티콘/쿠폰)", 3),

    VOUCHER("상품권", 2),
    CULTURE_BOOK_VOUCHER("문화/도서", 3),
    DEPARTMENT_VOUCHER("백화점", 3),
    DINING_VOUCHER("외식", 3),
    OTHER_VOUCHER("기타(상품권)", 3),

    TICKET("티켓", 2),
    MUSICAL_PLAY("뮤지컬/연극", 3),
    CONCERT("콘서트", 3),
    MOVIE_TICKET("영화(예매/관람권)", 3),
    TRAVEL_STAY_RENTAL("여행/숙박/렌트", 3),
    THEME_PARK("테마파크", 3),
    SPORTS_LEISURE_TICKET("스포츠/레저", 3),
    EXHIBITION_EVENT("공연/전시/행사", 3),
    OTHER_TICKET("기타(티켓)", 3),

    // 뷰티/미용
    BEAUTY("뷰티/미용", 1),

    SKINCARE("스킨케어", 2),
    CLEANSING_SCRUB("클렌징/스크럽", 3),
    TONER_MIST("스킨/토너/미스트", 3),
    LOTION_EMULSION("로션/에멀젼", 3),
    ESSENCE_CREAM("에센스/크림", 3),
    MASK("팩/마스크", 3),
    SUNCARE("썬케어", 3),
    OTHER_SKINCARE("기타(스킨케어)", 3),

    COLOR_MAKEUP("색조메이크업", 2),
    EYELINER_BROW("아이라이너/브로우", 3),
    EYESHADOW("아이섀도우", 3),
    MASCARA("마스카라", 3),
    LIP_TINT("립틴트", 3),
    LIP_BALM_GLOSS("립밤/립글로즈", 3),
    LIPSTICK("립스틱", 3),
    CHEEK_BLUSHER("치크/블러셔", 3),
    OTHER_COLOR_MAKEUP("기타(색조메이크업)", 3),

    BASE_MAKEUP("베이스메이크업", 2),
    MAKEUP_BASE("메이크업베이스", 3),
    BB_CC_CREAM("BB/CC크림", 3),
    CUSHION_COMPACT("쿠션팩트", 3),
    FOUNDATION("파운데이션", 3),
    POWDER_COMPACT("파우더/팩트", 3),
    PRIMER_CONCEALER("프라이머/컨실러", 3),
    OTHER_BASE_MAKEUP("기타(베이스메이크업)", 3),

    BODY_HAIRCARE("바디/헤어케어", 2),
    SHAMPOO_CONDITIONER("샴푸/린스", 3),
    HAIR_ESSENCE_TREATMENT("헤어에센스/트리트먼트", 3),
    HAIR_STYLING("헤어스타일링", 3),
    HAIR_COLOR("헤어컬러(염색)", 3),
    HAIR_LOSS_CARE("발모제/탈모관련", 3),
    BODY_WASH_LOTION("바디클렌져/로션", 3),
    HAND_FOOT_CARE("핸드/풋케어", 3),
    OTHER_HAIR_BODY("기타(헤어,바디)", 3),

    PERFUME_AROMA("향수/아로마", 2),
    WOMEN_PERFUME("여성 향수", 3),
    MEN_PERFUME("남성 향수", 3),
    UNISEX_PERFUME("남여공용 향수", 3),
    OTHER_PERFUME_AROMA("기타(향수/아로마)", 3),

    NAIL_ART_CARE("네일아트/케어", 2),
    NAIL_ART_STICKER("네일아트/스티커", 3),
    MANICURE_PEDICURE("매니큐어/패디큐어", 3),
    NAIL_CARE_TOOL("네일케어도구", 3),
    NAIL_REMOVER("네일리무버", 3),
    OTHER_NAIL_ART_CARE("기타(네일아트/케어)", 3),

    BEAUTY_TOOL_DEVICE("미용소품/기기", 2),
    BEAUTY_TOOL("뷰티소품(퍼프/거울)", 3),
    MAKEUP_BRUSH("메이크업 브러쉬", 3),
    COSMETIC_POUCH_ORGANIZER("화장품 파우치/정리함", 3),
    OTHER_BEAUTY_ACCESSORY("기타(이미용품)", 3),

    DIET_INNER_BEAUTY("다이어트/이너뷰티", 2),

    MEN_COSMETICS("남성 화장품", 2),
    MEN_SKIN_LOTION("스킨/로션", 3),
    MEN_CLEANSING_MASK("클렌징/마스크", 3),
    MEN_ESSENCE_CREAM("에센스/크림", 3),
    MEN_BB_CREAM("BB크림", 3),
    MEN_SUNCARE("선케어", 3),
    MEN_WAX("왁스", 3),
    MEN_ALL_IN_ONE("올인원", 3),
    MEN_SET("세트", 3),
    OTHER_MEN_COSMETICS("기타(남성화장품)", 3),

    // 가구/인테리어
    FURNITURE_INTERIOR("가구/인테리어", 1),

    FURNITURE("가구", 2),
    LIVING_ROOM_FURNITURE("거실가구", 3),
    BEDROOM_FURNITURE("침실가구", 3),
    STUDY_OFFICE_FURNITURE("학생/서재/사무용가구", 3),
    SHELF_STORAGE_FURNITURE("선반/수납 가구", 3),
    KITCHEN_FURNITURE("주방 가구", 3),

    BEDDING("침구", 2),

    HANDICRAFT_REPAIR("수공예/수선", 2),
    HANDICRAFT("수공예품", 3),
    HANDICRAFT_MATERIAL("수공예 용품/부자재", 3),

    DIY_INTERIOR("셀프 인테리어 용품", 2),

    INTERIOR_ACCESSORY("인테리어 소품", 2),
    POSTER_PICTURE_FRAME("포스터/그림/액자", 3),
    DIFFUSER_CANDLE("디퓨저/캔들", 3),
    CUSHION_PILLOW("쿠션/방석", 3),
    TABLE_WALL_CLOCK("탁상/벽시계", 3),
    TABLECLOTH_MAT("식탁보/테이블 매트", 3),
    MIRROR("거울", 3),
    OTHER_INTERIOR_ACCESSORY("기타 인테리어 소품", 3),

    FLOWER_GARDENING("꽃/원예", 2),
    PLANT_FLOWER("식물/꽃", 3),
    GARDENING_SUPPLIES("원예 용품", 3),

    LIGHTING("조명", 2),

    CARPET_RUG_MAT("카페트/러그/매트", 2),

    CURTAIN_BLIND("커튼/블라인드", 2),

    // 생활/주방용품
    LIVING_KITCHENWARE("생활/주방용품", 1),

    KITCHENWARE("주방용품", 2),
    TABLEWARE_SET("그릇/홈세트", 3),
    CUP_GLASS("잔/컵", 3),
    TUMBLER_WATER_BOTTLE("텀블러/물병", 3),
    CUTLERY("수저/커트러리", 3),
    COMMERCIAL_SUPPLY("업소용품/기기", 3),
    POT_PAN("냄비/프라이팬", 3),
    COOKING_TOOL("조리도구", 3),
    KNIFE_CUTTING_BOARD("칼/도마", 3),
    STORAGE_CONTAINER("보관/밀폐용기", 3),
    KETTLE_TEA_POT("주전자/티포트", 3),
    COFFEE_ACCESSORY("커피용품", 3),
    BAKING_TOOL("제과/제빵", 3),
    KITCHEN_STORAGE("주방수납용품", 3),
    OTHER_KITCHENWARE("기타 주방용품", 3),

    BATHROOM_SUPPLY("욕실용품", 2),
    ORAL_SHAVING_SUPPLY("구강/면도용품", 3),
    SHOWER_BATH_SUPPLY("샤워/목욕용품", 3),
    TOWEL("수건/타월", 3),
    SHOWER_HEAD_ACCESSORY("샤워기/헤드/수전용품", 3),
    BATHROOM_STORAGE("욕실수납/정리", 3),
    BATHTUB_SPA_SUPPLY("욕조/반신욕용품", 3),
    TOILET_BIDET_SUPPLY("변기/비데용품", 3),
    OTHER_BATHROOM_SUPPLY("기타 욕실용품", 3),

    LIVING_SUPPLY("생활용품", 2),
    CLEANING_SUPPLY("청소용품", 3),
    LAUNDRY_SUPPLY("세탁/빨래용품", 3),
    DETERGENT("세제", 3),
    TISSUE("화장지", 3),
    SANITARY_PAD("생리대", 3),
    OTHER_LIVING_SUPPLY("기타 생활용품", 3),

    // 공구/산업용품
    TOOL_SUPPLY("공구/산업용품", 1),

    DRILL_POWER_TOOL("드릴/전동공구", 2),

    HAND_TOOL_HOME_TOOL("수공구/가정용 공구", 2),

    TOOLBOX("공구함", 2),

    INDUSTRIAL_SUPPLY_MATERIAL("산업용품/자재", 2),

    MEASURING_INSTRUMENT_LEVEL("측정/계측/레벨", 2),

    FACTORY_MACHINE_WELDING_GAS("공장기계/용접/가스", 2),

    AIR_HYDRAULIC_TOOL("에어/유압공구", 2),

    OTHER_INDUSTRIAL_SUPPLY("기타 산업용품", 2),

    // 식품
    FOOD("식품", 1),

    HEALTH_FOOD("건강식품", 2),

    HEALTH_FUNCTIONAL_FOOD("건강기능식품", 2),

    AGRICULTURAL_FISHERY_ANIMAL_PRODUCT("농수축산물", 2),

    SNACK("간식", 2),

    COFFEE_TEA("커피/차", 2),

    WATER_BEVERAGE("생수/음료", 2),

    NOODLE_CANNED_FOOD("면/통조림", 2),

    SAUCE_OIL("장/소스/오일", 2),

    EASY_COOKING_FOOD("간편조리식품", 2),

    OTHER_FOOD("기타 식품", 2),

    // 유아동/출산
    CHILD_CHILDBIRTH("유아동/출산", 1),

    BABY_CLOTH_0_2("베이비 의류(0-2세)", 2),
    BABY_DRESS("원피스", 3),
    BABY_TOP("유아상의", 3),
    BABY_BOTTOM("유아하의", 3),
    BABY_UNDERWEAR("유아내의/속옷", 3),
    OTHER_BABY_CLOTH("기타 베이비의류", 3),

    GIRL_CLOTH_3_6("여아의류(3-6세)", 2),
    GIRL_SET_3_6("상하복/세트", 3),
    GIRL_DRESS_3_6("원피스", 3),
    GIRL_JACKET_JUMPER_3_6("자켓/점퍼", 3),
    GIRL_COAT_SUIT_3_6("코트/정장", 3),
    GIRL_CARDIGAN_VEST_3_6("가디건/조끼", 3),
    GIRL_KNIT_SWEATER_3_6("니트/스웨터", 3),
    GIRL_BLOUSE_SHIRT_3_6("블라우스/셔츠", 3),
    GIRL_TSHIRT_3_6("티셔츠", 3),
    GIRL_PANTS_3_6("바지", 3),
    GIRL_SKIRT_3_6("치마", 3),
    GIRL_UNDERWEAR_PAJAMAS_3_6("속옷/잠옷", 3),
    OTHER_GIRL_CLOTH_3_6("기타 여아의류", 3),

    BOY_CLOTH_3_6("남아의류(3-6세)", 2),
    BOY_SET_3_6("상하복/세트", 3),
    BOY_JACKET_JUMPER_3_6("자켓/점퍼", 3),
    BOY_COAT_SUIT_3_6("코트/정장", 3),
    BOY_CARDIGAN_VEST_3_6("가디건/조끼", 3),
    BOY_KNIT_SWEATER_3_6("니트/스웨터", 3),
    BOY_BLOUSE_SHIRT_3_6("블라우스/셔츠", 3),
    BOY_TSHIRT_3_6("티셔츠", 3),
    BOY_PANTS_3_6("바지", 3),
    BOY_UNDERWEAR_PAJAMAS_3_6("속옷/잠옷", 3),
    OTHER_BOY_CLOTH_3_6("기타 남아의류", 3),

    GIRL_JUNIOR_CLOTH_7("여아주니어의류(7세~)", 2),
    GIRL_JUNIOR_SET("상하복/세트", 3),
    GIRL_JUNIOR_DRESS("원피스", 3),
    GIRL_JUNIOR_JACKET_JUMPER("자켓/점퍼", 3),
    GIRL_JUNIOR_COAT_SUIT("코트/정장", 3),
    GIRL_JUNIOR_CARDIGAN_VEST("가디건/조끼", 3),
    GIRL_JUNIOR_KNIT_SWEATER("니트/스웨터", 3),
    GIRL_JUNIOR_BLOUSE_SHIRT("블라우스/셔츠", 3),
    GIRL_JUNIOR_TSHIRT("티셔츠", 3),
    GIRL_JUNIOR_PANTS("바지", 3),
    GIRL_JUNIOR_SKIRT("치마", 3),
    GIRL_JUNIOR_UNDERWEAR_PAJAMAS("속옷/잠옷", 3),
    OTHER_GIRL_JUNIOR_CLOTH("기타 여주니어의류", 3),

    BOY_JUNIOR_CLOTH_7("남아주니어의류(7세~)", 2),
    BOY_JUNIOR_SET("상하복/세트", 3),
    BOY_JUNIOR_JACKET_JUMPER("자켓/점퍼", 3),
    BOY_JUNIOR_COAT_SUIT("코트/정장", 3),
    BOY_JUNIOR_CARDIGAN_VEST("가디건/조끼", 3),
    BOY_JUNIOR_KNIT_SWEATER("니트/스웨터", 3),
    BOY_JUNIOR_BLOUSE_SHIRT("블라우스/셔츠", 3),
    BOY_JUNIOR_TSHIRT("티셔츠", 3),
    BOY_JUNIOR_PANTS("바지", 3),
    BOY_JUNIOR_UNDERWEAR_PAJAMAS("속옷/잠옷", 3),
    OTHER_BOY_JUNIOR_CLOTH("기타 남주니어의류", 3),

    KIDS_SHOES_BAG_ACCESSORY("신발/가방/잡화", 2),
    KIDS_ACCESSORY("액세서리", 3),
    KIDS_SHOES("신발", 3),
    KIDS_BAG_WALLET("가방/지갑", 3),
    KIDS_HAT("모자", 3),
    KIDS_SOCKS("양말", 3),
    OTHER_KIDS_ACCESSORY("기타 신발/가방/잡화", 3),

    BABY_SUPPLY("유아동용품", 2),
    STROLLER("유모차", 3),
    BABY_CARRIER("아기띠", 3),
    CAR_SEAT("카시트", 3),
    WALKER_SAUCER("보행기/쏘서", 3),
    DIAPER("기저귀", 3),
    FURNITURE_BED_MAT("가구/침대/매트", 3),
    BABY_BEDDING("이불/침구", 3),
    BATH_ORAL_SUPPLY("목욕/구강용품", 3),
    LAUNDRY_HYGIENE_SUPPLY("세탁/위생용품", 3),
    BABY_SKINCARE("유아동 스킨케어", 3),
    OTHER_BABY_SUPPLY("기타 유아동용품", 3),

    MATERNITY_CLOTH_SUPPLY("임부 의류/용품", 2),
    MATERNITY_CLOTH_FEEDING_WEAR("임부의류/수유복", 3),
    MATERNITY_SKINCARE("임부스킨케어", 3),
    OTHER_MATERNITY_SUPPLY("기타 임부 의류/용품", 3),

    EDUCATIONAL_TOY_DOLL("교구/완구/인형", 2),
    BABY_DOLL("인형(유아용)", 3),
    CHARACTER_TOY_ROBOT("캐릭터완구/로봇", 3),
    EDUCATIONAL_TOOL_CD_DVD("교구/CD/DVD", 3),
    PUZZLE_BLOCK("퍼즐/블록", 3),
    SPORTS_TOY("스포츠완구", 3),
    NEWBORN_TOY("신생아완구", 3),
    BICYCLE_RIDE_TOY("자전거/승용완구", 3),
    WATER_TOY_SEASONAL_SUPPLY("물놀이/계절용품", 3),
    PLAYHOUSE_TENT_SLIDE("놀이집/텐트/미끄럼틀", 3),
    OTHER_EDUCATIONAL_TOY("기타 교구/완구/인형", 3),

    FEEDING_WEANING_SUPPLY("수유/이유용품", 2),
    FEEDING_SUPPLY("수유용품", 3),
    BOTTLE_CLEANING_SUPPLY("젖병/세정용품", 3),
    BABY_FORMULA("분유", 3),
    BABY_FEEDING_TOOL("수저/식판/이유식용품", 3),
    OTHER_FEEDING_SUPPLY("기타 수유/이유용품", 3),

    // 반려동물용품
    PET("반려동물용품", 1),

    DOG_SUPPLY("강아지 용품", 2),

    DOG_FOOD_TREAT("강아지 사료/간식", 2),

    OTHER_DOG_SUPPLY("기타(강아지)", 2),

    CAT_SUPPLY("고양이 용품", 2),

    CAT_FOOD_TREAT("고양이 사료/간식", 2),

    OTHER_CAT_SUPPLY("기타(고양이)", 2),

    OTHER_PET_SUPPLY("기타(반려동물 용품)", 2),

    OTHER_PET_FOOD_TREAT("기타(반려동물 사료/간식)", 2),

    ETC("기타", 1);

    private final String description;
    private final int depth;

    private static final Map<String, CategoryName> DESCRIPTION_MAP = new HashMap<>();

    static {
        for (CategoryName category : CategoryName.values()) {
            DESCRIPTION_MAP.put(category.getDescription(), category);
        }
    }

    public static CategoryName from(String description) {
        return DESCRIPTION_MAP.get(description);
    }
}
