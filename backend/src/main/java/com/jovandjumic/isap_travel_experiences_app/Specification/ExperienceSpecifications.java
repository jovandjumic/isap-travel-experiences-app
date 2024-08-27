package com.jovandjumic.isap_travel_experiences_app.Specification;

import com.jovandjumic.isap_travel_experiences_app.entities.Destination;
import com.jovandjumic.isap_travel_experiences_app.entities.Experience;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class ExperienceSpecifications {

    public static Specification<Experience> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> {
            Join<Experience, Destination> destinationJoin = root.join("destination", JoinType.INNER);
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(destinationJoin.get("locationName")), "%" + location.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(destinationJoin.get("regionArea")), "%" + location.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(destinationJoin.join("country").get("countryName")), "%" + location.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(destinationJoin.join("country").get("continent")), "%" + location.toLowerCase() + "%")
            );
        };
    }

    public static Specification<Experience> hasDaysSpentBetween(Integer minDays, Integer maxDays) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("daysSpent"), minDays, maxDays);
    }

    public static Specification<Experience> hasTotalCostBetween(Double minCost, Double maxCost) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.join("costs").get("totalCost"), minCost, maxCost);
    }

    public static Specification<Experience> hasCostPerPersonBetween(Double minCostPerPerson, Double maxCostPerPerson) {
        return (root, query, criteriaBuilder) -> {
            Expression<Double> costPerPersonExpression = criteriaBuilder.function(
                    "getCostPerPerson",
                    Double.class,
                    root
            );
            return criteriaBuilder.between(costPerPersonExpression, minCostPerPerson, maxCostPerPerson);
        };
    }
}
