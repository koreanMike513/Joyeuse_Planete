package com.f_lab.joyeuse_planete.foods.repository;

import com.f_lab.joyeuse_planete.foods.dto.request.FoodSearchCondition;
import com.f_lab.joyeuse_planete.foods.dto.response.FoodDTO;
import com.f_lab.joyeuse_planete.foods.dto.response.QFoodDTO;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static com.f_lab.joyeuse_planete.core.domain.QFood.food;
import static com.f_lab.joyeuse_planete.core.domain.QStore.store;


public class FoodCustomRepositoryImplJPA {

  private final JPAQueryFactory queryFactory;
  private static final Map<String, OrderSpecifier> sortByMap = Map.of(
      "RATE_HIGH", food.rate.desc()
  );

  public FoodCustomRepositoryImplJPA(EntityManager em) {
    queryFactory = new JPAQueryFactory(em);
  }

  public Page<FoodDTO> getFoodList(FoodSearchCondition condition, Pageable pageable) {
    List<FoodDTO> result = queryFactory
        .select(new QFoodDTO(
            food.id.as("foodId"),
            food.store.id.as("storeId"),
            food.foodName.as("foodName"),
            food.price,
            food.totalQuantity.as("totalQuantity"),
            food.currencyCode.as("currencyCode"),
            food.currencySymbol.as("currencySymbol"),
            food.rate,
            food.collectionStartTime,
            food.collectionEndTime
        ))
        .from(food)
        .innerJoin(food.store, store)
        .where(eqFoodNameTagsAndStoreName(condition.getSearch()))
        .orderBy(getOrderSpecifiers(condition.getSortBy()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    long count = queryFactory
        .select(food.count())
        .from(food)
        .fetch()
        .get(0);

    return new PageImpl<>(result, pageable, count);
  }

  private BooleanExpression eqFoodNameTagsAndStoreName(String search) {
    return (search != null)
        ? Expressions.anyOf(
        food.foodName.containsIgnoreCase(search),
        Expressions.booleanTemplate(
            "LOWER({0}) LIKE LOWER(CONCAT('%', {1}, '%'))",
            food.tags, search),
        food.store.name.containsIgnoreCase(search))

        : null;
  }

  private OrderSpecifier[] getOrderSpecifiers(List<String> sortBy) {
    OrderSpecifier[] specifiedOrders = sortBy.stream()
        .filter(sortByMap::containsKey)
        .map(sortByMap::get)
        .toArray(OrderSpecifier[]::new);

    return specifiedOrders.length > 0
        ? specifiedOrders
        : new OrderSpecifier[]{ food.rate.desc() };
  }
}
