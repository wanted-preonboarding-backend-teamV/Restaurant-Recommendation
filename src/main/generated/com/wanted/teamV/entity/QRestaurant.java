package com.wanted.teamV.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurant is a Querydsl query type for Restaurant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurant extends EntityPathBase<Restaurant> {

    private static final long serialVersionUID = 577319169L;

    public static final QRestaurant restaurant = new QRestaurant("restaurant");

    public final NumberPath<Double> averageRating = createNumber("averageRating", Double.class);

    public final StringPath bsnStateNm = createString("bsnStateNm");

    public final StringPath circumfrDivNm = createString("circumfrDivNm");

    public final DateTimePath<java.time.LocalDateTime> clsbizDe = createDateTime("clsbizDe", java.time.LocalDateTime.class);

    public final NumberPath<Integer> femaleEnflpsnCnt = createNumber("femaleEnflpsnCnt", Integer.class);

    public final StringPath gradDivNm = createString("gradDivNm");

    public final StringPath gradFacltDivNm = createString("gradFacltDivNm");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final DateTimePath<java.time.LocalDateTime> licenseDe = createDateTime("licenseDe", java.time.LocalDateTime.class);

    public final NumberPath<Double> locplcAr = createNumber("locplcAr", Double.class);

    public final NumberPath<Double> lon = createNumber("lon", Double.class);

    public final StringPath lotAddress = createString("lotAddress");

    public final NumberPath<Integer> maleEnflpsnCnt = createNumber("maleEnflpsnCnt", Integer.class);

    public final BooleanPath multiUseBizestblYn = createBoolean("multiUseBizestblYn");

    public final StringPath name = createString("name");

    public final ListPath<Rating, QRating> ratingList = this.<Rating, QRating>createList("ratingList", Rating.class, QRating.class, PathInits.DIRECT2);

    public final StringPath roadnameAddress = createString("roadnameAddress");

    public final StringPath sanittnIndutypeNm = createString("sanittnIndutypeNm");

    public final StringPath sigun = createString("sigun");

    public final StringPath sigunCd = createString("sigunCd");

    public final NumberPath<Integer> totEmplyCnt = createNumber("totEmplyCnt", Integer.class);

    public final NumberPath<Double> totFacltScale = createNumber("totFacltScale", Double.class);

    public final StringPath type = createString("type");

    public final NumberPath<Integer> yy = createNumber("yy", Integer.class);

    public final StringPath zipCode = createString("zipCode");

    public QRestaurant(String variable) {
        super(Restaurant.class, forVariable(variable));
    }

    public QRestaurant(Path<? extends Restaurant> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRestaurant(PathMetadata metadata) {
        super(Restaurant.class, metadata);
    }

}

